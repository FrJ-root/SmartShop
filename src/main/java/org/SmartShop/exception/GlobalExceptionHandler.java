package org.SmartShop.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, Object>> handleRuntimeException(RuntimeException ex) {
        Map<String, Object> error = new HashMap<>();
        error.put("timestamp", LocalDateTime.now()); // [cite: 201]

        if (ex.getMessage().contains("Invalid credentials")) {
            error.put("status", HttpStatus.UNAUTHORIZED.value()); // [cite: 192]
            error.put("error", "Unauthorized");
            error.put("message", "Non authentifié");
            return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
        }

        error.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value()); // [cite: 199]
        error.put("message", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}