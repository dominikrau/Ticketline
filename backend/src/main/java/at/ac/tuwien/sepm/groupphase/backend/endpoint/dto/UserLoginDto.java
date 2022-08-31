package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Value
@Builder(builderClassName = "Builder", toBuilder = true)
@JsonDeserialize(builder = UserLoginDto.Builder.class)
public class UserLoginDto {

    @NotNull(message = "Email must not be null")
    @Email
    String email;

    @NotNull(message = "Password must not be null")
    String password;

    @JsonPOJOBuilder(withPrefix = "")
    public static final class Builder {
    }
}

