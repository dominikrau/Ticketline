package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Value
@Builder(builderClassName = "Builder", toBuilder = true)
public class StageDto {

    Long id;

    @NotNull
    @Min(value = 0)
    Integer x;

    @NotNull
    @Min(value = 0)
    Integer y;

    @NotNull
    @Min(value = 0)
    Integer width;

    @NotNull
    @Min(value = 0)
    Integer height;

    @JsonCreator

    public StageDto(
        @JsonProperty("id") Long id,
        @NotNull @JsonProperty("x") Integer x,
        @NotNull @JsonProperty("y") Integer y,
        @NotNull @JsonProperty("widht") Integer width,
        @NotNull @JsonProperty("height") Integer height
    ) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }
}
