package com.apis.fintrack.domain.payment.model;

import java.io.Serializable;

/**
 * Domain-level payment status for subscription payments.
 *
 * <p>This enum models business intent only. It is intentionally minimal for an MVP:
 * - PENDING: payment has been created/recorded but its final result is not yet known.
 * - SUCCEEDED: payment completed successfully and funds are considered collected.
 * - FAILED: payment attempt finished without success and no funds were collected.</p>
 *
 * <p>The enum does not contain provider-specific details, retry or refund concepts,
 * nor state transition rules. Use domain services or the aggregate to express transition
 * behaviour if required.</p>
 */
public enum PaymentStatus implements Serializable {
    /**
     * The payment has been initiated or recorded but its final outcome is not yet determined.
     * This status is used when the system expects the payment to be resolved (succeeded or failed)
     * in the future.
     */
    PENDING,

    /**
     * The payment completed successfully from the business perspective.
     * The system can consider the amount as collected and apply downstream consequences
     * (e.g., mark invoices paid, advance subscription state).
     */
    SUCCEEDED,

    /**
     * The payment attempt finished without success.
     *
     * This state represents a business-level failure. It may correspond to
     * a definitive failure (e.g. cancellation) or a recoverable one (e.g. retry required),
     * but that distinction is handled outside of this enum.
     */
    FAILED;


    /**
     * True when the payment is considered collected from the business perspective.
     */
    public boolean isPaid() {
        return this == SUCCEEDED;
    }

    /**
     * True when the payment result is still pending and not yet final.
     */
    public boolean isPending() {
        return this == PENDING;
    }

    /**
     * True when the payment is in a final state (either succeeded or failed).
     * Final states indicate no further automatic resolution is implied by the status itself.
     */
    public boolean isFinal() {
        return this == SUCCEEDED || this == FAILED;
    }
}