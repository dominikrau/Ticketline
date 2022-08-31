package at.ac.tuwien.sepm.groupphase.backend.domain.model;

import lombok.Builder;
import lombok.Value;

import java.util.Collection;

@Value
@Builder(builderClassName = "Builder")
public class PurchaseValidation {

    Show show;
    Collection<StandingSector> standingSectors;
    Collection<Seat> seats;

}
