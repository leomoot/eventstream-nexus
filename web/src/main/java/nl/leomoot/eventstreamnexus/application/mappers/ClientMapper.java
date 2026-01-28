package nl.leomoot.eventstreamnexus.application.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import nl.leomoot.eventstreamnexus.application.dto.ClientPageDto;
import nl.leomoot.eventstreamnexus.domain.model.ClientEntity;
import nl.leomoot.eventstreamnexus.model.Client;
import nl.leomoot.eventstreamnexus.model.ClientPage;
import nl.leomoot.eventstreamnexus.model.CreateClientRequest;

/** 
 * Mapper for converting between {@link ClientEntity} and {@link Client} models.
 * 
 * @see TimeMapper for time-related mappings.
 */
@Mapper(
    componentModel = "spring",
    uses = { TimeMapper.class }
)
public interface ClientMapper {

    /**
     * Maps a {@link ClientEntity} to API {@link Client} model.
     * 
     * @param entity the {@link ClientEntity} to map
     * @return the mapped {@link Client}
     */
    Client toApi(ClientEntity entity);

    /**
     * Maps a {@link ClientPageDto} to API {@link ClientPage} model.
     * 
     * @param entity the {@link ClientPageDto} to map
     * @return the mapped {@link ClientPage}
     */
    @Mapping(target = "items", expression = "java(entity.items().stream().map(this::toApi).toList())")
    ClientPage toApi(ClientPageDto entity);

    /**
     * Maps a {@link CreateClientRequest} to {@link ClientEntity} for persistence.
     * 
     * @param request the {@link CreateClientRequest} to map
     * @return the mapped {@link ClientEntity}
     */
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdAt", expression = "java(java.time.Instant.now())") 
    ClientEntity toEntity(CreateClientRequest request);
}
