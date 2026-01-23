package com.apis.fintrack.infrastructure.adapter.input.rest.mapper.stripe;

import com.apis.fintrack.domain.payment.model.PaymentEvent;
import com.apis.fintrack.domain.payment.model.PaymentStatus;
import com.apis.fintrack.domain.payment.port.input.HandlePaymentEventUseCase;
import com.apis.fintrack.domain.shared.model.Money;
import com.apis.fintrack.domain.subscription.model.SubscriptionId;
import com.apis.fintrack.domain.user.model.UserId;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.stripe.model.Event;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Instant;

@Component
public class StripeEventToCommandMapper {

    private final ObjectMapper objectMapper;

    public StripeEventToCommandMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public HandlePaymentEventUseCase.HandlePaymentEventCommand toCommand(Event event) {
        try {
            JsonNode dataObject = objectMapper.readTree(event.toJson())
                    .path("data").path("object");

            String externalPaymentId = extractExternalPaymentId(dataObject);
            SubscriptionId subscriptionId = extractSubscriptionId(dataObject);
            UserId userId = extractUserId(dataObject);
            Money amount = extractAmount(dataObject);

            PaymentStatus status = StripeEventToPaymentStatusMapper.fromEventType(event.getType());
            PaymentEvent paymentEvent = mapStatusToEvent(status);

            return new HandlePaymentEventUseCase.HandlePaymentEventCommand(
                    externalPaymentId,
                    subscriptionId,
                    userId,
                    paymentEvent,
                    amount,
                    Instant.ofEpochSecond(event.getCreated())
            );
        } catch (IOException e) {
            throw new IllegalArgumentException("Unable to parse Stripe event payload", e);
        }
    }

    private String extractExternalPaymentId(JsonNode dataObject) {
        return dataObject.hasNonNull("id") ? dataObject.path("id").asText() : null;
    }

    private SubscriptionId extractSubscriptionId(JsonNode dataObject) {
        if (dataObject.has("metadata") && dataObject.path("metadata").has("subscriptionId")) {
            String s = dataObject.path("metadata").path("subscriptionId").asText(null);
            if (s != null && !s.isBlank()) {
                try {
                    return SubscriptionId.of(Long.parseLong(s));
                } catch (NumberFormatException ignored) {}
            }
        }
        return null;
    }

    private UserId extractUserId(JsonNode dataObject) {
        return null; // opcional
    }

    private Money extractAmount(JsonNode dataObject) {
        return null; // opcional
    }

    private PaymentEvent mapStatusToEvent(PaymentStatus status) {
        return switch (status) {
            case SUCCEEDED -> PaymentEvent.PAYMENT_SUCCEEDED;
            case FAILED -> PaymentEvent.PAYMENT_FAILED;
            case PENDING -> PaymentEvent.PAYMENT_PENDING;
        };
    }
}
