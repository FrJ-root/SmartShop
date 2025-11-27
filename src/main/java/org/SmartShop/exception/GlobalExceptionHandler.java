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
        error.put("timestamp", LocalDateTime.now());

        if (ex.getMessage().contains("Invalid credentials") || ex.getMessage().contains("Non authentifié")) {
            error.put("status", HttpStatus.UNAUTHORIZED.value());
            error.put("message", ex.getMessage());
            return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
        }

        if (ex.getMessage().contains("Access Denied")) {
            error.put("status", HttpStatus.FORBIDDEN.value());
            error.put("error", "Forbidden");
            error.put("message", ex.getMessage());
            return new ResponseEntity<>(error, HttpStatus.FORBIDDEN);
        }

        if (ex.getMessage().contains("Stock") || ex.getMessage().contains("Limit") || ex.getMessage().contains("exceeds")) {
            error.put("status", HttpStatus.BAD_REQUEST.value());
            error.put("message", ex.getMessage());
            return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
        }

        error.put("status", HttpStatus.INTERNAL_SERVER_ERROR.value());
        error.put("message", ex.getMessage());
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}