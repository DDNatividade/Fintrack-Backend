package com.apis.fintrack.infrastructure.adapter.input.rest.mapper.stripe;

import com.apis.fintrack.domain.payment.model.PaymentStatus;
import com.apis.fintrack.domain.payment.port.output.PaymentStatusMapper;
import java.util.Locale;
import java.util.Objects;

/**
 * Infrastructure-level translator that maps Stripe webhook event types (provider-specific Strings)
 * to the domain-level {@link PaymentStatus}.
 *
 * <p>Responsibility:
 * - Translate Stripe event type strings into business-level payment statuses only.
 * - Do not contain business rules, persistence, retries, or side effects. Domain behaviour
 *   must remain in domain entities/services and application use-cases.
 *
 * <p>Notes:
 * - This mapper is intentionally Stripe-specific; additional providers should have their own
 *   mapper implementations in infrastructure that translate into the same domain enum.
 * - The mapping is explicit and small by design (MVP-focused). Unknown/unsupported event types
 *   fail fast with an {@link IllegalArgumentException}.
 */
public final class StripeEventToPaymentStatusMapper implements PaymentStatusMapper {

    // Prevent instantiation
    private StripeEventToPaymentStatusMapper() { }

    /**
     * Translate a Stripe webhook event type (e.g. "payment_intent.succeeded") into the domain
     * {@link PaymentStatus}.
     *
     * @param stripeEventType the Stripe event type string received from a webhook (must not be null/blank)
     * @return the corresponding domain {@link PaymentStatus}
     * @throws IllegalArgumentException if the input is null/blank or the event type is unsupported
     */
    public static PaymentStatus fromEventType(String stripeEventType) {
        Objects.requireNonNull(stripeEventType, "stripeEventType must not be null");
        String evt = stripeEventType.trim().toLowerCase(Locale.ROOT);
        if (evt.isEmpty()) {
            throw new IllegalArgumentException("stripeEventType must not be blank");
        }

        // Success events
        // Unknown/unsupported Stripe event type — fail fast so caller can decide how to handle
        return switch (evt) {
            case "payment_intent.succeeded", "charge.succeeded", "invoice.paid",
                 "checkout.session.completed" -> // when checkout session indicates payment completed
                    PaymentStatus.SUCCEEDED;

            // Failure events
            case "payment_intent.payment_failed", "charge.failed", "invoice.payment_failed",
                 "checkout.session.async_payment_failed" -> PaymentStatus.FAILED;

            // Pending / in-progress events
            case "payment_intent.processing", "payment_intent.requires_action",
                 "payment_intent.requires_payment_method", "payment_intent.requires_confirmation",
                 "invoice.payment_action_required", "charge.pending",
                 "checkout.session.async_payment_succeeded" -> // session succeeded but async payment flow may still require handling
                    PaymentStatus.PENDING;
            default -> throw new IllegalArgumentException("Unsupported Stripe event type: " + stripeEventType);
        };
    }
}

