package at.ac.tuwien.sepm.groupphase.backend.domain.model;

import lombok.Builder;
import lombok.Value;

import java.time.Instant;

@Value
@Builder(builderClassName = "Builder", toBuilder = true)
public class Order {
    Long orderId;
    PaymentMethod paymentMethod;
    Instant createdAt;
    Instant orderDate;
    Show show;
    ApplicationUser user;
}
