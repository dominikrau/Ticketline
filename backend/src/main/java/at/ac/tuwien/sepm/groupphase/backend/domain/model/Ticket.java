package at.ac.tuwien.sepm.groupphase.backend.domain.model;

import lombok.Builder;
import lombok.Value;

import java.time.Instant;

@Value
@Builder(builderClassName = "Builder", toBuilder = true)
public class Ticket {
    Long ticketId;
    Instant createdAt;
    ApplicationUser user;
    Show show;
    Sector sector;
    Seat seat;
    Double price;
    TicketStatus status;
    String reservationNumber;
    Order ticketOrder;
}
