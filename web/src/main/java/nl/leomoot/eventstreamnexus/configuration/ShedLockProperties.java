package nl.leomoot.eventstreamnexus.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

/**
 * Configuration properties backing the ShedLock integration.
 *
 * @param enabled set to true when ShedLock should acquire locks
 * @param defaultLockAtMostFor fallback max lock duration when none is specified
 * @param defaultLockAtLeastFor fallback minimal lock duration to guarantee execution
 * @param tableName database table name that stores ShedLock entries
 */
@ConfigurationProperties(prefix = "shedlock")
public record ShedLockProperties(
    @NotNull Boolean enabled,
    @NotBlank String defaultLockAtMostFor,
    @NotBlank String defaultLockAtLeastFor,
    @NotBlank String tableName
) {}
