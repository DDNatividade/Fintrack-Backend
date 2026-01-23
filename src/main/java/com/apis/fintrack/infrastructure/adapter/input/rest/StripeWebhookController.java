package com.apis.fintrack.infrastructure.adapter.input.rest;

import com.apis.fintrack.domain.payment.port.output.WebhooksPersistencePort;
import com.apis.fintrack.domain.payment.port.input.HandlePaymentEventUseCase;
import com.apis.fintrack.infrastructure.adapter.input.rest.mapper.stripe.StripeEventToCommandMapper;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.net.Webhook;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/webhooks/stripe")
public class StripeWebhookController {

    private static final Logger log = LoggerFactory.getLogger(StripeWebhookController.class);

    private final HandlePaymentEventUseCase handlePaymentEventUseCase;
    private final StripeEventToCommandMapper eventMapper;
    private final String stripeWebhookSecret;
    private final WebhooksPersistencePort persistencePort;
    private final Counter receivedCounter;
    private final Counter processedCounter;
    private final Counter duplicateCounter;
    private final Counter failedCounter;

    public StripeWebhookController(
            HandlePaymentEventUseCase handlePaymentEventUseCase,
            StripeEventToCommandMapper eventMapper,
            WebhooksPersistencePort persistencePort,
            MeterRegistry meterRegistry,
            @Value("${stripe.webhook.secret:}") String stripeWebhookSecret) {
        this.handlePaymentEventUseCase = handlePaymentEventUseCase;
        this.eventMapper = eventMapper;
        this.persistencePort = persistencePort;
        this.stripeWebhookSecret = stripeWebhookSecret;

        this.receivedCounter = meterRegistry.counter("webhook.received.total");
        this.processedCounter = meterRegistry.counter("webhook.processed.total");
        this.duplicateCounter = meterRegistry.counter("webhook.duplicate.total");
        this.failedCounter = meterRegistry.counter("webhook.failed.total");
    }

    @PostMapping
    public ResponseEntity<String> handleWebhook(@RequestBody String payload,
                                                @RequestHeader(name = "Stripe-Signature", required = false) String sigHeader) {
        // Count every incoming request as received (could be moved to after signature validation if desired)
        receivedCounter.increment();

        try {
            if (stripeWebhookSecret == null || stripeWebhookSecret.isBlank()) {
                throw new IllegalStateException("Stripe webhook secret not configured");
            }

            // Validate signature and parse event
            Event event = Webhook.constructEvent(payload, sigHeader, stripeWebhookSecret);
            String eventId = event.getId();
            String eventType = event.getType();

            log.info("Received Stripe webhook event id={}, type={}", eventId, eventType);

            // Idempotency: check if event already exists
            if (persistencePort.existsByEventId(eventId)) {
                duplicateCounter.increment();
                log.warn("Duplicate webhook event received, id={}, type={}, skipping processing", eventId, eventType);
                return ResponseEntity.ok("Duplicate event");
            }

            // Persist event as RECEIVED for audit and idempotency
            persistencePort.saveReceivedEvent(eventId, eventType, payload);
            log.info("Persisted webhook event id={}, type={}", eventId, eventType);

            // Map to domain command
            HandlePaymentEventUseCase.HandlePaymentEventCommand command = eventMapper.toCommand(event);

            // Delegate to application
            handlePaymentEventUseCase.handle(command);

            // Mark processed (best-effort)
            persistencePort.markProcessed(eventId);
            processedCounter.increment();

            log.info("Successfully processed webhook event id={}, type={}", eventId, eventType);

            return ResponseEntity.ok("Event processed");

        } catch (SignatureVerificationException e) {
            failedCounter.increment();
            log.warn("Invalid Stripe signature: {}", e.getMessage());
            return ResponseEntity.badRequest().body("Invalid Stripe signature");
        } catch (IllegalArgumentException | IllegalStateException e) {
            failedCounter.increment();
            log.warn("Bad request processing Stripe webhook: {}", e.getMessage());
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            failedCounter.increment();
            log.error("Error processing Stripe webhook", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Internal error processing event");
        }
    }
}
