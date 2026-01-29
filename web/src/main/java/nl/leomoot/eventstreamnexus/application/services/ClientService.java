package nl.leomoot.eventstreamnexus.application.services;

import java.util.UUID;

import nl.leomoot.eventstreamnexus.application.dto.ClientPageDto;
import nl.leomoot.eventstreamnexus.domain.model.ClientEntity;
import nl.leomoot.eventstreamnexus.model.CreateClientRequest;

/** 
 * Service interface for managing {@link ClientEntity} instances
 * and exposing {@link ClientPageDto} views. 
 */
public interface ClientService {

    /**
     * Create a new {@link ClientEntity}.
     * 
     * @param idempotencyKey idempotency guard for the request
     * @param request data for creating the client via {@link CreateClientRequest}
     * @return the created {@link ClientEntity}
     */
    ClientEntity createClient(UUID idempotencyKey, CreateClientRequest request);

    /**
     * List clients with pagination.
     * 
     * @param limit number of clients to return
     * @param cursor pagination cursor
     * @return a {@link ClientPageDto} page of clients
     */
    ClientPageDto listClients(Integer limit, String cursor);

    /**
     * Get a client by ID.
     * 
     * @param id identifier of the client to retrieve
     * @return the {@link ClientEntity}
     */
    ClientEntity getClient(Long id);

    /**
     * Delete a client by ID.
     * 
     * @param id identifier of the client to delete
     */
    void deleteClient(Long id);
}
