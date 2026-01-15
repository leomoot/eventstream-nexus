package nl.leomoot.eventstreamnexus.mappers;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import nl.leomoot.eventstreamnexus.domain.model.ClientEntity;
import nl.leomoot.eventstreamnexus.model.Client;
import nl.leomoot.eventstreamnexus.model.ClientPage;
import nl.leomoot.eventstreamnexus.services.dto.ClientPageDto;

@Mapper(
    componentModel = "spring",
    uses = { TimeMapper.class }
)
public interface ClientMapper {

    /**
     * Map a ClientEntity to API Client model.
     * 
     * @param entity the ClientEntity to map
     * @return the mapped Client model
     */
    Client toApi(ClientEntity entity);

    /**
     * Map a ClientPageDto to API ClientPage model.
     * 
     * @param entity the ClientPageDto to map
     * @return the mapped ClientPage model
     */
    @Mapping(target = "items", expression = "java(entity.items().stream().map(this::toApi).toList())")
    ClientPage toApi(ClientPageDto entity);
}
