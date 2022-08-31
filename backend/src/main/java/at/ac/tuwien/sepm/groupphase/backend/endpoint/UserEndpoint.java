package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.domain.model.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ApplicationUserDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ApplicationUserProfileDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.CustomPage;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UpdatePasswordDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.UserMapper;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.UserSearchMapper;
import at.ac.tuwien.sepm.groupphase.backend.service.UserService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("api/v1/users")
@RequiredArgsConstructor
@Slf4j
public class UserEndpoint {

    private final UserService userService;
    private final UserMapper userMapper;


    /**
     * Accepts the POST-Request and proceeds to create a new User entry
     *
     * @param user of the User to be created
     * @return the User DTO of the newly created User (including ID and createdAt)
     */
    @Secured("ROLE_ADMIN")
    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public ApplicationUserDto addNewUser(@RequestBody @Valid ApplicationUserDto user) {
        log.info("POST /api/v1/users body: {}", user);
        ApplicationUser registeredUser = userService.createNewUser(
            userMapper.toDomainUser(user)
        );
        return userMapper.toDto(registeredUser);
    }

    /**
     * Accepts the GET-Request and proceeds to retrieve the currently logged in User
     *
     * @return the User DTO of the currently logged in User
     */
    @GetMapping(value = "/profile")
    @ApiOperation(value = "Get detailed information about the current user",
        authorizations = {@Authorization(value = "apiKey")})
    public ApplicationUserDto getCurrentUser() {
        return userMapper.toDto(userService.getCurrentUser());
    }

    /**
     * Accepts the GET-Request and proceeds to search for the provided attributes in the saved User entries
     *
     * @param page number of the Page to be retrieved
     * @param size of the Page to be retrieved
     * @param firstName of an User to be retrieved
     * @param lastName of an User to be retrieved
     * @param blocked of an User to be retrieved
     * @return A Page of User DTOs whose properties match the attributes
     */
    @GetMapping(value = "/search")
    @ApiOperation(value = "Search for users by first name, last name and blocked-status",
        authorizations = {@Authorization(value = "apiKey")})
    public CustomPage<ApplicationUserDto> getAllUsers(
        @RequestParam("page") int page,
        @RequestParam("size") int size,
        @RequestParam("firstName") String firstName,
        @RequestParam("lastName") String lastName,
        @RequestParam("blocked") boolean blocked
    ) {
        log.info("GET /api/v1/users/search");
        return CustomPage.of(userService.searchUsers(UserSearchMapper.toUserSearch(
            firstName,
            lastName,
            blocked
        ), PageRequest.of(page, size))
            .map(userMapper::toDto));
    }

    /**
     * Accepts the GET-Request and proceeds to retrieve all User entries in the database
     *
     * @return A List of User DTOs containing all saved Users
     */
    @Secured("ROLE_ADMIN")
    @GetMapping(value = "")
    @ApiOperation(value = "Get detailed information about all users",
        authorizations = {@Authorization(value = "apiKey")})
    public List<ApplicationUserDto> getAllUsers() {
        log.info("GET /api/v1/users");
        return userMapper.toDto(userService.findAll());
    }


    /**
     * Accepts the PUT-Request and proceeds to update a new User entry
     *
     * @param user to be updated (determined by contained id) with attributes to be updated
     * @return the authorization token
     */
    @PutMapping(value = "/profile/edit")
    @ApiOperation(value = "Edit the current user",
        authorizations = {@Authorization(value = "apiKey")})
    public String updateUser(@RequestBody @Valid ApplicationUserProfileDto user) {
        log.info("Put /api/v1/users/profile/edit");
        return userService.editUser(
            userMapper.userProfileToDomainUser(user)
        );
    }

