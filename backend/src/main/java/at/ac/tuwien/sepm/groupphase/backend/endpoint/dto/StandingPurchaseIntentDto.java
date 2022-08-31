package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Value;

@Value
@Builder(builderClassName = "Builder", toBuilder = true)
@JsonDeserialize(builder = StandingPurchaseIntentDto.Builder.class)
public class StandingPurchaseIntentDto {

    long id;
    int amount;

    @JsonPOJOBuilder(withPrefix = "")
    public static final class Builder {
    }

}
