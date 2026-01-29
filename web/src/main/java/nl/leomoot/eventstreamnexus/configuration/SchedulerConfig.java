package nl.leomoot.eventstreamnexus.configuration;

import javax.sql.DataSource;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import net.javacrumbs.shedlock.core.LockProvider;
import net.javacrumbs.shedlock.provider.jdbctemplate.JdbcTemplateLockProvider;
import net.javacrumbs.shedlock.spring.annotation.EnableSchedulerLock;

/**
 * Spring configuration that enables ShedLock-backed scheduling.
 */
@Configuration
@EnableConfigurationProperties({
        ShedLockProperties.class,
        CleanupSchedulerProperties.class
})
@EnableSchedulerLock(defaultLockAtMostFor = "${shedlock.default-lock-at-most-for}")
public class SchedulerConfig {

    /**
     * Creates the ShedLock {@link LockProvider} using a shared {@link JdbcTemplate} backed by the configured {@link DataSource}.
     * The provider uses database time to synchronise lock expiry across nodes.
     *
     * @param dataSource application {@link DataSource} pointing to the ShedLock table
     * @return lock provider configured for JDBC persistence
     */
    @Bean
    public LockProvider lockProvider(DataSource dataSource) {
        return new JdbcTemplateLockProvider(
            JdbcTemplateLockProvider.Configuration.builder()
                .withJdbcTemplate(new JdbcTemplate(dataSource))
                .usingDbTime()
                .build()
        );
    }
}
