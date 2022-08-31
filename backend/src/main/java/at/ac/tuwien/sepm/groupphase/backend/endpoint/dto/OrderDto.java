package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import at.ac.tuwien.sepm.groupphase.backend.domain.model.PaymentMethod;
import at.ac.tuwien.sepm.groupphase.backend.domain.model.Show;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.NotNull;
import java.time.Instant;

@Value
@Builder(builderClassName = "Builder", toBuilder = true)
public class OrderDto {

    Long orderId;
    Instant createdAt;
    @NotNull
    Instant orderDate;
    @NotNull
    Show show;
    @NotNull
    PaymentMethod paymentMethod;


    @JsonCreator
    public OrderDto(
        @JsonProperty("orderId") Long orderId,
        @JsonProperty("createdAt") Instant createdAt,
        @NotNull @JsonProperty("orderDate") Instant orderDate,
        @NotNull @JsonProperty("show") Show show,
        @NotNull @JsonProperty("paymentMethod") PaymentMethod paymentMethod) {
        this.orderId = orderId;
        this.createdAt = createdAt;
        this.orderDate = orderDate;
        this.show = show;
        this.paymentMethod = paymentMethod;


    }
}
