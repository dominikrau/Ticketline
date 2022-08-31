package at.ac.tuwien.sepm.groupphase.backend.unittests;

import at.ac.tuwien.sepm.groupphase.backend.basetest.TestData;
import at.ac.tuwien.sepm.groupphase.backend.domain.model.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.AddressDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ApplicationUserDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ApplicationUserProfileDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.AddressMapper;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.UserMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.List;

import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.TEST_USER_EMAIL;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.mockito.Mockito.mock;

class UserMapperTest {

    private UserMapper userMapper;
    private PasswordEncoder encoder;

    @BeforeEach
    void setUp() {
        AddressMapper addressMapper = mock(AddressMapper.class);
        encoder = new BCryptPasswordEncoder();
        userMapper = new UserMapper(
            encoder,
            addressMapper
        );
    }

    @Test
    void toDomainUser() {
        List<String> roles = List.of(
            "ROLE_TESTER",
            "ROLE_BASIC",
            "ROLE_ADMIN"
        );
        ApplicationUserDto userDto = buildUserDto().toBuilder()
            .roles(roles)
            .build();
        ApplicationUser mapped = userMapper.toDomainUser(userDto);
        assertAll(
            () -> userEquals(userDto, mapped),
            () -> assertEquals(3, mapped.getRoles().size()),
            () -> assertTrue(mapped.getRoles().containsAll(roles))
        );
    }

    @Test
    void toDto() {
        ApplicationUser user = TestData.buildApplicationUserWith(TEST_USER_EMAIL);
        ApplicationUserDto mapped = userMapper.toDto(user);
        assertAll(
            () -> assertEquals(user.getUserId(), mapped.getUserId()),
            () -> assertEquals(user.getFirstName(), mapped.getFirstName()),
            () -> assertEquals(user.getLastName(), mapped.getLastName()),
            () -> assertEquals(user.getEmailAddress(), mapped.getEmailAddress()),
            () -> assertEquals(user.getGender().getCode(), mapped.getGender()),
            () -> assertEquals(user.getDateOfBirth(), mapped.getDateOfBirth()),
            () -> assertIterableEquals(user.getRoles(), mapped.getRoles())
        );
    }

    @Test
    public void givenNothing_whenValidUserProfileToDomainUser_thenValidApplicationUser() {
        ApplicationUserProfileDto user = buildUserProfileDto();
        ApplicationUser mapped = userMapper.userProfileToDomainUser(user);
        assertAll(
            () -> assertEquals(user.getUserId(), mapped.getUserId()),
            () -> assertEquals(user.getFirstName(), mapped.getFirstName()),
            () -> assertEquals(user.getLastName(), mapped.getLastName()),
            () -> assertEquals(user.getEmailAddress(), mapped.getEmailAddress()),
            () -> assertEquals(user.getGender(), mapped.getGender().getCode()),
            () -> assertEquals(user.getDateOfBirth(), mapped.getDateOfBirth())
        );
    }

    void userEquals(ApplicationUserDto dto, ApplicationUser domain) {
        assertAll(
            () -> assertEquals(dto.getUserId(), domain.getUserId()),
            () -> assertEquals(dto.getFirstName(), domain.getFirstName()),
            () -> assertEquals(dto.getLastName(), domain.getLastName()),
            () -> assertEquals(dto.getEmailAddress(), domain.getEmailAddress()),
            () -> assertEquals(dto.getGender(), domain.getGender().getCode()),
            () -> assertEquals(dto.getDateOfBirth().toString(), domain.getDateOfBirth().toString()),
            () -> assertTrue(encoder.matches(dto.getPassword(), domain.getPassword()))
        );
    }

    ApplicationUserDto buildUserDto() {
        return ApplicationUserDto.builder()
            .userId(123L)
            .firstName("Testus")
            .lastName("Maximus")
            .gender("M")
            .dateOfBirth(LocalDate.parse("2020-01-01"))
            .emailAddress(TEST_USER_EMAIL)
            .password("password")
            .address(AddressDto.builder()
                .country("Austria")
                .city("Vienna")
                .street("Karlsplatz 3")
                .postalCode("1030")
                .build())
            .build();
    }

    ApplicationUserProfileDto buildUserProfileDto() {
        return ApplicationUserProfileDto.builder()
            .firstName("Testus")
            .lastName("Maximus")
            .gender("M")
            .dateOfBirth(LocalDate.parse("2020-01-01"))
            .emailAddress(TEST_USER_EMAIL)
            .address(AddressDto.builder()
                .country("Austria")
                .city("Vienna")
                .street("Karlsplatz 3")
                .postalCode("1030")
                .build())
            .build();
    }
}