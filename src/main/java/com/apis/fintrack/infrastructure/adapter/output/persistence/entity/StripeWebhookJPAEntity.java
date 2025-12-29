package com.apis.fintrack.infrastructure.adapter.output.persistence.entity;

import com.apis.fintrack.infrastructure.adapter.output.persistence.model.WebhookEventStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

/**
 * JPA entity that stores incoming webhook events from Stripe for audit, idempotency and retry.
 *
 * This entity belongs to the infrastructure layer (persistence adapter). It should not leak
 * into the domain model. The domain should remain provider-agnostic and perform business logic
 * using domain types and ports.
 */
@Getter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Table(name = "stripe_webhook_event", indexes = {
        @Index(name = "idx_stripe_event_event_id", columnList = "event_id"),
        @Index(name = "idx_stripe_event_status", columnList = "status")
})
public class StripeWebhookJPAEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Provider event id (e.g. Stripe's event id) */
    @Column(name = "event_id", nullable = false, unique = true, length = 128)
    private String eventId;

    /** Provider event type string (e.g. invoice.paid) */
    @Column(name = "event_type", nullable = false, length = 128)
    private String eventType;

    /** Raw JSON payload received from the provider for audit and retry. */
    @Lob
    @Column(name = "payload", nullable = false)
    private String payload;

    /** Processing status for this persisted event. */
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 32)
    private WebhookEventStatus status;

    /** Timestamp when the event was received by this service. */
    @Column(name = "received_at", nullable = false)
    private Instant receivedAt;

}

