package at.ac.tuwien.sepm.groupphase.backend.domain.model;


import lombok.Builder;
import lombok.Data;
import lombok.Setter;
import lombok.Value;

import java.time.Instant;
import java.util.List;

@Data
@Builder(builderClassName = "Builder", toBuilder = true)
public class SeatingChart {
    Long id;
    Instant createdAt;
    String name;
    Hall hall;
    List<Sector> sectors;
    Stage stage;

}
