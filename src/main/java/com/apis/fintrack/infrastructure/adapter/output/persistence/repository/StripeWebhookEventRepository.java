package com.apis.fintrack.infrastructure.adapter.output.persistence.repository;

import com.apis.fintrack.infrastructure.adapter.output.persistence.entity.StripeWebhookJPAEntity;
import com.apis.fintrack.infrastructure.adapter.output.persistence.model.WebhookEventStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

/**
 * Spring Data repository for persisted webhook events. This repository is part of the infrastructure
 * persistence adapter and is responsible for storing incoming provider events for audit, idempotency
 * and retry. Domain layer must not depend on this interface.
 */
@Repository
public interface StripeWebhookEventRepository extends JpaRepository<StripeWebhookJPAEntity, Long> {

    /**
     * Check whether an event with the given provider event id has already been stored.
     * This is useful to implement idempotency when receiving webhooks.
     *
     * @param eventId provider event id
     * @return true if an event with the provided id exists
     */
    boolean existsByEventId(String eventId);

    /**
     * Find events that are not yet processed (e.g. RECEIVED or FAILED) to allow for retrying.
     *
     * @param status the status to filter by (commonly RECEIVED or FAILED)
     * @return list of matching events ordered by received timestamp ascending
     */
    @Query("select e from StripeWebhookJPAEntity e where e.status = :status order by e.receivedAt asc")
    List<StripeWebhookJPAEntity> findByStatusOrderByReceivedAtAsc(@Param("status") WebhookEventStatus status);

    /**
     * Find a persisted event by provider event id.
     *
     * @param eventId provider event id
     * @return optional persisted event entity
     */
    Optional<StripeWebhookJPAEntity> findByEventId(String eventId);

    /**
     * Find events matching any of the provided statuses ordered by received timestamp ascending.
     * This query is useful to implement batch retry workers or to inspect unprocessed events.
     *
     * @param statuses list of statuses to include
     * @return list of matching events ordered by receivedAt
     */
    @Query("select e from StripeWebhookJPAEntity e where e.status in :statuses order by e.receivedAt asc")
    List<StripeWebhookJPAEntity> findByStatusInOrderByReceivedAtAsc(@Param("statuses") java.util.List<WebhookEventStatus> statuses);

    /**
     * Convenience method that returns events that are considered unprocessed for retry/audit.
     * By default this includes RECEIVED and FAILED statuses.
     *
     * Note: this default method is provided for convenience and will execute the query above.
     * The repository is infrastructure-only; domain logic about retries must live outside.
     *
     * @return list of unprocessed webhook events
     */
    default List<StripeWebhookJPAEntity> findUnprocessedEvents() {
        return findByStatusInOrderByReceivedAtAsc(java.util.List.of(WebhookEventStatus.RECEIVED, WebhookEventStatus.FAILED));
    }
}
