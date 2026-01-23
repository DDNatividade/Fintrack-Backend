package com.apis.fintrack.infrastructure.adapter.output.persistence.mapper;

import com.apis.fintrack.domain.payment.model.Payment;
import com.apis.fintrack.domain.payment.model.PaymentDate;
import com.apis.fintrack.domain.payment.model.PaymentId;
import com.apis.fintrack.domain.shared.model.Money;
import com.apis.fintrack.domain.subscription.model.SubscriptionId;
import com.apis.fintrack.domain.user.model.UserId;
import com.apis.fintrack.infrastructure.adapter.output.persistence.entity.PaymentJPAEntity;
import com.apis.fintrack.infrastructure.adapter.output.persistence.entity.SubscriptionJPAEntity;
import com.apis.fintrack.infrastructure.adapter.output.persistence.entity.UserJPAEntity;
import com.apis.fintrack.infrastructure.adapter.output.persistence.repository.SubscriptionRepository;
import com.apis.fintrack.infrastructure.adapter.output.persistence.repository.UserRepository;
import org.springframework.stereotype.Component;

/**
 * Mapper for converting between Payment (domain) and PaymentJPAEntity (JPA).
 *
 * Responsibilities:
 * - Convert JPA entities to domain entities
 * - Convert domain entities to JPA entities
 * - Update existing JPA entities with domain data
 */
@Component
public class PaymentPersistenceMapper {

    private final UserRepository userRepository;
    private final SubscriptionRepository subscriptionRepository;

    public PaymentPersistenceMapper(UserRepository userRepository, SubscriptionRepository subscriptionRepository) {
        this.userRepository = userRepository;
        this.subscriptionRepository = subscriptionRepository;
    }

    /**
     * Converts a JPA entity to a domain entity.
     *
     * @param jpaEntity the JPA entity
     * @return the domain entity
     */
    public Payment toDomain(PaymentJPAEntity jpaEntity) {
        if (jpaEntity == null) {
            return null;
        }

        return  Payment.create(
            PaymentDate.fromStorage(jpaEntity.getPaymentDate().toLocalDate()),
            UserId.of(jpaEntity.getCostumer().getUserId()),
            SubscriptionId.of(jpaEntity.getSubscription().getId()),
            Money.of(jpaEntity.getSubscription().getPrice())
        );
    }

    /**
     * Converts a domain entity to a JPA entity.
     *
     * @param domainPayment the domain entity
     * @return the JPA entity
     */
    public PaymentJPAEntity toJpaEntity(Payment domainPayment) {
        if (domainPayment == null) {
            return null;
        }

        PaymentJPAEntity jpaEntity = new PaymentJPAEntity();

        // Only assign ID if it exists (not new)
        if (!domainPayment.getId().isEmpty()) {
            jpaEntity.setId(domainPayment.getId().getValue());
        }

        jpaEntity.setPaymentDate(domainPayment.getPaymentDate().getValue().atStartOfDay());

        // Find the user
        UserJPAEntity userEntity = userRepository.searchById(domainPayment.getUserId().getValue())
            .orElseThrow(() -> new IllegalArgumentException(
                "User does not exist with ID: " + domainPayment.getUserId().getValue()
            ));
        jpaEntity.setCostumer(userEntity);

        // Find the subscription
        SubscriptionJPAEntity subscriptionEntity = subscriptionRepository.findById(domainPayment.getSubscriptionId().getValue())
            .orElseThrow(() -> new IllegalArgumentException(
                "Subscription does not exist with ID: " + domainPayment.getSubscriptionId().getValue()
            ));
        jpaEntity.setSubscription(subscriptionEntity);

        return jpaEntity;
    }

    /**
     * Updates an existing JPA entity with domain data.
     *
     * @param domainPayment the domain entity with new data
     * @param jpaEntity the JPA entity to update
     */
    public void updateJpaEntity(Payment domainPayment, PaymentJPAEntity jpaEntity) {
        if (domainPayment == null || jpaEntity == null) {
            return;
        }

        jpaEntity.setPaymentDate(domainPayment.getPaymentDate().getValue().atStartOfDay());
    }
}

