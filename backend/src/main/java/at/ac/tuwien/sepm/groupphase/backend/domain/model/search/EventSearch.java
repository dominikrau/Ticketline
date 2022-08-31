package at.ac.tuwien.sepm.groupphase.backend.domain.model.search;

import at.ac.tuwien.sepm.groupphase.backend.domain.model.EventType;
import lombok.Builder;
import lombok.Value;

@Value
@Builder(builderClassName = "Builder", toBuilder = true)
public class EventSearch {

    String name;
    String description;
    EventType eventType;
    ArtistSearch artistSearch;
    ShowSearch showSearch;

}
