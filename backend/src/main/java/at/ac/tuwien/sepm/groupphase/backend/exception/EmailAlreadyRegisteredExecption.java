package at.ac.tuwien.sepm.groupphase.backend.exception;

public class EmailAlreadyRegisteredExecption extends RuntimeException {

    public EmailAlreadyRegisteredExecption(String message, Throwable cause) {
        super(message, cause);
    }
}
