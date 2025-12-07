package com.apis.fintrack.infrastructure.adapter.output.persistence.repository;

import com.apis.fintrack.infrastructure.adapter.output.persistence.entity.PaymentJPAEntity;
import com.apis.fintrack.infrastructure.adapter.output.persistence.entity.SubscriptionJPAEntity;
import com.apis.fintrack.infrastructure.adapter.output.persistence.entity.UserJPAEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<PaymentJPAEntity, Long> {

    // Buscar pagos por usuario (costumer)
    Page<PaymentJPAEntity> findByCostumer(UserJPAEntity costumer, Pageable pageable);

    // Find payments belonging to a subscription
    Page<PaymentJPAEntity> findBySubscription(SubscriptionJPAEntity subscription, Pageable pageable);
}

