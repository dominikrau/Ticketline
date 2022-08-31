package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.domain.model.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.domain.model.Ticket;
import at.ac.tuwien.sepm.groupphase.backend.domain.model.TicketStatus;
import at.ac.tuwien.sepm.groupphase.backend.domain.model.search.UserSearch;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.UpdatePasswordDto;
import at.ac.tuwien.sepm.groupphase.backend.exception.ValidationException;
import at.ac.tuwien.sepm.groupphase.backend.repository.MessageRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.OrderRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.TicketRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
import at.ac.tuwien.sepm.groupphase.backend.security.JwtTokenizer;
import at.ac.tuwien.sepm.groupphase.backend.service.UserService;
import at.ac.tuwien.sepm.groupphase.backend.util.Validator;
import io.vavr.control.Try;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserService {


    private final PasswordEncoder passwordEncoder;

    private final Validator validator;

    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;
    private final MessageRepository messageRepository;
    private final OrderRepository orderRepository;
    private final JwtTokenizer tokenizer;

    @Override
    public UserDetails loadUserByUsername(String email) {
        log.debug("Load all user by email");
        return Try.of(() -> userRepository.findUserByEmail(email))
            .map(user -> new User(
                user.getEmailAddress(),
                user.getPassword(),
                true,
                true,
                true,
                !user.isBlocked(),
                AuthorityUtils.createAuthorityList(user.getRoles().toArray(new String[]{}))
            ))
            .getOrElseThrow(e -> new UsernameNotFoundException(e.getMessage(), e));
    }

    @Override
    public String registerNewUser(ApplicationUser newUser) {
        log.debug("Register new user {}", newUser);
        ApplicationUser user = newUser.toBuilder()
            .loginAttempts(0)
            .build();
        validator.validateNewUser(newUser);
        ApplicationUser savedUser = userRepository.saveUser(user);
        return tokenizer.getAuthToken(savedUser.getEmailAddress(), savedUser.getRoles());
    }

    @Override
    public ApplicationUser createNewUser(ApplicationUser newUser) {
        log.debug("Create new user {}", newUser);
        ApplicationUser user = newUser.toBuilder()
            .loginAttempts(0)
            .build();
        validator.validateNewUser(user);
        return userRepository.saveUser(newUser);
    }

    @Override
    public ApplicationUser getCurrentUser() {
        log.debug("load current user");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            UserDetails user = loadUserByUsername(authentication.getName());
            return userRepository.findUserByEmail(user.getUsername());
        }
        return null;
    }

    @Override
    public String editUser(ApplicationUser user) {
        log.debug("Edit user {}", user);
        validator.validateNewUser(user);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            UserDetails authenticatedUser = loadUserByUsername(authentication.getName());

            ApplicationUser current = userRepository.findUserByEmail(authenticatedUser.getUsername());

            ApplicationUser updated = current.toBuilder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .emailAddress(user.getEmailAddress())
                .dateOfBirth(user.getDateOfBirth())
                .gender(user.getGender())
                .address(user.getAddress())
                .build();

            ApplicationUser saved = userRepository.updateUser(updated);
            return tokenizer.getAuthToken(saved.getEmailAddress(), saved.getRoles());
        }
        return null;
    }

    @Override
    public String updatePassword(UpdatePasswordDto passwordDto) {
        log.debug("update password {}", passwordDto);
        ApplicationUser currentUser = getCurrentUser();
        if (passwordEncoder.matches(passwordDto.getCurrentPassword(), currentUser.getPassword())) {
            log.debug("Current password is correct!");
            String newPassword = passwordEncoder.encode(passwordDto.getNewPassword());
            ApplicationUser updatedUser = userRepository.saveUser(currentUser.toBuilder().password(newPassword).build());
            return tokenizer.getAuthToken(updatedUser.getEmailAddress(), updatedUser.getRoles());
        } else {
            log.error("Current password is incorrect!");
            throw new BadCredentialsException("Invalid Password!");
        }
    }

    @Override
    public List<ApplicationUser> findAll() {
        log.debug("Find all users");
        return userRepository.findAllUsers();
    }

    @Override
    public Page<ApplicationUser> searchUsers(UserSearch search, Pageable pageable) {
        log.debug("Search users with" + search.toString());
        return userRepository.searchUser(search.getFirstName(), search.getLastName(), search.isBlocked(), pageable);
    }

    @Override
    public ApplicationUser getUserById(Long id) {
        log.debug("Get user by id {}", id);
        return userRepository.getUserById(id);
    }

    @Override
    public ApplicationUser blockUser(Long id) {
        log.debug("Block user with id {}", id);
        ApplicationUser user = getUserById(id)
            .toBuilder()
            .blocked(true)
            .build();
        return userRepository.updateUser(user);
    }

    @Override
    public ApplicationUser unblockUser(Long id) {
        log.debug("Unblock user with id {}", id);
        ApplicationUser user = getUserById(id).toBuilder()
            .blocked(false)
            .loginAttempts(0)
            .build();
        return userRepository.updateUser(user);
    }

    @Override
    public ApplicationUser updatePasswordById(UpdatePasswordDto passwordDto, Long id) {
        log.debug("Update password {}", passwordDto);
        ApplicationUser user = getUserById(id);
        String newPassword = passwordEncoder.encode(passwordDto.getNewPassword());
        return userRepository.saveUser(user.toBuilder().password(newPassword).build());
    }

    @Override
    public ApplicationUser makeUserAdmin(Long id) {
        log.debug("Make User to admin, id {}", id);
        ApplicationUser user = getUserById(id).toBuilder()
            .role("ROLE_ADMIN")
            .build();
        return userRepository.updateUser(user);
    }

    @Override
    public ApplicationUser getUserByEmail(String email) {
        log.debug("Load user by email");
        return userRepository.findUserByEmail(email);
    }

    @Override
    public ApplicationUser updateUser(ApplicationUser user) {
        return userRepository.updateUser(user);
    }


    @Override
    @Transactional
    public ApplicationUser deleteUser(){
        ApplicationUser currentUser = this.getCurrentUser();
        List<Ticket> tickets = ticketRepository.findTicketsOfUser(currentUser);
        log.debug("Tickets of User to be deleted: " + tickets);

        for (Ticket ticket:tickets) {
            if (ticket.getStatus() != TicketStatus.CANCELLED && ticket.getShow().getEndTime().isAfter(LocalDateTime.now())){
                log.error("User to be deleted still has tickets for for future shows. Aborting deletion.");
                throw new ValidationException("Can't delete User with tickets for shows in the future! Please cancel your tickets first!");
            }
        }
        for (Ticket ticket:tickets) ticketRepository.deleteTicket(ticket.getTicketId());
        orderRepository.deleteOrdersWithUser(currentUser);
        messageRepository.deleteUserFromReadTable(currentUser);
        userRepository.deleteUser(currentUser);
        return currentUser;
    }

}
