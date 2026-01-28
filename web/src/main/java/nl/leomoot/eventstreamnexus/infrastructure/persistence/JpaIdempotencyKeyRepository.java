package nl.leomoot.eventstreamnexus.infrastructure.persistence;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import nl.leomoot.eventstreamnexus.domain.model.IdempotencyKeyEntity;

/**
 * Repository persisting {@link IdempotencyKeyEntity} records keyed by the idempotency token.
 */
public interface JpaIdempotencyKeyRepository extends JpaRepository<IdempotencyKeyEntity, UUID> {
}
