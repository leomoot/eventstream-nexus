package nl.leomoot.eventstreamnexus.application.services.impl;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import nl.leomoot.eventstreamnexus.configuration.CleanupSchedulerProperties;
import nl.leomoot.eventstreamnexus.infrastructure.persistence.JpaIdempotencyKeyRepository;

@ExtendWith(MockitoExtension.class)
class CleanupServiceImplTest {

    @Mock
    private JpaIdempotencyKeyRepository idempotencyRepository;

    private static final CleanupSchedulerProperties PROPERTIES =
        new CleanupSchedulerProperties("0 0 * * * *", "PT30S", "PT5S", 30);

    @Test
    void cleanupOldDataDeletesEntriesOlderThanRetentionThreshold() {
        when(idempotencyRepository.deleteOlderThan(any(Instant.class))).thenReturn(3);

        var cleanupService = new CleanupServiceImpl(idempotencyRepository, PROPERTIES);

        var beforeInvocation = Instant.now();

        cleanupService.cleanupOldData();

        var afterInvocation = Instant.now();

        ArgumentCaptor<Instant> thresholdCaptor = ArgumentCaptor.forClass(Instant.class);
        verify(idempotencyRepository).deleteOlderThan(thresholdCaptor.capture());
        verifyNoMoreInteractions(idempotencyRepository);

        var expectedLowerBound = beforeInvocation.minus(PROPERTIES.retentionDays(), ChronoUnit.DAYS);
        var expectedUpperBound = afterInvocation.minus(PROPERTIES.retentionDays(), ChronoUnit.DAYS);

        assertThat(thresholdCaptor.getValue()).isBetween(expectedLowerBound, expectedUpperBound);
    }
}
