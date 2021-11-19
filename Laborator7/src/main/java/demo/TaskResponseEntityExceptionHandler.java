package demo;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ValidationException;
import java.util.stream.Collectors;

@ControllerAdvice
public class TaskResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ValidationException.class)
    public final ResponseEntity<String[]> handleValidationException (Exception exception, WebRequest request) {
        return new ResponseEntity<>(exception.getMessage().split("\\|"), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public final ResponseEntity<String> handleOtherException (Exception exception, WebRequest request) {
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        String errors = ex.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> fieldError.getDefaultMessage()).collect(Collectors.joining(" "));
        return ResponseEntity.badRequest().body(errors);
    }
}
