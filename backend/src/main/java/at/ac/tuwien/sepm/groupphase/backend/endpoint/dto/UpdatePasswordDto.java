package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Value
@Builder(toBuilder = true)
public class UpdatePasswordDto {
    @NotNull(message = "Current password must not be null")
    @Pattern(regexp = ".*([a-zA-Z][0-9]|[0-9][a-zA-Z]).*", message = "Your password must contain at least one letter and one number")
    String currentPassword;

    @NotNull(message = "New password must not be null")
    @Pattern(regexp = ".*([a-zA-Z][0-9]|[0-9][a-zA-Z]).*", message = "Your new password must contain at least one letter and one number")
    String newPassword;

    @JsonCreator
    public UpdatePasswordDto(
        @NotNull @JsonProperty("currentPassword") String currentPassword,
        @NotNull @JsonProperty("newPassword") String newPassword
    ) {
        this.currentPassword = currentPassword;
        this.newPassword = newPassword;
    }
}
