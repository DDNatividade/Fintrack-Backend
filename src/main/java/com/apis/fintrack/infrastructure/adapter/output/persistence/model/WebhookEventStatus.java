package com.apis.fintrack.infrastructure.adapter.output.persistence.model;

/**
 * Status of a persisted webhook event used for audit, idempotency and retry.
 *
 * This enum is infrastructure-specific and describes the processing lifecycle
 * of an incoming webhook event as stored by the persistence adapter. Domain
 * objects must remain provider-agnostic and should not depend on this enum.
 */
public enum WebhookEventStatus {
    /** Event has been received but not processed by the application. */
    RECEIVED,

    /** Event was processed successfully and no further action is required. */
    PROCESSED,

    /** Event processing failed and may be retried or inspected manually. */
    FAILED
}

