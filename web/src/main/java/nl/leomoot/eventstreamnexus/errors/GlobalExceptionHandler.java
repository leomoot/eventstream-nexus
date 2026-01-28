package nl.leomoot.eventstreamnexus.errors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import nl.leomoot.eventstreamnexus.model.ErrorResponse;

/**
 * Centralizes REST exception translation into {@link ErrorResponse} payloads.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Returns a HTTP 409 (Conflict) response when an idempotency key is reused.
     */
    @ExceptionHandler(DuplicateIdempotencyKeyException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateIdempotencyKey(DuplicateIdempotencyKeyException ex) {
        var error = new ErrorResponse("duplicate_idempotency_key", ex.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
    }

    /**
     * Returns a HTTP 400 (Bad Request) response when required headers are missing.
     */
    @ExceptionHandler(MissingHeaderException.class)
    public ResponseEntity<ErrorResponse> handleMissingHeader(MissingHeaderException ex) {
        var error = new ErrorResponse("missing_header", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }
}
