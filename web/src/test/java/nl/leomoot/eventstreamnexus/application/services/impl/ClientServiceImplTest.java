package nl.leomoot.eventstreamnexus.application.services.impl;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import nl.leomoot.eventstreamnexus.application.dto.ClientPageDto;
import nl.leomoot.eventstreamnexus.application.mappers.ClientMapper;
import nl.leomoot.eventstreamnexus.application.services.IdempotencyService;
import nl.leomoot.eventstreamnexus.domain.model.ClientEntity;
import nl.leomoot.eventstreamnexus.infrastructure.messaging.ClientEventPublisher;
import nl.leomoot.eventstreamnexus.infrastructure.persistence.JpaClientRepository;
import nl.leomoot.eventstreamnexus.model.CreateClientRequest;

@ExtendWith(MockitoExtension.class)
class ClientServiceImplTest {

    @Mock
    private JpaClientRepository jpaClientRepository;

    @Mock
    private ClientMapper mapper;

    @Mock
    private IdempotencyService idempotencyService;

    @Mock
    private ClientEventPublisher events;

    private ClientServiceImpl service() {
        return new ClientServiceImpl(jpaClientRepository, mapper, idempotencyService, events);
    }

    @Test
    void createClientRunsUnderIdempotencyAndPublishesEvent() {
        var idempotencyKey = UUID.randomUUID();
        var request = new CreateClientRequest("Acme Corp", "leo@acme.test");
        var entityToPersist = new ClientEntity("Acme Corp", "leo@acme.test", Instant.now());
        var persistedEntity = new ClientEntity("Acme Corp", "leo@acme.test", Instant.now());

        when(mapper.toEntity(request)).thenReturn(entityToPersist);
        when(jpaClientRepository.save(entityToPersist)).thenReturn(persistedEntity);
        when(idempotencyService.execute(eq(idempotencyKey), any())).thenAnswer(invocation -> {
            @SuppressWarnings("unchecked")
            IdempotencyService.IdempotentOperation<ClientEntity> operation =
                (IdempotencyService.IdempotentOperation<ClientEntity>) invocation.getArgument(1);
            return operation.run();
        });

        var result = service().createClient(idempotencyKey, request);

        assertThat(result).isSameAs(persistedEntity);
        verify(idempotencyService).execute(eq(idempotencyKey), any());
        verify(mapper).toEntity(request);
        verify(jpaClientRepository).save(entityToPersist);
        verify(events).publishClientCreated(persistedEntity);
        verifyNoMoreInteractions(events);
    }

    @Test
    void listClientsReturnsRepositoryPage() {
        var expectedPage = new ClientPageDto(List.of(new ClientEntity("Acme", "a@acme.test", Instant.now())), "cursor-2");
        when(jpaClientRepository.findPage(20, "cursor-1")).thenReturn(expectedPage);

        var page = service().listClients(20, "cursor-1");

        assertThat(page).isEqualTo(expectedPage);
        verify(jpaClientRepository).findPage(20, "cursor-1");
        verifyNoInteractions(mapper, idempotencyService, events);
    }

    @Test
    void getClientReturnsEntityWhenPresent() {
        var client = new ClientEntity("Acme", "a@acme.test", Instant.now());
        when(jpaClientRepository.findById(15L)).thenReturn(Optional.of(client));

        var result = service().getClient(15L);

        assertThat(result).isSameAs(client);
        verify(jpaClientRepository).findById(15L);
        verifyNoInteractions(mapper, idempotencyService, events);
    }

    @Test
    void getClientThrowsWhenMissing() {
        when(jpaClientRepository.findById(42L)).thenReturn(Optional.empty());

        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> service().getClient(42L))
            .withMessage("Client not found: 42");

        verify(jpaClientRepository).findById(42L);
        verifyNoInteractions(mapper, idempotencyService, events);
    }

    @Test
    void deleteClientRemovesEntityAndPublishesEvent() {
        var client = new ClientEntity("Acme", "a@acme.test", Instant.now());
        when(jpaClientRepository.findById(99L)).thenReturn(Optional.of(client));

        service().deleteClient(99L);

        verify(jpaClientRepository).findById(99L);
        verify(jpaClientRepository).delete(client);
        verify(events).publishClientDeleted(client);
        verifyNoMoreInteractions(events);
        verifyNoInteractions(mapper, idempotencyService);
    }
}
