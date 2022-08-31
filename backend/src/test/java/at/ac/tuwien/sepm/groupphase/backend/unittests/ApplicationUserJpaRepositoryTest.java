package at.ac.tuwien.sepm.groupphase.backend.unittests;

import at.ac.tuwien.sepm.groupphase.backend.domain.model.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.exception.EmailAlreadyRegisteredExecption;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.buildApplicationUserWith;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class ApplicationUserJpaRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void findUserByEmail() {

        String emailOne = "test@test.at";
        String emailTwo = "numbertwo@email.com";
        ApplicationUser userOne = userRepository.saveUser(buildApplicationUserWith(emailOne));
        ApplicationUser userTwo = userRepository.saveUser(buildApplicationUserWith(emailTwo));

        ApplicationUser found = userRepository.findUserByEmail(emailOne);
        assertEquals(userOne.getUserId(), found.getUserId());
        found = userRepository.findUserByEmail(emailTwo);
        assertEquals(userTwo.getUserId(), found.getUserId());
    }

    @Test
    void findUserByEmailNoneFound() {
        assertEquals(0, userRepository.numberOfUsers());
        assertThrows(NotFoundException.class, () -> userRepository.findUserByEmail("test@email.com"));
    }
    @Test
    void saveUser() {
        ApplicationUser user = buildApplicationUserWith("testus@maximus.at");
        ApplicationUser saved = userRepository.saveUser(user);
        assertAll(
            () -> assertEquals(1L, userRepository.numberOfUsers()),
            () -> assertEqualsWithoutId(user, saved)
        );

        ApplicationUser inDb = userRepository.findUserByEmail("testus@maximus.at");
        assertEquals(saved.getUserId(), inDb.getUserId());
    }

    @Test
    void saveUserEmailAlreadyRegistered() {
        ApplicationUser user = buildApplicationUserWith("test@test.at");
        userRepository.saveUser(user);
        assertThrows(EmailAlreadyRegisteredExecption.class, () -> userRepository.saveUser(user));
    }

    @Test
    void numberOfUsers() {
        assertEquals(0, userRepository.numberOfUsers());
        userRepository.saveUser(buildApplicationUserWith("test@email.com"));
        assertEquals(1, userRepository.numberOfUsers());
    }

    @Test
    void givenNothing_whenUpdateValidApplicationUser_thenValidApplicationUser() {
        ApplicationUser user = buildApplicationUserWith("test@test2.at");
        ApplicationUser saved = userRepository.saveUser(user);
        ApplicationUser updated = saved.toBuilder().lastName(saved.getLastName() + "Updated").build();
        updated = userRepository.updateUser(updated);
        assertEquals(updated.getUserId(), userRepository.findUserByEmail(updated.getEmailAddress()).getUserId());
    }

    private void assertEqualsWithoutId(ApplicationUser expected, ApplicationUser actual) {
        assertAll(
            () -> assertEquals(expected.getFirstName(), actual.getFirstName()),
            () -> assertEquals(expected.getLastName(), actual.getLastName()),
            () -> assertEquals(expected.getEmailAddress(), actual.getEmailAddress()),
            () -> assertEquals(expected.getPassword(), actual.getPassword()),
            () -> assertEquals(expected.getDateOfBirth(), actual.getDateOfBirth()),
            () -> assertEquals(expected.getGender(), actual.getGender()),
            () -> assertEquals(expected.getRoles(), actual.getRoles()),
            () -> assertEquals(expected.isBlocked(), actual.isBlocked()),
            () -> assertEquals(expected.getAddress().getCountry(), actual.getAddress().getCountry()),
            () -> assertEquals(expected.getAddress().getCity(), actual.getAddress().getCity()),
            () -> assertEquals(expected.getAddress().getPostalCode(), actual.getAddress().getPostalCode()),
            () -> assertEquals(expected.getAddress().getStreet(), actual.getAddress().getStreet()),
            () -> assertEquals(expected.getAddress().getAdditional(), actual.getAddress().getAdditional())
        );
    }
}