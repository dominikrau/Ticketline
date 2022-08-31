package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.domain.CancellationPdfCreator;
import at.ac.tuwien.sepm.groupphase.backend.domain.TicketPdfCreator;
import at.ac.tuwien.sepm.groupphase.backend.domain.model.Ticket;
import at.ac.tuwien.sepm.groupphase.backend.domain.model.TicketStatus;
import at.ac.tuwien.sepm.groupphase.backend.exception.ValidationException;
import at.ac.tuwien.sepm.groupphase.backend.repository.TicketRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.TicketService;
import at.ac.tuwien.sepm.groupphase.backend.service.UserService;
import at.ac.tuwien.sepm.groupphase.backend.util.Validator;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class SimpleTicketService implements TicketService {

    private final TicketRepository ticketRepository;
    private final UserService userService;
    private final Validator validator;

    @Override
    public List<Ticket> findAll() {
        log.debug("Find all tickets");
        return ticketRepository.findAllByOrderByCreatedAt();
    }

    @Override
    public Ticket findById(Long id) {
        log.debug("Find ticket with id {}", id);
        return createReservationNumberTicket(ticketRepository.findById(id));
    }

    @Override
    public List<Ticket> saveTicket(final List<Ticket> newTickets) {
        return ticketRepository.saveTicket(newTickets)
            .stream()
            .map(this::createReservationNumberTicket)
            .collect(Collectors.toList());
    }

    @Transactional
    @Override
    public void cancelTicket(final Long ticketId) {
        Ticket ticket = ticketRepository.findById(ticketId);
        validator.validateCancelTicket(ticket, userService.getCurrentUser());
        if (ticket.getStatus() == TicketStatus.RESERVED) {
            ticketRepository.deleteTicket(ticket.getTicketId());
        } else if (ticket.getStatus() == TicketStatus.BOUGHT) {
            ticketRepository.updateTicketStatus(ticket.getTicketId(), TicketStatus.CANCELLED);
        } else {
            throw new ValidationException("Trying to Cancel Ticket already in Status Cancelled");
        }
    }

    @Override
    public void updateTicketStatus(List<Long> ticketIds, TicketStatus status) {
        ticketRepository.updateTicketStatus(ticketIds, status);
    }

    @Override
    @SneakyThrows
    public byte[] createCancellationPdf(Long id) {
        final Ticket ticket = ticketRepository.findById(id);
        return new CancellationPdfCreator(ticket).createPdf();
    }

    private Ticket createReservationNumberTicket(Ticket ticket) {
        log.debug("Create reservationNumberTicket {}", ticket);
        if (ticket.getStatus() == TicketStatus.RESERVED) {
            String reservationNumber = Long.toHexString(ticket.getCreatedAt().toEpochMilli()) + ticket.getTicketId();
            ticket = ticket.toBuilder().reservationNumber(reservationNumber).build();
        }
        return ticket;
    }

    @Override
    public List<Ticket> findTicketsForOrderWithId(Long id) {
        return ticketRepository.findTicketsForOrderWithId(id);
    }

    @Override
    @SneakyThrows
    public byte[] createTicketPdf(Long orderId) {
        log.debug("Create TicketPDF with id {}", orderId);
        List<Ticket> tickets = validator.validateTicketPdf(orderId);
        return new TicketPdfCreator(tickets)
            .createPdf();
    }

}
