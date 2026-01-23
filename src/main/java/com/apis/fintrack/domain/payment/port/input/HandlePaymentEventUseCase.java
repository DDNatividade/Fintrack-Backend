package com.apis.fintrack.domain.payment.port.input;

import com.apis.fintrack.domain.payment.model.PaymentEvent;
import com.apis.fintrack.domain.shared.model.Money;
import com.apis.fintrack.domain.subscription.model.SubscriptionId;
import com.apis.fintrack.domain.user.model.UserId;

import java.time.Instant;

/**
 * Input port (primary) that represents how the application handles payment-related
 * events coming from external systems (webhooks, polling, etc.).
 *
 * This port is provider-agnostic and uses domain types only. Implementations live
 * in the application layer and orchestrate domain logic (e.g. update subscription
 * state, record payments, trigger notifications).
 */
public interface HandlePaymentEventUseCase {

    /**
     * Immutable command that carries the information needed to handle a payment event.
     *
     * Notes:
     * - Either subscriptionId or userId can be provided depending on the integration; at least
     *   one should be present so the application can correlate the event to a domain aggregate.
     * - amount may be null for events where no amount is applicable.
     */
    record HandlePaymentEventCommand(
            String externalPaymentId,
            SubscriptionId subscriptionId,
            UserId userId,
            PaymentEvent event,
            Money amount,
            Instant eventOccurredAt
    ) {}

    /**
     * Handle a payment-related event expressed in domain terms (PaymentEvent).
     *
     * Implementations should validate the command, map it to domain operations
     * and persist any state changes via output ports. No provider-specific logic
     * or SDK types should appear in implementations of domain ports.
     *
     * @param command immutable command with event data
     */
    void handle(HandlePaymentEventCommand command);
}

