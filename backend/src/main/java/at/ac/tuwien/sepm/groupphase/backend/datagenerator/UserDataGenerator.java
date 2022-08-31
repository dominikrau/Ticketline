package at.ac.tuwien.sepm.groupphase.backend.datagenerator;

import at.ac.tuwien.sepm.groupphase.backend.domain.model.Address;
import at.ac.tuwien.sepm.groupphase.backend.domain.model.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.domain.model.Gender;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Slf4j
@Profile("generateData")
@Component
@RequiredArgsConstructor
public class UserDataGenerator {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public void generateAdminUser() {
        if (userRepository.numberOfUsers() > 0) {
            log.debug("user already generated");
        } else {
            userRepository.saveUser(ApplicationUser.builder()
                .firstName("Adminus")
                .lastName("Userus")
                .emailAddress("admin@email.com")
                .password(passwordEncoder.encode("password1"))
                .dateOfBirth(LocalDate.of(1999, 1, 1))
                .gender(Gender.OTHER)
                .role("ROLE_ADMIN")
                .role("ROLE_USER")
                .address(Address.builder()
                    .country("Austria")
                    .postalCode("1040")
                    .city("Vienna")
                    .street("Karlsplatz 13")
                    .build())
                .blocked(false)
                .build());
        }
    }
}