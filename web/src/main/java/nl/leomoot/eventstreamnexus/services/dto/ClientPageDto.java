package nl.leomoot.eventstreamnexus.services.dto;

import nl.leomoot.eventstreamnexus.domain.model.ClientEntity;
import java.util.List;

public record ClientPageDto(
        List<ClientEntity> items,
        String nextCursor
) {}
