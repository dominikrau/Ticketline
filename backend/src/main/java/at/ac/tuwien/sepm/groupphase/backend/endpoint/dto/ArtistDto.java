package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.NotNull;
import java.time.Instant;

@Slf4j
@Value
@Builder(builderClassName = "Builder", toBuilder = true)
public class ArtistDto {

    Long id;
    Instant createdAt;
    String firstName;
    String lastName;
    String pseudonym;

    public ArtistDto(@JsonProperty("id") Long id, @JsonProperty("createdAt") Instant createdAt,
                     @NotNull @JsonProperty("firstName") String firstName,
                     @NotNull @JsonProperty("lastName") String lastName,
                     @NotNull @JsonProperty("pseudonym") String pseudonym) {
        this.id = id;
        this.createdAt = createdAt;
        this.firstName = firstName;
        this.lastName = lastName;
        this.pseudonym = pseudonym;
    }
}
