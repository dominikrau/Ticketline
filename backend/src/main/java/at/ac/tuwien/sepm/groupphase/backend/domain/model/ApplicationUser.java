package at.ac.tuwien.sepm.groupphase.backend.domain.model;

import lombok.Builder;
import lombok.Singular;
import lombok.Value;

import java.time.LocalDate;
import java.util.List;

@Value
@Builder(builderClassName = "Builder", toBuilder = true)
public class ApplicationUser {

    Long userId;
    String firstName;
    String lastName;
    String emailAddress;
    String password;
    LocalDate dateOfBirth;
    Gender gender;
    Address address;
    @Singular
    List<String> roles;
    boolean blocked;
    int loginAttempts;

}

