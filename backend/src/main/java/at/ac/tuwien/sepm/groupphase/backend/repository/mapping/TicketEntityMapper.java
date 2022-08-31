package at.ac.tuwien.sepm.groupphase.backend.repository.mapping;

import at.ac.tuwien.sepm.groupphase.backend.domain.model.Ticket;
import at.ac.tuwien.sepm.groupphase.backend.domain.model.TicketStatus;
import at.ac.tuwien.sepm.groupphase.backend.repository.entity.SittingSectorEntity;
import at.ac.tuwien.sepm.groupphase.backend.repository.entity.TicketEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
public class TicketEntityMapper {

    private final SectorEntityMapper sectorEntityMapper;
    private final ShowEntityMapper showEntityMapper;
    private final UserEntityMapper userEntityMapper;
    private final OrderEntityMapper orderEntityMapper;
    private final SeatingChartEntityMapper seatingChartEntityMapper;


    /**
     * Maps the given Ticket Entity to a Domain representation of the Ticket
     *
     * @param ticketEntity the Ticket Entity to be mapped
     * @return the mapped Ticket
     */
    public Ticket toDomain(TicketEntity ticketEntity) {
        if (ticketEntity == null) {
            return null;
        }

        return Ticket.builder()
            .ticketId(ticketEntity.getTicketId())
            .createdAt(ticketEntity.getCreatedAt())
            .user(userEntityMapper.toDomain(ticketEntity.getUser()))
            .show(showEntityMapper.toDomain(ticketEntity.getShow()))
            .ticketOrder(orderEntityMapper.toDomain(ticketEntity.getTicketOrder()))
            .price(ticketEntity.getPrice())
            .seat(SeatEntityMapper.toDomain(ticketEntity.getSeat()))
            .sector(sectorEntityMapper.toDomain(ticketEntity.getSector()))
            .status(TicketStatus.fromCode(ticketEntity.getStatus()))
            .build();
    }

    /**
     * Maps the given Tickets Entities in a List to a List of Domain representations of the Tickets
     *
     * @param ticketEntities the List of Ticket Entities to be mapped
     * @return the mapped Ticket List
     */
    public List<Ticket> toDomain(List<TicketEntity> ticketEntities) {
        if (ticketEntities == null) {
            return new ArrayList<>();
        }

        List<Ticket> list = new ArrayList<>();

        for (TicketEntity ticketEntity : ticketEntities) {
            list.add(toDomain(ticketEntity));
        }

        return list;
    }



    /**
     * Maps the given Ticket to a Repository Entity representation of the Ticket
     *
     * @param ticket the Ticket to be mapped
     * @return the mapped Ticket Entity
     */
    public TicketEntity toEntity(Ticket ticket) {
        TicketEntity.Builder ticketEntity = TicketEntity.builder()
            .ticketId(ticket.getTicketId())
            .createdAt(ticket.getCreatedAt())
            .price(ticket.getPrice())
            .show(showEntityMapper.toEntity(ticket.getShow()))
            .user(userEntityMapper.toEntity(ticket.getUser()))
            //.seat(seatEntityMapper.toEntity(ticket.getSeat(),((SittingSectorEntity) sectorEntityMapper.toEntity(ticket.getSector(), seatingChartEntityMapper.toEntity(ticket.getShow().getSeatingChart())))))
            .sector(sectorEntityMapper.toEntity(ticket.getSector(), seatingChartEntityMapper.toEntity(ticket.getShow().getSeatingChart())))
            .status(ticket.getStatus().getCode())
            .ticketOrder(orderEntityMapper.toEntity(ticket.getTicketOrder()));
        if (ticket.getSeat() != null) {
            ticketEntity = ticketEntity.seat(SeatEntityMapper.toEntity(
                ticket.getSeat(),
                ((SittingSectorEntity) sectorEntityMapper.toEntity(
                    ticket.getSector(),
                    seatingChartEntityMapper.toEntity(ticket.getShow().getSeatingChart())
                ))
            ));
        }
        return ticketEntity.build();
    }

    /**
     * Maps the given Tickets in a List to a List of Repository Entity representations of the Tickets
     *
     * @param tickets the List of Tickets to be mapped
     * @return the mapped Ticket Entity List
     */
    public List<TicketEntity> toEntity(List<Ticket> tickets) {
        if (tickets == null) {
            return new ArrayList<>();
        }

        List<TicketEntity> list1 = new ArrayList<>(tickets.size());
        for (Ticket ticket : tickets) {
            list1.add(toEntity(ticket));
        }

        return list1;
    }
}
