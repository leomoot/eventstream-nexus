package nl.leomoot.eventstreamnexus.services.impl;

import java.time.Instant;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import nl.leomoot.eventstreamnexus.domain.model.ClientEntity;
import nl.leomoot.eventstreamnexus.events.ClientCreatedEvent;
import nl.leomoot.eventstreamnexus.events.ClientDeletedEvent;
import nl.leomoot.eventstreamnexus.events.ClientUpdatedEvent;
import nl.leomoot.eventstreamnexus.services.ClientEventPublisher;

/**
 * Kafka-backed implementation of {@link ClientEventPublisher}, serializing Avro events.
 */
@Service
public class ClientEventPublisherImpl implements ClientEventPublisher {

    private final KafkaTemplate<String, Object> template;

    /**
     * @param template configured Kafka template for sending client events
     */
    public ClientEventPublisherImpl(KafkaTemplate<String, Object> template) {
        this.template = template;
    }

    /**
     * Publish the client creation payload to the `client-created` topic.
     */
    @Override
    public void publishClientCreated(ClientEntity entity) {
        var event = ClientCreatedEvent.newBuilder()
                .setId(entity.getId())
                .setName(entity.getName())
                .setEmail(entity.getEmail())
                .setCreatedAt(entity.getCreatedAt().toString())
                .build();

        template.send("client-created", entity.getId().toString(), event);
    }

    /**
     * Publish the client update payload to the `client-updated` topic.
     */
    @Override
    public void publishClientUpdated(ClientEntity entity) {
        var event = ClientUpdatedEvent.newBuilder()
                .setId(entity.getId())
                .setName(entity.getName())
                .setEmail(entity.getEmail())
                .setUpdatedAt(Instant.now().toString())
                .build();

        template.send("client-updated", entity.getId().toString(), event);
    }

    /**
     * Publish the client deletion payload to the `client-deleted` topic.
     */
    @Override
    public void publishClientDeleted(ClientEntity entity) {
        var event = ClientDeletedEvent.newBuilder()
                .setId(entity.getId())
                .setDeletedAt(Instant.now().toString())
                .build();

        template.send("client-deleted", entity.getId().toString(), event);
    }
}
