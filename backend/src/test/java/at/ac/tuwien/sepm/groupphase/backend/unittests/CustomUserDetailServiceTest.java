package at.ac.tuwien.sepm.groupphase.backend.unittests;

import at.ac.tuwien.sepm.groupphase.backend.basetest.TestData;
import at.ac.tuwien.sepm.groupphase.backend.domain.model.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.repository.MessageRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.OrderRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.TicketRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.UserRepository;
import at.ac.tuwien.sepm.groupphase.backend.security.JwtTokenizer;
import at.ac.tuwien.sepm.groupphase.backend.service.UserService;
import at.ac.tuwien.sepm.groupphase.backend.service.impl.CustomUserDetailService;
import at.ac.tuwien.sepm.groupphase.backend.util.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CustomUserDetailServiceTest {

    private PasswordEncoder passwordEncoder;

    private UserService userService;
    private UserRepository repo;
    private TicketRepository ticketRepository;
    private MessageRepository messageRepository;
    private OrderRepository orderRepository;

    @BeforeEach
    void setUp() {
        passwordEncoder = mock(PasswordEncoder.class);
        repo = mock(UserRepository.class);
        ticketRepository = mock(TicketRepository.class);
        messageRepository = mock(MessageRepository.class);
        orderRepository = mock(OrderRepository.class);
        JwtTokenizer tokenizer = mock(JwtTokenizer.class);
        Validator validator = mock(Validator.class);
        //userService = new CustomUserDetailService(passwordEncoder, validator,ticketRepository, repo, tokenizer);
        userService = new CustomUserDetailService(passwordEncoder, validator,ticketRepository, repo, messageRepository, orderRepository, tokenizer);
    }

    @Test
    void loadUserByUsername() {
        ApplicationUser user = TestData.buildApplicationUserWith("test@test.at");
        when(repo.findUserByEmail(anyString())).thenReturn(user);
        UserDetails userDetails = userService.loadUserByUsername("whatever");
        List<String> roles = userDetails.getAuthorities().stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.toList());
        assertAll(
            () -> assertEquals(user.getEmailAddress(), userDetails.getUsername()),
            () -> assertEquals(user.getPassword(), userDetails.getPassword()),
            () -> assertEquals(user.getRoles(), roles),
            () -> assertEquals(user.isBlocked(), !userDetails.isAccountNonLocked())
        );

    }

    @Test
    void loadUserByUsernameNoneFound() {
        when(repo.findUserByEmail(anyString())).thenThrow(new NotFoundException());
        assertThrows(
            UsernameNotFoundException.class,
            () -> userService.loadUserByUsername("whatever")
        );
    }

    @Test
    void createUser() {
        when(repo.saveUser(any(ApplicationUser.class))).thenAnswer(i -> i.getArguments()[0]);
        ApplicationUser user = TestData.buildApplicationUserWith("test@test.at");
        ApplicationUser created = userService.createNewUser(user);
        assertEquals(user, created);
    }
}