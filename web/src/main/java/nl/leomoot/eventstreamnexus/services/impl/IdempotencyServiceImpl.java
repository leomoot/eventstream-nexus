package nl.leomoot.eventstreamnexus.services.impl;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import nl.leomoot.eventstreamnexus.domain.model.IdempotencyKeyEntity;
import nl.leomoot.eventstreamnexus.domain.repository.IdempotencyKeyRepository;
import nl.leomoot.eventstreamnexus.services.IdempotencyService;

/**
 * Stores idempotency keys in the backing repository to protect state-changing operations.
 */
@Service
public class IdempotencyServiceImpl implements IdempotencyService {

    private final IdempotencyKeyRepository repository;

    /**
     * @param repository persistence gateway for {@link IdempotencyKeyEntity} instances
     */
    public IdempotencyServiceImpl(IdempotencyKeyRepository repository) {
        this.repository = repository;
    }

    /**
     * @inheritDoc
     */
    @Override
    @Transactional
    public <T> T execute(UUID key, IdempotentOperation<T> operation) {

        if (repository.existsById(key)) {
            throw new IllegalStateException("Duplicate idempotency key: " + key);
        }

        repository.save(new IdempotencyKeyEntity(key));

        return operation.run();
    }
}
