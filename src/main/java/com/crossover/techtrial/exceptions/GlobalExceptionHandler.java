package com.crossover.techtrial.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.AbstractMap;

@ControllerAdvice
@Component
public class GlobalExceptionHandler {

    private static final Logger LOG = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(BookNotFoundException.class)
    public ResponseEntity<AbstractMap.SimpleEntry<String, String>> handleBookNotFound(Exception exception) {
        LOG.error("Exception: Unable to process this request. ", exception);
        AbstractMap.SimpleEntry<String, String> response =
                new AbstractMap.SimpleEntry<>("message", "Requested book not found");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(MemberNotFoundException.class)
    public ResponseEntity<AbstractMap.SimpleEntry<String, String>> handleMemberNotFound(Exception exception) {
        LOG.error("Exception: Unable to process this request. ", exception);
        AbstractMap.SimpleEntry<String, String> response =
                new AbstractMap.SimpleEntry<>("message", "Requested member not found");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(TransactionNotFoundException.class)
    public ResponseEntity<AbstractMap.SimpleEntry<String, String>> handleTransactionNotFound(Exception exception) {
        LOG.error("Exception: Unable to process this request. ", exception);
        AbstractMap.SimpleEntry<String, String> response =
                new AbstractMap.SimpleEntry<>("message", "Requested transaction not found");
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(BookIsAlreadyIssuedToSomeoneException.class)
    public ResponseEntity<AbstractMap.SimpleEntry<String, String>> handleBookIsAlreadyIssued(Exception exception) {
        LOG.error("Exception: Unable to process this request. ", exception);
        AbstractMap.SimpleEntry<String, String> response =
                new AbstractMap.SimpleEntry<>("message", "Requested book is already issued to someone");
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ValidationErrorResponse> handleValidationError(ConstraintViolationException exception) {
        LOG.error("Exception: Unable to process this request. ", exception);
        ValidationErrorResponse error = new ValidationErrorResponse();
        for (ConstraintViolation violation : exception.getConstraintViolations()) {
            error.getViolations().add(
                    new Violation(violation.getPropertyPath().toString(), violation.getMessage()));
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    /**
     * Global Exception handler for all exceptions.
     */
    @ExceptionHandler
    public ResponseEntity<AbstractMap.SimpleEntry<String, String>> handle(Exception exception) {
        // general exception
        LOG.error("Exception: Unable to process this request. ", exception);
        AbstractMap.SimpleEntry<String, String> response =
                new AbstractMap.SimpleEntry<>("message", "Unable to process this request.");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

}
