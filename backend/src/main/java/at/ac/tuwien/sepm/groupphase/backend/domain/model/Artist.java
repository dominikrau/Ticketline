package at.ac.tuwien.sepm.groupphase.backend.domain.model;

import lombok.Builder;
import lombok.Value;

import java.time.Instant;

@Value
@Builder(builderClassName = "Builder", toBuilder = true)
public class Artist {

    Long id;
    Instant createdAt;
    String firstName;
    String lastName;
    String pseudonym;

}
