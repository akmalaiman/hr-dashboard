package aaiman.hrdashboardapi.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.nio.file.AccessDeniedException;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

        @ExceptionHandler(JwtAuthenticationException.class)
        public ResponseEntity<String> handleJwtAuthenticationException(JwtAuthenticationException e) {
                log.error("Invalid JWT token: {}", e.getMessage());
                return new ResponseEntity<>("Invalid or expired JWT token", HttpStatus.UNAUTHORIZED);
        }

        @ExceptionHandler(AuthenticationException.class)
        public ResponseEntity<String> handleAuthenticationException(AuthenticationException e) {
                log.error("Authentication failed: {}", e.getMessage());
                return new ResponseEntity<>("Authentication required", HttpStatus.UNAUTHORIZED);
        }

        @ExceptionHandler(AccessDeniedException.class)
        public ResponseEntity<String> handleAccessDeniedException(AccessDeniedException e) {
                log.error("Access denied: {}", e.getMessage());
                return new ResponseEntity<>("Access denied", HttpStatus.FORBIDDEN);
        }

        @ExceptionHandler(Exception.class)
        public ResponseEntity<String> handleGenericException(Exception e) {
                log.error("Unexpected error: {}", e.getMessage());
                return new ResponseEntity<>("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR);
        }

}
