package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Value
@Builder(toBuilder = true)
public class ApplicationUserProfileDto {
    Long userId;
    @NotBlank
    String firstName;
    @NotBlank
    String lastName;
    @NotBlank
    @Email
    String emailAddress;
    @NotNull
    LocalDate dateOfBirth;
    @NotBlank
    String gender;
    @NotNull
    AddressDto address;
//    @JsonPOJOBuilder(withPrefix = "")

    @JsonCreator
    public ApplicationUserProfileDto(
        @JsonProperty("userId") Long userId,
        @NotBlank @JsonProperty("firstName") String firstName,
        @NotBlank @JsonProperty("lastName") String lastName,
        @NotBlank @Email @JsonProperty("emailAddress") String emailAddress,
        @NotNull @JsonProperty("dateOfBirth") LocalDate dateOfBirth,
        @NotBlank @JsonProperty("gender") String gender,
        @NotNull @JsonProperty("address") AddressDto address
    ) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.emailAddress = emailAddress;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.address = address;
    }
}
