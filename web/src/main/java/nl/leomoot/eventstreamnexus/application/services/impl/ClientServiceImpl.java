package nl.leomoot.eventstreamnexus.application.services.impl;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import nl.leomoot.eventstreamnexus.application.dto.ClientPageDto;
import nl.leomoot.eventstreamnexus.application.mappers.ClientMapper;
import nl.leomoot.eventstreamnexus.application.services.ClientService;
import nl.leomoot.eventstreamnexus.application.services.IdempotencyService;
import nl.leomoot.eventstreamnexus.domain.model.ClientEntity;
import nl.leomoot.eventstreamnexus.infrastructure.messaging.ClientEventPublisher;
import nl.leomoot.eventstreamnexus.infrastructure.persistence.JpaClientRepository;
import nl.leomoot.eventstreamnexus.model.CreateClientRequest;

/**
 * Coordinates persistence, idempotency, and event emission for client lifecycle operations.
 */
@Service
public class ClientServiceImpl implements ClientService {

    private final JpaClientRepository jpaClientRepository;
    private final ClientMapper mapper;
    private final IdempotencyService idempotencyService;
    private final ClientEventPublisher events;

    public ClientServiceImpl(
            JpaClientRepository jpaClientRepository,
            ClientMapper mapper,
            IdempotencyService idempotencyService,
            ClientEventPublisher events
    ) {
        this.jpaClientRepository = jpaClientRepository;
        this.mapper = mapper;
        this.idempotencyService = idempotencyService;
        this.events = events;
    }

    /**
     * @inheritDoc
     */
    @Override
    @Transactional
    public ClientEntity createClient(UUID idempotencyKey, CreateClientRequest request) {

        return idempotencyService.execute(idempotencyKey, () -> {

            // Guard against duplicate submissions by creating and publishing exactly once per idempotency key.
            var entity = mapper.toEntity(request);
            var saved = jpaClientRepository.save(entity);

            events.publishClientCreated(saved);

            return saved;
        });
    }

    /**
     * @inheritDoc
     */
    @Override
    @Transactional(readOnly = true)
    public ClientPageDto listClients(Integer limit, String cursor) {
        var page = jpaClientRepository.findPage(limit, cursor);
        return new ClientPageDto(page.items(), page.nextCursor());
    }

    /**
     * @inheritDoc
     */
    @Override
    @Transactional(readOnly = true)
    public ClientEntity getClient(Long id) {
        return jpaClientRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Client not found: " + id));
    }

    /**
     * @inheritDoc
     */
    @Override
    @Transactional
    public void deleteClient(Long id) {
        var entity = jpaClientRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Client not found: " + id));

        jpaClientRepository.delete(entity);

        events.publishClientDeleted(entity);
    }
}
