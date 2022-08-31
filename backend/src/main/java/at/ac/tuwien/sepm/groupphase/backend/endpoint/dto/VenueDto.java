package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.Instant;

@Value
@Builder(builderClassName = "Builder", toBuilder = true)
public class VenueDto {


    Long venueId;
    Instant createdAt;
    @NotBlank
    @Size(max = 255)
    String name;
    @NotNull
    AddressDto address;

    @JsonCreator
    public VenueDto(
        @JsonProperty("venueId") Long venueId,
        @JsonProperty("createdAt") Instant createdAt,
        @NotBlank @JsonProperty("name") String name,
        @NotNull @JsonProperty("address") AddressDto address
    ) {
        this.venueId = venueId;
        this.createdAt = createdAt;
        this.name = name;
        this.address = address;
    }
}
