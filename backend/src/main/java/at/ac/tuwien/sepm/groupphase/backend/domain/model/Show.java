package at.ac.tuwien.sepm.groupphase.backend.domain.model;


import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;
import java.util.List;

@Value
@Builder(builderClassName = "Builder", toBuilder = true)
public class Show {
    Long showId;
    LocalDateTime createdAt;
    LocalDateTime startTime;
    LocalDateTime endTime;
    Venue venue;
    Event event;
    List<Pricing> pricings;
    SeatingChart seatingChart;
}
