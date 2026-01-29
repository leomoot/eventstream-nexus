package nl.leomoot.eventstreamnexus.infrastructure.scheduler;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import nl.leomoot.eventstreamnexus.application.services.CleanupService;

/**
 * Schedules the periodic cleanup task and guards it with ShedLock to enforce single-node execution.
 */
@Component
public class CleanupScheduler {
    private final CleanupService cleanupService;

    /**
     * Creates the scheduler with the cleanup service implementation that will perform deletions.
     *
     * @param cleanupService service handling cleanup operations
     */
    public CleanupScheduler(CleanupService cleanupService) {
        this.cleanupService = cleanupService;
    }
    
    /**
     * Executes the cleanup job, invoked according to the configured cron schedule.
     */
    @Scheduled(cron = "${scheduling.cleanup.cron}")
    @SchedulerLock(
        name = "cleanupTask",
        lockAtMostFor = "${scheduling.cleanup.lock-at-most-for}",
        lockAtLeastFor = "${scheduling.cleanup.lock-at-least-for}"
    )
    public void cleanup() {
        cleanupService.cleanupOldData();
    }
}
