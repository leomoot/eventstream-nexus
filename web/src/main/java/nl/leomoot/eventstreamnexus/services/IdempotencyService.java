package nl.leomoot.eventstreamnexus.services;

import java.util.UUID;

/**
 * Guards command handlers with idempotency semantics keyed by a unique token.
 */
public interface IdempotencyService {

    /**
     * Execute the supplied {@link IdempotentOperation} once per idempotency key.
     *
     * @param key unique idempotency token
     * @param operation business action to run at most once
     * @return result produced by the operation
     */
    <T> T execute(UUID key, IdempotentOperation<T> operation);

    /**
     * Action that can be executed under idempotency protection.
     *
     * @param <T> result type
     */
    interface IdempotentOperation<T> {
        /**
         * @return result of the guarded operation
         */
        T run();
    }
}
