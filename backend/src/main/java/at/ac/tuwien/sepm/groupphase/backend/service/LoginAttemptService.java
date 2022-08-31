package at.ac.tuwien.sepm.groupphase.backend.service;

public interface LoginAttemptService {

    /**
     * Searches user with email and sets loginAttempts to 0.
     */
    void loginSucceeded(String email);

    /**
     * Searches user with email and increments loginAttempts by 1.
     * If loginAttempts > MAX_ATTEMPTS sets user.blocked true.
     */
    void loginFailed(String email);

}
