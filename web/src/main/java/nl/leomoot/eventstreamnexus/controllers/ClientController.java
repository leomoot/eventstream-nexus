package nl.leomoot.eventstreamnexus.controllers;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import nl.leomoot.eventstreamnexus.api.V1Api;
import nl.leomoot.eventstreamnexus.mappers.ClientMapper;
import nl.leomoot.eventstreamnexus.model.Client;
import nl.leomoot.eventstreamnexus.model.ClientPage;
import nl.leomoot.eventstreamnexus.model.CreateClientRequest;
import nl.leomoot.eventstreamnexus.services.ClientService;

/**
 * REST controller for managing clients.
 */
@RestController
public class ClientController implements V1Api {

    private final ClientService service;
    private final ClientMapper mapper;

    /**
     * Constructor.
     *
     * @param service Client service
     * @param mapper  Client mapper
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
        var created = service.createClient(idempotencyKey, request);
        return ResponseEntity.status(201).body(mapper.toApi(created));
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
