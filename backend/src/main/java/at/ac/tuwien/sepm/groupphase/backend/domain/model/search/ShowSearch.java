package at.ac.tuwien.sepm.groupphase.backend.domain.model.search;

import lombok.Builder;
import lombok.Value;
import org.springframework.util.StringUtils;

import java.time.Duration;
import java.time.LocalTime;

@Value
@Builder(builderClassName = "Builder", toBuilder = true)
public class ShowSearch {

    LocalTime startTime;
    LocalTime endTime;
    Duration duration;
    Double minPrice;
    Double maxPrice;
    String location;
    String hall;

    /**
     * Checks if the Show Search domain representation is empty
     * @return true if the Show Search is empty
     */
    public boolean isEmpty() {
        return startTime == null &&
            endTime == null &&
            duration == null &&
            minPrice == null &&
            maxPrice == null &&
            StringUtils.isEmpty(location) &&
            StringUtils.isEmpty(hall);
    }

}
