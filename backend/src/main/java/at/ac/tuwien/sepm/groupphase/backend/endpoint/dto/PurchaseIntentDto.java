package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Value;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@Value
@Builder(builderClassName = "Builder", toBuilder = true)
@JsonDeserialize(builder = PurchaseIntentDto.Builder.class)
public class PurchaseIntentDto {

    @NotNull
    long showId;
    @Valid
    @NotNull
    PaymentDto payment;
    List<Long> seats;
    List<StandingPurchaseIntentDto> standing;

    @JsonPOJOBuilder(withPrefix = "")
    public static final class Builder {
    }

}
