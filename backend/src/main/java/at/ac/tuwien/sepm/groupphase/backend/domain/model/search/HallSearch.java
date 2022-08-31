package at.ac.tuwien.sepm.groupphase.backend.domain.model.search;

import lombok.Builder;
import lombok.Value;

@Value
@Builder(builderClassName = "Builder", toBuilder = true)
public class HallSearch {

    Long venueId;
    String name;

}
