package at.ac.tuwien.sepm.groupphase.backend.domain.model;

import lombok.Builder;
import lombok.Value;

import java.time.Instant;

@Value
@Builder(builderClassName = "Builder", toBuilder = true)
public class Hall {
    Long id;
    Instant createdAt;
    String name;
    Integer width;
    Integer height;
    Venue venue;
}
