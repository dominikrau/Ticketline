package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.domain.model.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.service.LoginAttemptService;
import at.ac.tuwien.sepm.groupphase.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.stereotype.Service;


@Slf4j
@Service
@RequiredArgsConstructor
public class SimpleLoginAttemptService implements LoginAttemptService {
        private static final int MAX_ATTEMPT = 4;
        private final UserService userService;

        public void loginSucceeded(String email) {
            log.debug("Login succeed");
            ApplicationUser user = userService.getUserByEmail(email)
                .toBuilder()
                .loginAttempts(0)
                .build();
            userService.updateUser(user);
        }

        public void loginFailed(String email) {
            log.debug("Login failed");
            try {
            ApplicationUser user = userService.getUserByEmail(email);
            ApplicationUser updatedUser = user.toBuilder()
                .loginAttempts(user.getLoginAttempts() + 1)
                .build();
            userService.updateUser(updatedUser);
                if (user.getLoginAttempts() >= MAX_ATTEMPT) {
                    log.debug("Maximum login attempts, user now blocked");
                    userService.blockUser(user.getUserId());
                }
            } catch (NotFoundException e) {
                log.error("Bad Credentials", e);
                throw new BadCredentialsException("Bad Credentials", e);
            }
        }
}
