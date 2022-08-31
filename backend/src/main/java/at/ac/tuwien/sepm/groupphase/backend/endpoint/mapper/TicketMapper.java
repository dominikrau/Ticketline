package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.domain.model.Ticket;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.PurchaseTicketDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.TicketDto;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper (uses = {ShowMapper.class, UserMapper.class, SectorMapper.class, SeatMapper.class})
public interface TicketMapper {
    /**
     * Maps the given Ticket DTO to a Domain representation of the Ticket
     *
     * @param ticketDto the Ticket DTO to be mapped
     * @return the mapped Ticket
     */
    @Named("ticket")
    @Mapping(source = "show", target = "show", qualifiedByName = "show")
    @Mapping(source = "sector", target = "sector", qualifiedByName = "toSectorDomainInterface")
    Ticket ticketDtoToTicket(TicketDto ticketDto);

    /**
     * Maps the given Purchase Ticket DTO to a Domain representation of the Ticket
     * This is need since a Purchase Ticket DTO doesn't have the following properties:
     * user, reservationNumber and ticketOrder
     *
     * @param ticketDto the Purchase Ticket DTO to be mapped
     * @return the mapped Ticket
     */
    @Mapping(source = "sector", target = "sector", qualifiedByName = "toSectorDomainInterface")
    @Mapping(source = "show", target = "show", qualifiedByName = "show")
    Ticket purchaseTicketDtoToTicket(PurchaseTicketDto ticketDto);

    /**
     * Maps the given Ticket to a Data Transfer Object representation of the Ticket
     *
     * @param ticket the Ticket to be mapped
     * @return the mapped Ticket DTO
     */
    @Named("ticketDto")
    @Mapping(source = "sector", target = "sector", qualifiedByName = "toSectorsDtoInterface")
    @Mapping(source = "show", target = "show", qualifiedByName = "showDto")
    TicketDto ticketToTicketDto(Ticket ticket);

    /**
     * Maps the given Tickets in a List to a List of Data Transfer Object representations of the Tickets
     *
     * @param tickets the List of Tickets to be mapped
     * @return the mapped Ticket DTO List
     */
    @IterableMapping(qualifiedByName = "ticketDto")
    List<TicketDto> ticketToTicketDto(List<Ticket> tickets);
}
