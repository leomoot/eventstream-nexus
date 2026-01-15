package nl.leomoot.eventstreamnexus.services;

import java.util.UUID;
import nl.leomoot.eventstreamnexus.domain.model.ClientEntity;
import nl.leomoot.eventstreamnexus.model.CreateClientRequest;
import nl.leomoot.eventstreamnexus.services.dto.ClientPageDto;

public interface ClientService {

    /**
     * Create a new client.
     * 
     * @param idempotencyKey for the request
     * @param request data for creating the client
     * @return the created client entity
     */
    ClientEntity createClient(UUID idempotencyKey, CreateClientRequest request);

    /**
     * List clients with pagination.
     * 
     * @param limit number of clients to return
     * @param cursor pagination cursor
     * @return a page of clients
     */
    ClientPageDto listClients(Integer limit, String cursor);

    /**
     * Get a client by ID.
     * 
     * @param identifier of the client to retrieve
     * @return the client entity
     */
    ClientEntity getClient(Long id);

    /**
     * Delete a client by ID.
     * 
     * @param identifier of the client to delete
     */
    void deleteClient(Long id);
}
