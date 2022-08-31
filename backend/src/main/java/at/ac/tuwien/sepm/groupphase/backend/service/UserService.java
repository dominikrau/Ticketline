package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.domain.model.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.domain.model.search.UserSearch;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UpdatePasswordDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;

public interface UserService extends UserDetailsService {

    /**
     * Find a user in the context of Spring Security based on the email address
     * <p>
     * For more information have a look at this tutorial:
     * https://www.baeldung.com/spring-security-authentication-with-a-database
     *
     * @param email the email address
     * @return a Spring Security user
     * @throws UsernameNotFoundException is thrown if the specified user does not exists
     */
    @Override
    UserDetails loadUserByUsername(String email);

    /**
     * Saves a new User and provides the authentication token
     * @param newUser to be saved
     * @return the new authentication token
     */
    String registerNewUser(ApplicationUser newUser);

    /**
     * Saves a new User and provides the new User object with its' new attributes
     * @param newUser to be saved
     * @return the saved User
     */
    ApplicationUser createNewUser(ApplicationUser newUser);

    /**
     * gets the current logged in user
     *
     * @return currently signed in user
     */
    ApplicationUser getCurrentUser();

    /**
     * Edits the current User Profile
     * @param userProfile containing the attributes to be updated
     * @return the updated authentication token
     */
    String editUser(ApplicationUser userProfile);

    /**
     * updates a user password
     * @param passwordDto the password combination to update
     * @return the new authentication token
     */
    String updatePassword(UpdatePasswordDto passwordDto);

    /**
     * Deletes the current User
     * @return the updated authentication token
     */
    ApplicationUser deleteUser();
    /**
     * Find all ApplicationUser entries.
     *
     * @return list of all ApplicationUser entries
     */
    List<ApplicationUser> findAll();

    /**
     * Find all ApplicationUser entries.
     * @param userSearch parameter for search
     * @param pageRequest contains details for paging
     * @return list of found ApplicationUser
     */
    Page<ApplicationUser> searchUsers(UserSearch userSearch, Pageable pageRequest);

    /**
     * Find ApplicationUser by id.
     * @param id of the user
     * @return found ApplicationUser
     */
    ApplicationUser getUserById(Long id);

    /**
     * Block ApplicationUser by id.
     * @param id of the user
     * @return blocked ApplicationUser
     */
    ApplicationUser blockUser(Long id);

    /**
     * Unblock ApplicationUser by id.
     * @param id of the user
     * @return unblocked ApplicationUser
     */
    ApplicationUser unblockUser(Long id);

    /**
     * Reset password from ApplicationUser by id.
     * @param passwordDto the password combination to update
     * @return updated User
     */
    ApplicationUser updatePasswordById(UpdatePasswordDto passwordDto, Long id);

    /**
     * Change role of ApplicationUser specified by id to admin.
     * @param id of the user
     * @return unblocked ApplicationUser
     */
    ApplicationUser makeUserAdmin(Long id);

    /**
     * Find ApplicationUser by email.
     * @param email of the user
     * @return found ApplicationUser
     */
    ApplicationUser getUserByEmail(String email);

    /**
     * Update the ApplicationUser.
     * @param user the user to update
     * @return updated user
     */
    ApplicationUser updateUser(ApplicationUser user);
}
