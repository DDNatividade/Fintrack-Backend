package com.apis.fintrack.domain.payment.port.output;

/**
 * Port (output) for persisting incoming webhook events for audit and idempotency.
 *
 * This interface belongs to the domain/application boundary and is implemented by
 * an infrastructure adapter that performs the actual database operations.
 */
public interface WebhooksPersistencePort {

    /**
     * Check whether an event with the given provider event id has already been stored.
     *
     * @param eventId provider event id
     * @return true if an event with the provided id exists
     */
    boolean existsByEventId(String eventId);

    /**
     * Persist a newly received event with status RECEIVED for audit and idempotency.
     * Implementations should handle race conditions (unique constraint) gracefully.
     *
     * @param eventId provider event id
     * @param eventType provider event type
     * @param payload raw JSON payload
     */
    void saveReceivedEvent(String eventId, String eventType, String payload);

    /**
     * Mark the persisted event as processed (status = PROCESSED).
     * Implementations should update processed timestamp and any related audit fields.
     *
     * @param eventId provider event id
     */
    void markProcessed(String eventId);
}

