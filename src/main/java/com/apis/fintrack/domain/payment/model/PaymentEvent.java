package com.apis.fintrack.domain.payment.model;

/**
 * Domain-level events related to payments and subscription lifecycle.
 *
 * These events describe business-relevant occurrences (the ubiquitous language)
 * and are intentionally provider-agnostic so they can be produced by any
 * payment integration (Stripe, PayPal, bank gateway, etc.).
 *
 * Use these events in application services, domain event handlers or to map
 * provider-specific webhooks to domain semantics.
 */
public enum PaymentEvent {

    /**
     * The payment has been successfully completed and the business
     * can consider the subscription period as paid.
     */
    PAYMENT_SUCCEEDED,

    /**
     * The payment failed and the subscription cannot be renewed
     * unless a new attempt succeeds.
     */
    PAYMENT_FAILED,

    /**
     * The payment is pending customer or external action and
     * cannot yet be considered successful or failed.
     */
    PAYMENT_PENDING,
}
