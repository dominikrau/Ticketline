package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.Instant;


@Value
@Builder(builderClassName = "Builder", toBuilder = true)
public class HallDto {

    Long id;
    Instant createdAt;
    @NotBlank
    @Size()
    String name;
    @NotNull
    @Min(value = 0)
    Integer width;
    @NotNull
    @Min(value = 0)
    Integer height;
    @NotNull
    @Valid
    VenueDto venue;
    @JsonCreator
    public HallDto(
        @JsonProperty("hallId") Long id,
        @JsonProperty("createdAt") Instant createdAt,
        @NotBlank @JsonProperty("name") String name,
        @NotNull @JsonProperty("width") Integer width,
        @NotNull @JsonProperty("height") Integer height,
        @NotNull @JsonProperty("venue") VenueDto venue
    ) {
        this.id = id;
        this.createdAt = createdAt;
        this.name = name;
        this.width = width;
        this.height = height;
        this.venue = venue;
    }
}
