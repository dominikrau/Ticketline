package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Value
@Builder(builderClassName = "Builder", toBuilder = true)
public class SeatDto {

    Long id;
    @NotNull
    @Min(value = 0)
    Integer x;
    @NotNull
    @Min(value = 0)
    Integer y;
    Boolean available;

    @JsonCreator

    public SeatDto(
        @JsonProperty("seatId") Long id,
        @NotBlank @JsonProperty("x") Integer x,
        @NotBlank @JsonProperty("y") Integer y,
        @JsonProperty("available") Boolean available
    ) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.available = available;
    }
}
