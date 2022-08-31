package at.ac.tuwien.sepm.groupphase.backend.security;

import at.ac.tuwien.sepm.groupphase.backend.service.LoginAttemptService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AuthenticationSuccessEventListener
    implements ApplicationListener<AuthenticationSuccessEvent>
{

    @Autowired
    private LoginAttemptService loginAttemptService;

    /**
     * Resets user.loginAttempts to 0 after successful login from user.
     **/
    public void onApplicationEvent(AuthenticationSuccessEvent e) {
        UserDetails userDetails = (UserDetails) e.getAuthentication().getPrincipal();
        log.debug("Authentication Success: User Email : {}", userDetails.getUsername());

        loginAttemptService.loginSucceeded(userDetails.getUsername());
    }
}
