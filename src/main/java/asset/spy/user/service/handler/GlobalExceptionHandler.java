package asset.spy.user.service.handler;

import asset.spy.user.service.dto.ValidationErrorDto;
import asset.spy.user.service.exception.ContactNotFoundException;
import asset.spy.user.service.exception.UserNotFoundException;
import lombok.extern.slf4j.Slf4j;
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
        log.error("Validation errors: {}", validationErrorDTO);
        return new ResponseEntity<>(validationErrorDTO, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({ContactNotFoundException.class, UserNotFoundException.class})
    public ResponseEntity<String> handleNotFoundException(RuntimeException ex) {
        log.warn("Resource not found: {}", ex.getMessage());
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }
}
