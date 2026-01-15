package nl.leomoot.eventstreamnexus.mappers;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import org.mapstruct.Mapper;

import jakarta.validation.constraints.NotNull;

/**
 * Mapper for converting between Instant and OffsetDateTime.
 */
@Mapper(componentModel = "spring")
public interface TimeMapper {

    default OffsetDateTime toOffsetDateTime(@NotNull Instant instant) {
        return instant.atOffset(ZoneOffset.UTC);
    }

    default Instant toInstant(@NotNull OffsetDateTime odt) {
        return odt.toInstant();
    }
}
