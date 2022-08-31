package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.domain.model.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.exception.EmailAlreadyRegisteredExecption;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.repository.mapping.UserEntityMapper;
import io.vavr.control.Try;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static io.vavr.API.$;
import static io.vavr.API.Case;
import static io.vavr.Predicates.instanceOf;
import static java.lang.String.format;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserRepository {

    private final UserJpaRepository repository;
    private final UserEntityMapper mapper;

    /**
     * Get User by email.
     * @param email of the user
     * @return application user with specified email
     */
    public ApplicationUser findUserByEmail(String email) {
        return repository.findByEmailAddress(email)
            .map(mapper::toDomain)
            .orElseThrow(() -> new NotFoundException(
                format("No User with given Email \"%s\" address found", email)
            ));
    }

    /**
     * Saves a new User in the database
     * @param user to be saved
     * @return the saved User
     */
    @SneakyThrows
    @SuppressWarnings("unchecked")
    public ApplicationUser saveUser(ApplicationUser user) {
        return Try.of(() -> repository.save(mapper.toEntity(user)))
            .map(mapper::toDomain)
            .mapFailure(
                Case($(instanceOf(DataIntegrityViolationException.class)), e -> new EmailAlreadyRegisteredExecption(
                    format("User with email %s already registered", user.getEmailAddress()), e
                ))
            )
            .getOrElseThrow(Function.identity());
    }

    /**
     * Get the amount of Users in the database.
     *
     * @return total number of Users
     */
    public long numberOfUsers() {
        return repository.count();
    }


    /**
     * Edits the current User Profile
     * @param user containing the attributes to be updated
     * @return the updated User
     */
    public ApplicationUser updateUser(ApplicationUser user) {
        try {
            return mapper.toDomain(repository.save(mapper.toEntity(user)));
        } catch (DataIntegrityViolationException e) {
            log.warn("This email is already registered");
            throw new EmailAlreadyRegisteredExecption("This email is already registered", e);
        }

    }

    /**
     * Find all ApplicationUser entries.
     *
     * @return list of all users
     */
    public List<ApplicationUser> findAllUsers() {
        return repository.findAll().
            stream().map(mapper::toDomain).collect(Collectors.toList());
    }

    /**
     * Search for a specific Page of ApplicationUsers with parameters.
     * @param firstName of the user
     * @param lastName of the user
     * @param blocked if user is blocked
     * @param page contains page information (page number + size)
     * @return Page of found users
     */
    public Page<ApplicationUser> searchUser(String firstName, String lastName, boolean blocked, final Pageable page) {
        return repository.findAllByFirstNameContainingIgnoreCaseAndLastNameContainingIgnoreCaseAndBlocked(firstName, lastName, blocked, page)
            .map(mapper::toDomain);
    }

    /**
     * Get ApplicationUser by id.
     * @param id of the user
     * @return application user
     */
    public ApplicationUser getUserById(Long id) {
        return repository.findById(id)
            .map(mapper::toDomain)
            .orElseThrow(() -> new NotFoundException(
                format("No User with given id \"%s\" found", id)
            ));
    }

    /**
     * Deletes the specified User
     *
     * @param user to be deleted
     */
    public void deleteUser(ApplicationUser user) {
        log.debug("User with id {} is to be deleted", user.getUserId());
        repository.deleteAllById(user.getUserId());
    }
}
