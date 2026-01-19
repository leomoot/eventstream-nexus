package nl.leomoot.eventstreamnexus.services;

import nl.leomoot.eventstreamnexus.domain.model.ClientEntity;

/**
 * Publishes lifecycle events for {@link ClientEntity} changes.
 */
public interface ClientEventPublisher {

    /**
     * Nnotification when a client is created.
     */
    void publishClientCreated(ClientEntity entity);

    /**
     * Notification when a client is updated.
     */
    void publishClientUpdated(ClientEntity entity);

    /**
     * Notification when a client is deleted.
     */
    void publishClientDeleted(ClientEntity entity);
}
