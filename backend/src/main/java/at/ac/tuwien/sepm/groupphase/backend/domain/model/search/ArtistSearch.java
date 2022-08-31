package at.ac.tuwien.sepm.groupphase.backend.domain.model.search;

import lombok.Builder;
import lombok.Value;
import org.springframework.util.StringUtils;

@Value
@Builder(builderClassName = "Builder", toBuilder = true)
public class ArtistSearch {

    String firstName;
    String lastName;
    String pseudonym;

    /**
     * Checks if the Artist Search domain representation is empty
     * @return true if the Artist Search is empty
     */
    public boolean isEmpty() {
        return StringUtils.isEmpty(firstName) &&
            StringUtils.isEmpty(lastName) &&
            StringUtils.isEmpty(pseudonym);
    }

}
