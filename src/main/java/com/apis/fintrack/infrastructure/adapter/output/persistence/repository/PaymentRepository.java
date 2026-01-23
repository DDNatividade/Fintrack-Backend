
package com.apis.fintrack.infrastructure.adapter.output.persistence.repository;

import com.apis.fintrack.domain.payment.model.PaymentStatus;
import com.apis.fintrack.infrastructure.adapter.output.persistence.entity.PaymentJPAEntity;
import com.apis.fintrack.infrastructure.adapter.output.persistence.entity.SubscriptionJPAEntity;
import com.apis.fintrack.infrastructure.adapter.output.persistence.entity.UserJPAEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PaymentRepository extends JpaRepository<PaymentJPAEntity, Long> {

    // Buscar pagos por usuario (costumer)
    Page<PaymentJPAEntity> findByCostumer(UserJPAEntity costumer, Pageable pageable);

    // Find payments belonging to a subscription
    Page<PaymentJPAEntity> findBySubscription(SubscriptionJPAEntity subscription, Pageable pageable);

    /**
     * Finds all pending payments.
     * Uses HQL to query by PaymentStatus.PENDING.
     *
     * @return list of pending payments
     */
    @Query("SELECT p FROM PaymentJPAEntity p WHERE p.status = :status")
    List<PaymentJPAEntity> findByStatus(@Param("status") PaymentStatus status);

    /**
     * Finds all pending payments with pagination.
     * Uses HQL to query by PaymentStatus.PENDING.
     *
     * @param status the payment status to filter by
     * @param pageable pagination information
     * @return page of payments with the specified status
     */
    @Query("SELECT p FROM PaymentJPAEntity p WHERE p.status = :status")
    Page<PaymentJPAEntity> findByStatus(@Param("status") PaymentStatus status, Pageable pageable);

    /**
     * Finds pending payments for a specific user.
     * Uses HQL to query by user and status.
     *
     * @param costumer the user who made the payments
     * @param status the payment status to filter by
     * @return list of pending payments for the user
     */
    @Query("SELECT p FROM PaymentJPAEntity p WHERE p.costumer = :costumer AND p.status = :status")
    List<PaymentJPAEntity> findByCostumerAndStatus(
            @Param("costumer") UserJPAEntity costumer,
            @Param("status") PaymentStatus status
    );

    /**
     * Finds pending payments for a specific subscription.
     * Uses HQL to query by subscription and status.
     *
     * @param subscription the subscription associated with the payments
     * @param status the payment status to filter by
     * @return list of pending payments for the subscription
     */
    @Query("SELECT p FROM PaymentJPAEntity p WHERE p.subscription = :subscription AND p.status = :status")
    List<PaymentJPAEntity> findBySubscriptionAndStatus(
            @Param("subscription") SubscriptionJPAEntity subscription,
            @Param("status") PaymentStatus status
    );

}

