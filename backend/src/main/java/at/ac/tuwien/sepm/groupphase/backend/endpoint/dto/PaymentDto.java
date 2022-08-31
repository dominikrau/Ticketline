package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.NotNull;

@Value
@Builder(builderClassName = "Builder", toBuilder = true)
@JsonDeserialize(builder = PaymentDto.Builder.class)
public class PaymentDto {

    @NotNull
    String method;
    String name;
    String number;

    @JsonPOJOBuilder(withPrefix = "")
    public static final class Builder {
    }

}
