package at.ac.tuwien.sepm.groupphase.backend.domain.model;

import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder(builderClassName = "", toBuilder = true)
public class PurchaseIntent {

    long showId;
    Payment payment;
    List<Long> seats;
    List<StandingPurchaseIntent> standing;

}
