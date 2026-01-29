package nl.leomoot.eventstreamnexus.infrastructure.messaging;


import java.time.Instant;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import nl.leomoot.eventstreamnexus.domain.model.ClientEntity;
import nl.leomoot.eventstreamnexus.events.ClientCreatedEvent;
import nl.leomoot.eventstreamnexus.events.ClientDeletedEvent;
import nl.leomoot.eventstreamnexus.events.ClientUpdatedEvent;

/**
 * Kafka-backed implementation of {@link ClientEventPublisher}, serializing Avro events.
 */
@Service
public class ClientEventPublisher  {

    private static final String TOPIC_CLIENT_CREATED = "client-created";
    private static final String TOPIC_CLIENT_UPDATED = "client-updated";
    private static final String TOPIC_CLIENT_DELETED = "client-deleted";

    private final KafkaTemplate<String, Object> template;

    /**
     * @param template configured Kafka template for sending client events
     */
    public ClientEventPublisher(KafkaTemplate<String, Object> template) {
        this.template = template;
    }

    /**
     * Publish the client creation payload to the `client-created` topic.
     */
    public void publishClientCreated(ClientEntity entity) {
        var event = ClientCreatedEvent.newBuilder()
                .setId(entity.getId())
                .setName(entity.getName())
                .setEmail(entity.getEmail())
                .setCreatedAt(entity.getCreatedAt().toString())
                .build();

        template.send(TOPIC_CLIENT_CREATED, entity.getId().toString(), event);
    }

    /**
     * Publish the client update payload to the `client-updated` topic.
     */
    public void publishClientUpdated(ClientEntity entity) {
        var event = ClientUpdatedEvent.newBuilder()
                .setId(entity.getId())
                .setName(entity.getName())
                .setEmail(entity.getEmail())
                .setUpdatedAt(Instant.now().toString())
                .build();

        template.send(TOPIC_CLIENT_UPDATED, entity.getId().toString(), event);
    }

    /**
     * Publish the client deletion payload to the `client-deleted` topic.
     */
    public void publishClientDeleted(ClientEntity entity) {
        var event = ClientDeletedEvent.newBuilder()
                .setId(entity.getId())
                .setDeletedAt(Instant.now().toString())
                .build();

        template.send(TOPIC_CLIENT_DELETED, entity.getId().toString(), event);
    }
}
