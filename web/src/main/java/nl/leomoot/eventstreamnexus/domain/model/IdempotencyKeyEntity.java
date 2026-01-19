package nl.leomoot.eventstreamnexus.domain.model;

import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 * JPA entity persisting processed idempotency keys to guard against replayed commands.
 */
@Entity
@Table(name = "idempotency_keys")
public class IdempotencyKeyEntity {

    @Id
    private UUID key;

    protected IdempotencyKeyEntity() {
        // JPA requires a no-arg constructor for entity instantiation
    }

    /**
     * Create a tracked key instance.
     *
     * @param key unique idempotency token
     */
    public IdempotencyKeyEntity(UUID key) {
        this.key = key;
    }

    /**
     * Access the stored idempotency token.
     *
     * @return unique idempotency key
     */
    public UUID getKey() {
        return key;
    }
}
