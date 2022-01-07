package demo.controller;

import demo.service.BankException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class BankExceptionHandler extends ResponseEntityExceptionHandler {
    @ExceptionHandler(BankException.class)
    public ResponseEntity<String> handlePaymentException (BankException exception, WebRequest request) {
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        if (exception.getError() == BankException.ErrorCode.USER_NOT_FOUND) {
            status = HttpStatus.UNAUTHORIZED;
        } else if (exception.getError() == BankException.ErrorCode.BAD_CREDENTIALS) {
            status = HttpStatus.FORBIDDEN;
        }
        return new ResponseEntity<>(exception.getError().name(), status);
    }
}
