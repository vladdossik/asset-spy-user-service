package asset.spy.user.service.handler;

import asset.spy.auth.lib.exception.InvalidJwtException;
import asset.spy.auth.lib.exception.TokenExpiredException;
import asset.spy.auth.lib.exception.UnauthorizedException;
import asset.spy.user.service.dto.ValidationErrorDto;
import asset.spy.user.service.exception.ContactNotFoundException;
import asset.spy.user.service.exception.UserAlreadyExistsException;
import asset.spy.user.service.exception.UserNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Object> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        List<ValidationErrorDto> validationErrorDTO = ex.getBindingResult()
                .getFieldErrors().stream()
                .map(error -> new ValidationErrorDto(error.getField(),
                        error.getDefaultMessage(), error.getCode()))
                .collect(Collectors.toList());
        log.error("Validation errors: {}", validationErrorDTO, ex);
        return new ResponseEntity<>(validationErrorDTO, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({ContactNotFoundException.class, UserNotFoundException.class})
    public ResponseEntity<String> handleNotFoundException(RuntimeException ex) {
        log.error("Resource not found: {}", ex.getMessage(), ex);
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception ex) {
        log.error("Unexpected exception: {}", ex.getMessage(), ex);
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException ex) {
        log.error("Illegal argument: {}", ex.getMessage(), ex);
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<String> handleUserAlreadyExistsException(UserAlreadyExistsException ex) {
        log.error("User already exists: {}", ex.getMessage(), ex);
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<String> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        if (ex.getMessage().contains("unique constraint")) {
            log.error("Unique constraint violation: {}", ex.getMessage(), ex);
        } else {
            log.error("Data integrity violation: {}", ex.getMessage(), ex);
        }
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(TokenExpiredException.class)
    public ResponseEntity<String> handleTokenExpiredException(TokenExpiredException ex) {
        log.error("Token expired: {}", ex.getMessage(), ex);
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(InvalidJwtException.class)
    public ResponseEntity<String> handleInvalidJwtException(InvalidJwtException ex) {
        log.error("Invalid jwt: {}", ex.getMessage(), ex);
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<String> handleUnauthorizedException(UnauthorizedException ex) {
        log.error("Unauthorized: {}", ex.getMessage(), ex);
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.UNAUTHORIZED);
    }
}
