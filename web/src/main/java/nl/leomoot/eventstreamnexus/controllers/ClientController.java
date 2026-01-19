package nl.leomoot.eventstreamnexus.controllers;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import nl.leomoot.eventstreamnexus.api.V1Api;
import nl.leomoot.eventstreamnexus.errors.MissingHeaderException;
import nl.leomoot.eventstreamnexus.mappers.ClientMapper;
import nl.leomoot.eventstreamnexus.model.Client;
import nl.leomoot.eventstreamnexus.model.ClientPage;
import nl.leomoot.eventstreamnexus.model.CreateClientRequest;
import nl.leomoot.eventstreamnexus.services.ClientService;

/**
 * REST controller for managing {@link Client} resources via {@link ClientService}.
 */
@RestController
public class ClientController implements V1Api {

    private static final String IDEMPOTENCY_HEADER = "X-Idempotency-Key";
    private static final Logger log = LoggerFactory.getLogger(ClientController.class);

    private final ClientService service;
    private final ClientMapper mapper;

    /**
     * Constructor wiring {@link ClientService} and {@link ClientMapper}.
     *
     * @param service {@link ClientService} dependency
     * @param mapper {@link ClientMapper} dependency
     */
    public ClientController(ClientService service, ClientMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    /**
     * @inheritDoc
     */
    @Override
    public ResponseEntity<Client> createClient(UUID idempotencyKey, CreateClientRequest request) {
        if (idempotencyKey == null) {
            throw new MissingHeaderException(IDEMPOTENCY_HEADER);
        }
        log.debug("Creating client for idempotencyKey={}", idempotencyKey);

        var created = service.createClient(idempotencyKey, request);
        var response = ResponseEntity.status(HttpStatus.CREATED).body(mapper.toApi(created));
        log.info("Created client {}", created.getId());

        return response;
    }

    /**
     * @inheritDoc
     */
    @Override
    public ResponseEntity<ClientPage> listClients(Integer limit, String cursor) {
        var page = service.listClients(limit, cursor);
        return ResponseEntity.ok(mapper.toApi(page));
    }

    /**
     * @inheritDoc
     */
    @Override
    public ResponseEntity<Client> getClient(Long clientId) {
        var client = service.getClient(clientId);
        return ResponseEntity.ok(mapper.toApi(client));
    }

    /**
     * @inheritDoc
     */
    @Override
    public ResponseEntity<Void> deleteClient(Long clientId) {
        service.deleteClient(clientId);
        return ResponseEntity.noContent().build();
    }
}
