package nl.leomoot.eventstreamnexus.services.dto;

import java.util.List;

import nl.leomoot.eventstreamnexus.domain.model.ClientEntity;

/** 
 * DTO representing a page of clients.
 */
public record ClientPageDto(
        List<ClientEntity> items,
        String nextCursor
) {}
