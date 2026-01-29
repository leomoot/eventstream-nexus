package nl.leomoot.eventstreamnexus.infrastructure.persistence;

import java.time.Instant;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import nl.leomoot.eventstreamnexus.domain.model.IdempotencyKeyEntity;

/**
 * Repository persisting {@link IdempotencyKeyEntity} records keyed by the idempotency token
 * and runs cleanup operations.
 */
@Repository
public interface JpaIdempotencyKeyRepository extends JpaRepository<IdempotencyKeyEntity, UUID> {

      /**
       * Deletes idempotency entries created before the supplied threshold.
       *
       * @param threshold cutoff instant; entries older than this are removed
       * @return number of deleted rows
       */
      @Modifying
      @Query("DELETE FROM IdempotencyKeyEntity e WHERE e.createdAt < :threshold")
      int deleteOlderThan(@Param("threshold") Instant threshold);
}
