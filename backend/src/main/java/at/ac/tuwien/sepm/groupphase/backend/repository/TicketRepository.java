package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.domain.model.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.domain.model.StandingSector;
import at.ac.tuwien.sepm.groupphase.backend.domain.model.Ticket;
import at.ac.tuwien.sepm.groupphase.backend.domain.model.TicketStatus;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.repository.entity.TicketEntity;
import at.ac.tuwien.sepm.groupphase.backend.repository.mapping.TicketEntityMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static java.lang.String.format;

@Slf4j
@Service
@RequiredArgsConstructor
public class TicketRepository {

    private final TicketJpaRepository repository;
    private final TicketEntityMapper mapper;

    /**
     * Find all ticket entries ordered by creation date.
     *
     * @return ordered list of all ticket entries
     */
    public List<Ticket> findAllByOrderByCreatedAt() {
        return repository.findAllByOrderByCreatedAt()
            .stream().map(mapper::toDomain).collect(Collectors.toList());
    }


    /**
     * Save a single ticket entry
     *
     * @param ticket to save
     * @return saved ticket entry
     */
    public Ticket saveTicket(Ticket ticket) {
        TicketEntity saved = repository.save(mapper.toEntity(ticket));
        log.debug("Ticket Repository, ticket saved: {}", ticket);
        return mapper.toDomain(saved);
    }

    /**
     * Save multiple ticket entries
     *
     * @param tickets to be saved
     * @return saved ticket entries
     */
    public List<Ticket> saveTicket(List<Ticket> tickets) {
        return mapper.toDomain(repository.saveAll(mapper.toEntity(tickets)));
    }


    /**
     * Find a single ticket entry by id.
     *
     * @param id the id of the ticket entry
     * @return the ticket entry
     */
    public Ticket findById(Long id) {
        return repository.findByTicketId(id)
            .map(mapper::toDomain)
            .orElseThrow(() -> new NotFoundException(
                format("No Ticket with given Id \"%s\"  found", id)
            ));
    }

    /**
     * Find all ticket entries for a corresponding order.
     *
     * @param orderId of the corresponding order.
     * @return list of all ticket entries for specified order.
     */
    public List<Ticket> findTicketsForOrderWithId(Long orderId) {
        log.debug("Find by orderId: {}", orderId);
        return mapper.toDomain(repository.findAllByTicketOrder_OrderId(orderId));
    }

    public long numberOfTickets() {
        return repository.count();
    }

    /**
     * Get the amount of occupied places in the specified sector and show
     * @param sectorId Id of the sector whose occupied places amount shall be counted
     * @param showId Id of the corresponding show
     * @return amount of occupied places
     */
    public Integer numberOfPlacesOccupied(Long sectorId, Long showId) {
        return repository.countAllBySector_IdAndShow_showIdAndStatusNotLike(sectorId, showId, "CANCELLED");
    }

    /**
     * Get the amount of free places in the specified sector and show
     * @param sector Id of the sector whose free places amount shall be counted
     * @param showId Id of the corresponding show
     * @return amount of free places
     */
    public Integer placesRemaining(StandingSector sector, Long showId) {
        return sector.getCapacity() - numberOfPlacesOccupied(sector.getId(), showId);
    }

    /**
     * Checks if a specified seat is free or occupied
     * @param seatId Id of seat whose status shall be checked
     * @param showId Id of show to which the seat belongs
     * @return true if specified seat is free
     */
    public boolean isSeatAvailable(Long seatId, Long showId) {
        return !repository.existsTicketEntityBySeat_IdAndShow_showIdAndStatusNotLike(seatId, showId, "CANCELLED");
    }


    /**
     * Updates the status of multiple Tickets
     * @param ids of tickets to be updated
     * @param status to which the tickets shall be updated
     */
    public void updateTicketStatus(List<Long> ids, TicketStatus status) {
        repository.updateTicketStatus(ids, status.getCode());
    }

    /**
     * Updates the status of a single Ticket
     * @param id of the ticket to be updated
     * @param status to which the ticket shall be updated
     */
    public void updateTicketStatus(Long id, TicketStatus status) {
        repository.updateTicketStatus(id, status.getCode());
    }

    /**
     * Delete specified Ticket from the database
     *
     * @param id of the Ticket to be deleted
     */
    public void deleteTicket(Long id) {
        repository.deleteById(id);
    }


    /**
     * Get all Tickets associated with the specified User
     * @param user whose tickets shall be retrieved
     * @return List of Tickets associated with the User
     */
    public List<Ticket> findTicketsOfUser(ApplicationUser user){
        log.debug("Ticket Repository: Getting Tickets of User: {}", user.getUserId());
        return mapper.toDomain(repository.findAllByUser_Id(user.getUserId()));
    }
}
