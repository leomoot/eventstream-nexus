package nl.leomoot.eventstreamnexus.infrastructure.persistence;

import java.nio.ByteBuffer;
import java.util.Base64;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.stereotype.Repository;

import jakarta.persistence.EntityManager;
import nl.leomoot.eventstreamnexus.application.dto.ClientPageDto;
import nl.leomoot.eventstreamnexus.domain.model.ClientEntity;

/**
 * Repository implementation that backs {@link ClientRepository}
 * with cursor-based pagination support.
 */
@Repository
public class JpaClientRepository extends SimpleJpaRepository<ClientEntity, Long> {

    private static final Logger log = LoggerFactory.getLogger(JpaClientRepository.class);

    private final EntityManager entityManager;

    /**
     * Constructs a repository delegating to {@link SimpleJpaRepository} 
     * while retaining the {@link EntityManager}.
     *
     * @param em managed {@link EntityManager}
     */
    public JpaClientRepository(EntityManager em) {
        super(ClientEntity.class, em);
        this.entityManager = em;
    }

    public ClientPageDto findPage(int limit, String cursor) {

        var cursorId = decodeCursor(cursor);
        log.debug("Finding clients page with limit={} and cursorId={}", limit, cursorId);

        var query = entityManager.createQuery("""
                SELECT c FROM ClientEntity c
                WHERE (:cursorId IS NULL OR c.id > :cursorId)
                ORDER BY c.id ASC
                """, ClientEntity.class);

        query.setParameter("cursorId", cursorId);
        query.setMaxResults(limit + 1);

        var results = query.getResultList();
        var hasMore = results.size() > limit;

        if (hasMore) {
            results = results.subList(0, limit);
        }

        String nextCursor = hasMore
                ? encodeCursor(results.get(results.size() - 1).getId())
                : null;

        log.debug("Returning {} clients (hasMore={}) with nextCursor={}", results.size(), hasMore, nextCursor);
        return new ClientPageDto(results, nextCursor);
    }

    /**
     * Encode the last seen identifier into a Base64 cursor.
     */
    private String encodeCursor(Long id) {
        var buffer = ByteBuffer.allocate(Long.BYTES).putLong(0, id);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(buffer.array());
    }

    /**
     * Decode a Base64 cursor into the original identifier.
     */
    private Long decodeCursor(String cursor) {
        if (cursor == null) {
            return null;
        }

        try {
            var bytes = Base64.getUrlDecoder().decode(cursor);
            if (bytes.length != Long.BYTES) {
                log.warn("Cursor length mismatch; expected {} bytes but got {}", Long.BYTES, bytes.length);
                return null;
            }

            return ByteBuffer.wrap(bytes).getLong();
        } catch (IllegalArgumentException ex) {
            log.warn("Failed to decode cursor '{}': {}", cursor, ex.getMessage());
            return null;
        }
    }
}
