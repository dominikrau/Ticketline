package at.ac.tuwien.sepm.groupphase.backend.domain.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Setter
@Getter
@ToString
@EqualsAndHashCode(callSuper = true)
@SuperBuilder
public class SittingSector extends Sector{
    List<Seat> seats;

    /**
     * Gets the type description of the Sector. In this case a Sitting Sector
     * @return a String "Sitting"
     */
    @Override
    public String getTypeDescription() {
        return "Sitting";
    }
}
