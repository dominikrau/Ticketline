package at.ac.tuwien.sepm.groupphase.backend.domain.model;

import lombok.Builder;
import lombok.Value;

@Value
@Builder(builderClassName = "Builder", toBuilder = true)
public class Seat {
    Long id;
    Integer x;
    Integer y;
    Boolean available;
    Sector sector;
}
