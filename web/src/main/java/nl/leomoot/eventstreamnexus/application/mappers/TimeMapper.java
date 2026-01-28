package nl.leomoot.eventstreamnexus.application.mappers;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import org.mapstruct.Mapper;

import jakarta.validation.constraints.NotNull;

/**
 * Mapper for converting between {@link Instant} and {@link OffsetDateTime} (TimeZone-aware).
 */
@Mapper(componentModel = "spring")
public interface TimeMapper {

    /** 
     * Convert {@link Instant} to {@link OffsetDateTime} in UTC.
     * 
     * @param instant the {@link Instant} to convert
     * @return the corresponding {@link OffsetDateTime} in UTC
     */
    default OffsetDateTime toOffsetDateTime(@NotNull Instant instant) {
        return instant.atOffset(ZoneOffset.UTC);
    }

    /** 
     * Convert {@link OffsetDateTime} to {@link Instant}.
     * 
     * @param odt the {@link OffsetDateTime} to convert
     * @return the corresponding {@link Instant}
     */
    default Instant toInstant(@NotNull OffsetDateTime odt) {
        return odt.toInstant();
    }
}
