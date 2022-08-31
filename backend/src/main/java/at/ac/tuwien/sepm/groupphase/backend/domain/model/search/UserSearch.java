package at.ac.tuwien.sepm.groupphase.backend.domain.model.search;

import lombok.Builder;
import lombok.Value;

@Value
@Builder(builderClassName = "Builder", toBuilder = true)
public class UserSearch {
    String firstName;
    String lastName;
    boolean blocked;

}