    /**
     * Accepts the GET-Request and proceeds to retrieve a single User entry
     *
     * @param id of the User to be retrieved
     * @return the User DTO of the specified User
     */
    @Secured("ROLE_ADMIN")
    @GetMapping("/{id}")
    @ApiOperation(value = "Get user by id",
        authorizations = {@Authorization(value = "apiKey")})
    public ApplicationUserDto getUserById(@PathVariable("id") Long id) {
        log.info("GET /api/v1/events/{}", id);
        return userMapper.toDto(userService.getUserById(id));
    }

    /**
     * Accepts the PUT-Request and proceeds to update the provided User as Blocked
     *
     * @param id of the User to be blocked
     * @return the updated User DTO of the specified User that was blocked
     */
    @Secured("ROLE_ADMIN")
    @PutMapping(value = "/{id}/block")
    @ApiOperation(value = "Block user by id",
        authorizations = {@Authorization(value = "apiKey")})
    public ApplicationUserDto blockUser(@PathVariable("id") Long id) {
        log.info("Put /api/v1/users/{}/block", id);
        return userMapper.toDto(userService.blockUser(id));
    }

    /**
     * Accepts the PUT-Request and proceeds to update the provided User as Not Blocked
     *
     * @param id of the User to be unblocked
     * @return the updated User DTO of the specified User that was unblocked
     */
    @Secured("ROLE_ADMIN")
    @PutMapping(value = "/{id}/unblock")
    @ApiOperation(value = "Unblock user by id",
        authorizations = {@Authorization(value = "apiKey")})
    public ApplicationUserDto unblockUser(@PathVariable("id") Long id) {
        log.info("Put /api/v1/users/{}/unblock", id);
        return userMapper.toDto(userService.unblockUser(id));
    }

    /**
     * Accepts the PUT-Request and proceeds to update the current User's password
     *
     * @param passwordDto with the new password
     * @return the authorization token
     */
    @PutMapping(value = "/password/update")
    @ApiOperation(value = "Update the current user password",
        authorizations = {@Authorization(value = "apiKey")})
    public String updatePassword(@RequestBody @Valid UpdatePasswordDto passwordDto) {
        log.info("Put /api/v1/users/password/update body: {}", passwordDto);
        return userService.updatePassword(passwordDto);
    }

    /**
     * Accepts the DELETE-Request and proceeds to try and delete the currently logged in User.
     *
     * @return the User DTO of the deleted User
     */
    @DeleteMapping(value = "/profile/delete")
    @ApiOperation(value = "delete the current user",
        authorizations = {@Authorization(value = "apiKey")})
    public ApplicationUserDto deleteUser() {
        log.info("Delete /api/v1/users/profile/delete");

        return userMapper.toDto(userService.deleteUser());
    }

    /**
     * Accepts the PUT-Request and proceeds to update the specified User's password
     *
     * @param id of the User whose password shall be updated
     * @param passwordDto with the new password
     * @return the User DTO with the updated User
     */
    @Secured("ROLE_ADMIN")
    @PutMapping(value = "/{id}/reset")
    @ApiOperation(value = "Update password of user by id",
        authorizations = {@Authorization(value = "apiKey")})
    public ApplicationUserDto updatePasswordById(@PathVariable("id") Long id, @RequestBody @Valid UpdatePasswordDto passwordDto) {
        log.info("Put /api/v1/users/{}/reset", id);
        return userMapper.toDto(userService.updatePasswordById(passwordDto, id));
    }

    /**
     * Accepts the PUT-Request and proceeds to update the specified User's Role to Admin
     *
     * @param id of the User who shall be updated to Admin
     * @return the User DTO with the updated Admin User
     */
    @Secured("ROLE_ADMIN")
    @PutMapping(value = "/{id}/toadmin")
    @ApiOperation(value = "Update role of user by id to admin",
        authorizations = {@Authorization(value = "apiKey")})
    public ApplicationUserDto makeUserAdmin(@PathVariable("id") Long id) {
        log.info("Put /api/v1/users/{}/toadmin", id);
        return userMapper.toDto(userService.makeUserAdmin(id));
    }

}
