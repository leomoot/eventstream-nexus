package nl.leomoot.eventstreamnexus.application.services.impl;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import nl.leomoot.eventstreamnexus.application.services.IdempotencyService;
import nl.leomoot.eventstreamnexus.domain.model.IdempotencyKeyEntity;
import nl.leomoot.eventstreamnexus.errors.DuplicateIdempotencyKeyException;
import nl.leomoot.eventstreamnexus.infrastructure.persistence.JpaIdempotencyKeyRepository;


/**
 * Stores idempotency keys in the backing repository to protect state-changing operations.
 */
@Service
public class IdempotencyServiceImpl implements IdempotencyService {

    private final JpaIdempotencyKeyRepository repository;

    /**
     * @param repository persistence gateway for {@link IdempotencyKeyEntity} instances
     */
    public IdempotencyServiceImpl(JpaIdempotencyKeyRepository repository) {
        this.repository = repository;
    }

    /**
     * @inheritDoc
     */
    @Override
    @Transactional
    public <T> T execute(UUID key, IdempotentOperation<T> operation) {
        if (repository.existsById(key)) {
            throw new DuplicateIdempotencyKeyException(key);
        }

        repository.save(new IdempotencyKeyEntity(key));

        return operation.run();
    }
}
