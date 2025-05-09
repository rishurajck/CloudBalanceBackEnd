package com.project.cloudbalance.exception;

import com.project.cloudbalance.exception.customException.*;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.validation.UnexpectedTypeException;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import net.snowflake.client.jdbc.internal.google.api.Http;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ExpiredJwtException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<Map<String, String>> handleExpiredJwtException(ExpiredJwtException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("status", String.valueOf(HttpStatus.BAD_REQUEST.value()));
        response.put("message", ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Map<String, String>> handleIllegalArgumentException(IllegalArgumentException e)
    {
        Map<String, String> response = new HashMap<>();
        response.put("status", String.valueOf(HttpStatus.BAD_REQUEST.value()));
        response.put("message", e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(BlackListedTokenException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<Map<String, String>> handleBlackListedTokenException(BlackListedTokenException e) {
        Map<String, String> response = new HashMap<>();
        response.put("status", String.valueOf(HttpStatus.UNAUTHORIZED.value()));
        response.put("message", e.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    @ExceptionHandler(RequestDtoNull.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<Map<String, String>> handleRequestDtoNUll(RequestDtoNull e)
    {
        Map<String, String> response = new HashMap<>();
        response.put("status", String.valueOf(HttpStatus.BAD_REQUEST.value()));
        response.put("message", e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Map<String, String>> handleTypeMismatch(MethodArgumentTypeMismatchException ex) {
        Map<String, String> error = new HashMap<>();
        error.put("error", "Invalid ID");
        error.put("message", "ID must be a numeric value. Got: " + ex.getValue() + " ");
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND); // send 404
    }
    @ExceptionHandler(InvalidCredentialsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handleInvalidCredentialsException(InvalidCredentialsException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<Map<String, String>> handleUsernameNotFoundException(UsernameNotFoundException e)
    {
        Map<String, String> response = new HashMap<>();
        response.put("status", String.valueOf(HttpStatus.NOT_FOUND.value()));
        response.put("message", e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(AccountsNotFound.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<String> handleAccountNotFound(AccountsNotFound e)
    {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage() + "No Accounts Found");
    }
    @ExceptionHandler(AccountsAlreadyExists.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handleAccountAlreadyExists(AccountsAlreadyExists e)
    {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage()+ "Account Already Exists");
    }

    @ExceptionHandler(SignatureException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<String> handleSignatureException(SignatureException e)
    {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage() + "Token invalid");
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<Map<String, String>> handleUserAlreadyExistsException(UserAlreadyExistsException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("status", String.valueOf(HttpStatus.BAD_REQUEST.value()));
        response.put("message", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(response); // Custom error message
    }
    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<Map<String, String>> handleEmailAlreadyExistsException(EmailAlreadyExistsException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("status", String.valueOf(HttpStatus.BAD_REQUEST.value()));
        response.put("message", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(response);
    }

    @ExceptionHandler(UnexpectedTypeException.class)
    public ResponseEntity<Map<String, Object>> handleUnexpectedTypeException(UnexpectedTypeException e) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("status", HttpStatus.BAD_REQUEST.value());

        // Attempt to extract field name from the exception message
        String fieldName = "unknown";
        Pattern pattern = Pattern.compile("Check configuration for '(.*?)'");
        Matcher matcher = pattern.matcher(e.getMessage());
        if (matcher.find()) {
            fieldName = matcher.group(1);
        }

        errorResponse.put("message", "Invalid validation annotation used on field: '" + fieldName + "'");

        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", HttpStatus.BAD_REQUEST.value());

        String message = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .findFirst()
                .map(FieldError::getDefaultMessage)
                .orElse("Validation error");

        response.put("message", message);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }


    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("status", HttpStatus.BAD_REQUEST.value());
        response.put("message", "Request body is missing or improperly formatted.");
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }



}
