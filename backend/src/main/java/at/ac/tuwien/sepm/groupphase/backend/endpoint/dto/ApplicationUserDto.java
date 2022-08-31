package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Singular;
import lombok.Value;

import javax.validation.Valid;
import javax.validation.constraints.*;
import java.time.LocalDate;
import java.util.List;

@Value
@JsonDeserialize(builder = ApplicationUserDto.Builder.class)
@Builder(builderClassName = "Builder", toBuilder = true)
public class ApplicationUserDto {

    Long userId;
    @NotBlank
    @Size(max = 255)
    String firstName;
    @NotBlank
    @Size(max = 255)
    String lastName;
    @NotBlank
    @Email
    @Size(max = 255)
    String emailAddress;
    @NotBlank
    @Size(max = 255, min = 8)
    @Pattern(regexp = ".*([a-zA-Z][0-9]|[0-9][a-zA-Z]).*", message = "Your password must contain at least one letter and one number")
    String password;
    @NotNull
    LocalDate dateOfBirth;
    @NotBlank
    String gender;
    @Valid
    @NotNull
    AddressDto address;
    @Singular
    List<String> roles;
    @NotNull
    boolean blocked;

    @JsonPOJOBuilder(withPrefix = "")
    public static final class Builder {
    }
}
