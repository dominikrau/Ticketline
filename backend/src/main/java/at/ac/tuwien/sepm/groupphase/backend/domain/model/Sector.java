package at.ac.tuwien.sepm.groupphase.backend.domain.model;


import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Getter
@ToString
@Setter
@EqualsAndHashCode
@SuperBuilder
public abstract class Sector {
    Long id;
    String name;
    String color;

    /**
     * Gets the type description of the Sector. It can be a Sitting or a Standing Sector
     * @return a String (either "Standing" or "Sitting")
     */
    public abstract String getTypeDescription();
}
