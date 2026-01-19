package nl.leomoot.eventstreamnexus.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import nl.leomoot.eventstreamnexus.domain.model.ClientEntity;
import nl.leomoot.eventstreamnexus.services.dto.ClientPageDto;

/**
 * Repository abstraction for persisting {@link ClientEntity} instances 
 * and serving {@link ClientPageDto} slices.
 */
public interface ClientRepository extends JpaRepository<ClientEntity, Long> {

    /**
     * Retrieve a {@link ClientPageDto} constrained by the provided paging parameters.
     *
     * @param limit maximum number of items
     * @param cursor opaque paging cursor
     * @return resulting {@link ClientPageDto}
     */
    ClientPageDto findPage(int limit, String cursor);
}
