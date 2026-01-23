package com.apis.fintrack.domain.payment.port.output;

import com.apis.fintrack.domain.payment.model.PaymentStatus;

/**
 * Domain-agnostic contract for mapping external/payment provider events into
 * domain-level {@link PaymentStatus}.
 *
 * <p>Implementations of this interface translate provider-specific event
 * identifiers (strings, codes, etc.) into the domain's canonical payment status.
 * This abstraction allows the domain layer to remain independent of any specific
 * payment provider.</p>
 *
 * <p>Responsibilities:
 * <ul>
 *     <li>Convert an external event type into a {@link PaymentStatus}.</li>
 *     <li>Fail fast or throw an exception for unsupported event types.</li>
 *     <li>Do not implement business rules, persistence, retries, or side effects.</li>
 * </ul>
 * </p>
 */
public interface PaymentStatusMapper {

    /**
     * Translate a provider-specific payment event into a domain {@link PaymentStatus}.
     *
     * @param eventType the provider-specific event identifier (must not be null or blank)
     * @return the corresponding domain {@link PaymentStatus}
     * @throws IllegalArgumentException if the input is null, blank, or not supported by this mapper
     */
    static PaymentStatus fromEventType(String eventType) {
        return null;
    }
}
