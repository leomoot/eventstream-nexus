package nl.leomoot.eventstreamnexus.services.impl;

import java.util.UUID;

import nl.leomoot.eventstreamnexus.domain.model.ClientEntity;
import nl.leomoot.eventstreamnexus.domain.repository.ClientRepository;
import nl.leomoot.eventstreamnexus.mappers.ClientMapper;
import nl.leomoot.eventstreamnexus.model.CreateClientRequest;
import nl.leomoot.eventstreamnexus.services.dto.ClientPageDto;
import nl.leomoot.eventstreamnexus.services.ClientEventPublisher;

import nl.leomoot.eventstreamnexus.services.ClientService;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import nl.leomoot.eventstreamnexus.services.IdempotencyService;

@Service
public class ClientServiceImpl implements ClientService {

    private final ClientRepository repository;
    private final ClientMapper mapper;
    private final IdempotencyService idempotency;
    private final ClientEventPublisher events;

    public ClientServiceImpl(
            ClientRepository repository,
            ClientMapper mapper,
            IdempotencyService idempotency,
            ClientEventPublisher events
    ) {
        this.repository = repository;
        this.mapper = mapper;
        this.idempotency = idempotency;
        this.events = events;
    }

    @Override
    @Transactional
    public ClientEntity createClient(UUID idempotencyKey, CreateClientRequest request) {

        return idempotency.execute(idempotencyKey, () -> {

            var entity = mapper.toEntity(request);
            var saved = repository.save(entity);

            events.publishClientCreated(saved);

            return saved;
        });
    }

    @Override
    @Transactional(readOnly = true)
    public ClientPageDto listClients(Integer limit, String cursor) {
        var page = repository.findPage(limit, cursor);
        return new ClientPageDto(page.items(), page.nextCursor());
    }

    @Override
    @Transactional(readOnly = true)
    public ClientEntity getClient(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Client not found: " + id));
    }

    @Override
    @Transactional
    public void deleteClient(Long id) {
        var entity = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Client not found: " + id));

        repository.delete(entity);

        events.publishClientDeleted(entity);
    }
}
