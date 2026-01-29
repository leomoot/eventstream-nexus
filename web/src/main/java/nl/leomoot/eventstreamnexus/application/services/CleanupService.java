package nl.leomoot.eventstreamnexus.application.services;

/**
 * Service abstraction defining cleanup operations for stale or expired data.
 */
public interface CleanupService {

    /**
     * Removes outdated data to keep storage tidy and within retention policies.
     */
    void cleanupOldData();
}
