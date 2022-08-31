package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.time.Instant;

@Value
@Builder(builderClassName = "Builder", toBuilder = true)
public class PurchaseTicketDto {

    Long ticketId;
    Instant createdAt;
    @NotNull
    @Valid
    ShowDto show;
    @NotNull
    @Valid
    SectorDto sector;
    @Valid
    SeatDto seat;
    @NotNull
    @Min(value = 0)
    Double price;
    @NotNull
    @Pattern(regexp = "^(?:RESERVED|BOUGHT|CANCELLED)$", message = "ticket must be from type RESERVED, BOUGHT or CANCELLED")
    String status;

    @JsonCreator
    public PurchaseTicketDto(
        @JsonProperty("showId") Long ticketId,
        @JsonProperty("createdAt") Instant createdAt,
        @NotNull @JsonProperty("show") ShowDto show,
        @NotNull @Valid @JsonProperty("sector") SectorDto sector,
        @Valid @JsonProperty("seat") SeatDto seat,
        @JsonProperty("price") Double price,
        @NotNull @JsonProperty("status") String status
    ){
        this.ticketId = ticketId;
        this.createdAt = createdAt;
        this.show = show;
        this.sector = sector;
        this.seat = seat;
        this.price = price;
        this.status = status;
    }
}
