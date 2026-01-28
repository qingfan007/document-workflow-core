package com.qingfan.documentcoredemo.web.api;

import com.qingfan.documentcoredemo.exception.DomainException;
import com.qingfan.documentcoredemo.exception.ForbiddenOperationException;
import com.qingfan.documentcoredemo.exception.InvalidStateException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;
import java.util.Map;

@RestControllerAdvice(assignableTypes = { DocumentApiController.class })
public class ApiExceptionHandler {

    @ExceptionHandler(value = ForbiddenOperationException.class)
    public ResponseEntity<?> handleForbidden(ForbiddenOperationException ex) {
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(error(
                        HttpStatus.FORBIDDEN,
                        "FORBIDDEN_OPERATION",
                        ex.getMessage()
                ));

    }

    @ExceptionHandler(InvalidStateException.class)
    public ResponseEntity<?> handleInvalidState(InvalidStateException ex) {
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(error(
                        HttpStatus.CONFLICT,
                        "INVALID_STATE",
                        ex.getMessage()
                ));
    }

    @ExceptionHandler(DomainException.class)
    public ResponseEntity<?> handleDomain(DomainException ex) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(error(
                        HttpStatus.BAD_REQUEST,
                        "DOMAIN_ERROR",
                        ex.getMessage()
                ));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleGeneric(Exception ex) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(error(
                        HttpStatus.INTERNAL_SERVER_ERROR,
                        "INTERNAL_ERROR",
                        "Unexpected error occurred"
                ));
    }


    private Map<String, Object> error(HttpStatus status, String code, String message) {
        return Map.of(
                "timestamp", Instant.now().toString(),
                "status", status.value(),
                "error", status.getReasonPhrase(),
                "code", code,
                "message", message
        );
    }

}
