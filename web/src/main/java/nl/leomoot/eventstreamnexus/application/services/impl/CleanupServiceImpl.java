package nl.leomoot.eventstreamnexus.application.services.impl;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import nl.leomoot.eventstreamnexus.application.services.CleanupService;
import nl.leomoot.eventstreamnexus.configuration.CleanupSchedulerProperties;
import nl.leomoot.eventstreamnexus.infrastructure.persistence.JpaIdempotencyKeyRepository;

/**
 * Service responsible for cleaning up old data from the system.
 */
@Service
public class CleanupServiceImpl implements CleanupService {
    private static final Logger log = LoggerFactory.getLogger(CleanupServiceImpl.class);

    private final JpaIdempotencyKeyRepository idempotencyRepository;
    private final CleanupSchedulerProperties props;

    public CleanupServiceImpl(JpaIdempotencyKeyRepository idempotencyRepository,
                              CleanupSchedulerProperties props) {
        this.idempotencyRepository = idempotencyRepository;
        this.props = props;
    }

    /** {@inheritDoc} */
    @Override
    @Transactional
    public void cleanupOldData() {
        var threshold = Instant.now().minus(props.retentionDays(), ChronoUnit.DAYS);

        log.info("Starting idempotency cleanup. Removing entries older than {} days (threshold: {})", 
            props.retentionDays(), threshold);

        var deleted = idempotencyRepository.deleteOlderThan(threshold);

        log.info("Idempotency cleanup completed. Removed {} entries older than {} days", 
            deleted, props.retentionDays()
        );
    }
}
