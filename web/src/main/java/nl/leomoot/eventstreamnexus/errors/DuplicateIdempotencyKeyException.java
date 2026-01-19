package nl.leomoot.eventstreamnexus.errors;

import java.util.UUID;

/**
 * Exception indicating a duplicate idempotency key conflict.
 */
public class DuplicateIdempotencyKeyException extends RuntimeException {

    /**
     * @param key the conflicting idempotency token
     */
    public DuplicateIdempotencyKeyException(UUID key) {
        super("Duplicate idempotency key: " + key);
    }
}
