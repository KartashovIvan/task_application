package ru.kartashov.task_application.config;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import javax.naming.AuthenticationException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<String> handlerException(AuthenticationException e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Authentication failed " + e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handlerGenericException(Exception e) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred " + e.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handlerGenericException(RuntimeException e) {
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(e.getMessage());
    }
}
