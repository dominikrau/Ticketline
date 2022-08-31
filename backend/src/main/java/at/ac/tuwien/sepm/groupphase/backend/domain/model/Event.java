package at.ac.tuwien.sepm.groupphase.backend.domain.model;

import lombok.Builder;
import lombok.Singular;
import lombok.Value;

import java.util.List;

@Value
@Builder(builderClassName = "Builder", toBuilder = true)
public class Event {

    Long id;
    String name;
    String description;
    String imageUrl;
    EventType eventType;
    @Singular
    List<Artist> artists;
}
