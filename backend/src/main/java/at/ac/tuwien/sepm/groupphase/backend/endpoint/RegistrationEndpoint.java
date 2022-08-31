package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.domain.model.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ApplicationUserDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.UserMapper;
import at.ac.tuwien.sepm.groupphase.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("api/v1/registration")
@RequiredArgsConstructor
public class RegistrationEndpoint {

    private final UserService userService;
    private final UserMapper userMapper;


    /**
     * Accepts the POST-Request and proceeds to create a new User entry
     *
     * @param user DTO of the User to be created
     * @return the User DTO of the newly created User (including ID and createdAt)
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @CrossOrigin(origins = "http://localhost:4200")
    public String register(@RequestBody @Valid ApplicationUserDto user) {
        log.info("POST /api/v1/registration body: {}", user);
        ApplicationUser basicUser = userMapper.toDomainUser(user);

        return userService.registerNewUser(basicUser.toBuilder().role("ROLE_USER").build());
    }

}
