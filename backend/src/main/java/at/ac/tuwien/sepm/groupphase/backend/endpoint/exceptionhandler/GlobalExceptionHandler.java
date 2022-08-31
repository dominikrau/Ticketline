package at.ac.tuwien.sepm.groupphase.backend.endpoint.exceptionhandler;

import at.ac.tuwien.sepm.groupphase.backend.exception.EmailAlreadyRegisteredExecption;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.exception.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.nio.file.AccessDeniedException;
import java.time.Clock;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Register all your Java exceptions here to map them into meaningful HTTP exceptions
 * If you have special cases which are only important for specific endpoints, use ResponseStatusExceptions
 * https://www.baeldung.com/exception-handling-for-rest-with-spring#responsestatusexception
 */
@Slf4j
@ControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    private final Clock clock;

    /**
     * Use the @ExceptionHandler annotation to write handler for custom exceptions
     */
    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ErrorDto handle(final NotFoundException e) {
        log.error("A Value was not found. Message: {}", e.getMessage());
        return error(e.getMessage(), HttpStatus.NOT_FOUND.value());
    }

    @ExceptionHandler(EmailAlreadyRegisteredExecption.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ResponseBody
    public ErrorDto handle(final EmailAlreadyRegisteredExecption e) {
        log.error("An Email has already been registered. Message: {}", e.getMessage());
        return error(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY.value());
    }

    @ExceptionHandler(BadCredentialsException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ResponseBody
    public ErrorDto handle(final BadCredentialsException e) {
        log.error("Bad Credentials entered. Message: {}", e.getMessage());
        return error(e.getMessage(), HttpStatus.FORBIDDEN.value());
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ResponseBody
    public ErrorDto handle(final AccessDeniedException e) {
        log.error("Deletion denied: {}", e.getMessage());
        return error(e.getMessage(), HttpStatus.FORBIDDEN.value());
    }

    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ResponseBody
    public ErrorDto handle(final ValidationException e) {
        log.error("Validation exception occured. Message: {}", e.getMessage());
        return error(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY.value());
    }

    /**
     * Override methods from ResponseEntityExceptionHandler to send a customized HTTP response for a known exception
     * from e.g. Spring
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatus status, WebRequest request) {
        Map<String, Object> body = new LinkedHashMap<>();
        // Get all errors
        List<String> errors = ex.getBindingResult()
            .getFieldErrors()
            .stream()
            .map(err -> err.getField() + " " + err.getDefaultMessage())
            .collect(Collectors.toList());
        body.put("Validation errors", errors);

        return new ResponseEntity<>(body.toString(), headers, status);

    }

    private ErrorDto error(final List<String> messages, final int code) {
        return new ErrorDto(
            messages,
            code,
            clock.instant().toString()
        );
    }

    private ErrorDto error(final String message, final int code) {
        return error(List.of(message), code);
    }

}
