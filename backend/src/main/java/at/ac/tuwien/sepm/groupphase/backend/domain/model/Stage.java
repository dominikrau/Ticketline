package at.ac.tuwien.sepm.groupphase.backend.domain.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder(builderClassName = "Builder", toBuilder = true)
public class Stage {
    Long id;
    Integer x;
    Integer y;
    Integer width;
    Integer height;
}
