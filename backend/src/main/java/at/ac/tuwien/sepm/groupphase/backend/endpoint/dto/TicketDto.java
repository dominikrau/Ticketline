package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.Instant;

@Value
@Builder(builderClassName = "Builder", toBuilder = true)
public class TicketDto {

    Long ticketId;
    Instant createdAt;
    ApplicationUserDto user;
    @NotNull
    @Valid
    ShowDto show;
    @NotNull
    @Valid
    SectorDto sector;
    @Valid
    SeatDto seat;
    Double price;
    String status;
    String reservationNumber;
    OrderDto ticketOrder;


    @JsonCreator
    public TicketDto(
        @JsonProperty("showId") Long ticketId,
        @JsonProperty("createdAt") Instant createdAt,
        @JsonProperty("user") ApplicationUserDto user,
        @NotNull @JsonProperty("show") ShowDto show,
        @NotNull @Valid @JsonProperty("sector") SectorDto sector,
        @Valid @JsonProperty("seat") SeatDto seat,
        @JsonProperty("price") Double price,
        @JsonProperty("status") String status,
        @JsonProperty("reservationNumber") String reservationNumber,
        @JsonProperty("ticketOrder") OrderDto ticketOrder
    ){
        this.ticketId = ticketId;
        this.createdAt = createdAt;
        this.user = user;
        this.show = show;
        this.sector = sector;
        this.seat = seat;
        this.price = price;
        this.status = status;
        this.reservationNumber = reservationNumber;
        this.ticketOrder = ticketOrder;
    }

}
