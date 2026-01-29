package nl.leomoot.eventstreamnexus.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Properties that configure the cleanup scheduler cron expression and lock durations.
 *
 * @param cron cron expression controlling cleanup job timing
 * @param lockAtMostFor maximum time the cleanup lock may be held
 * @param lockAtLeastFor minimum time the cleanup lock stays held to avoid overlap
 */
@ConfigurationProperties(prefix = "scheduling.cleanup")
public record CleanupSchedulerProperties(
    @NotBlank String cron,
    @NotBlank String lockAtMostFor,
    @NotBlank String lockAtLeastFor,
    @NotNull Integer retentionDays
) {}
