package at.ac.tuwien.sepm.groupphase.backend.domain.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Getter
@ToString
@Setter
@EqualsAndHashCode(callSuper = true)

@SuperBuilder
public class StandingSector extends Sector {
    Integer x;
    Integer y;
    Integer width;
    Integer height;
    Integer capacity;
    Integer available;

    /**
     * Gets the type description of the Sector. In this case a Standing Sector
     * @return a String "Standing"
     */
    @Override
    public String getTypeDescription() {
        return "Standing";
    }
}
