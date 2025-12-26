package com.apis.fintrack.infrastructure.adapter.output.persistence.mapper;

import com.apis.fintrack.domain.payment.model.Payment;
import com.apis.fintrack.domain.payment.model.PaymentMethod;
import com.apis.fintrack.domain.subscription.model.Subscription;
import com.apis.fintrack.domain.subscription.model.SubscriptionDate;
import com.apis.fintrack.domain.subscription.model.SubscriptionId;

import com.apis.fintrack.domain.user.model.UserId;
import com.apis.fintrack.infrastructure.adapter.output.persistence.entity.PaymentJPAEntity;
import com.apis.fintrack.infrastructure.adapter.output.persistence.entity.SubscriptionJPAEntity;
import com.apis.fintrack.infrastructure.adapter.output.persistence.entity.UserJPAEntity;
import com.apis.fintrack.infrastructure.adapter.output.persistence.repository.UserRepository;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Mapper for converting between Subscription (domain) and SubscriptionJPAEntity (JPA).
 *
 * Responsibilities:
 * - Convert JPA entities to domain entities
 * - Convert domain entities to JPA entities
 * - Update existing JPA entities with domain data
 */
@Component
public class SubscriptionPersistenceMapper {

    private final UserRepository userRepository;
    private final PaymentPersistenceMapper paymentMapper;

    public SubscriptionPersistenceMapper(UserRepository userRepository, PaymentPersistenceMapper paymentMapper) {
        this.userRepository = userRepository;
        this.paymentMapper = paymentMapper;
    }

    /**
     * Converts a JPA entity to a domain entity.
     *
     * @param jpaEntity the JPA entity
     * @return the domain entity
     */
    public Subscription toDomain(SubscriptionJPAEntity jpaEntity) {
        if (jpaEntity == null) {
            return null;
        }

        // Map payments
        List<Payment> payments = new ArrayList<>();
        if (jpaEntity.getPayments() != null) {
            payments = jpaEntity.getPayments().stream()
                .map(paymentMapper::toDomain)
                .collect(Collectors.toList());
        }

        // Map payment method
        PaymentMethod paymentMethod = null;
        if (jpaEntity.getPaymentMethodType() != null) {
            paymentMethod = PaymentMethod.of(
                jpaEntity.getPaymentMethodType(),
                jpaEntity.getPaymentExternalToken(),
                jpaEntity.getPaymentLastFourDigits()
            );
        }

        return new Subscription(
            SubscriptionId.of(jpaEntity.getId()),
            SubscriptionDate.fromStorage(jpaEntity.getSubscriptionDate().toLocalDate()),
            jpaEntity.getStatus(),
            UserId.of(jpaEntity.getCostumer().getUserId()),
            jpaEntity.isActive(),
            paymentMethod,
            payments
        );
    }

    /**
     * Converts a domain entity to a JPA entity.
     *
     * @param domainSubscription the domain entity
     * @return the JPA entity
     */
    public SubscriptionJPAEntity toJpaEntity(Subscription domainSubscription) {
        if (domainSubscription == null) {
            return null;
        }

        SubscriptionJPAEntity jpaEntity = new SubscriptionJPAEntity();

        // Only assign ID if it exists (not new)
        if (!domainSubscription.getId().isEmpty()) {
            jpaEntity.setId(domainSubscription.getId().getValue());
        }

        jpaEntity.setSubscriptionDate(domainSubscription.getSubscriptionDate().getValue().atStartOfDay());
        jpaEntity.setStatus(domainSubscription.getType());
        jpaEntity.setActive(domainSubscription.isActive());

        // Map payment method
        if (domainSubscription.getPaymentMethod() != null) {
            PaymentMethod pm = domainSubscription.getPaymentMethod();
            jpaEntity.setPaymentMethodType(pm.getType());
            jpaEntity.setPaymentExternalToken(pm.getExternalToken());
            jpaEntity.setPaymentLastFourDigits(pm.getLastFourDigits());
        }

        // Find the user
        UserJPAEntity userEntity = userRepository.searchById(domainSubscription.getUserId().getValue())
            .orElseThrow(() -> new IllegalArgumentException(
                "User does not exist with ID: " + domainSubscription.getUserId().getValue()
            ));
        jpaEntity.setCostumer(userEntity);

        // Map payments
        if (domainSubscription.getPayments() != null && !domainSubscription.getPayments().isEmpty()) {
            List<PaymentJPAEntity> paymentEntities = domainSubscription.getPayments().stream()
                .map(paymentMapper::toJpaEntity)
                .collect(Collectors.toList());
            jpaEntity.setPayments(paymentEntities);
        }

        return jpaEntity;
    }

    /**
     * Updates an existing JPA entity with domain data.
     *
     * @param domainSubscription the domain entity with new data
     * @param jpaEntity the JPA entity to update
     */
    public void updateJpaEntity(Subscription domainSubscription, SubscriptionJPAEntity jpaEntity) {
        if (domainSubscription == null || jpaEntity == null) {
            return;
        }

        jpaEntity.setSubscriptionDate(domainSubscription.getSubscriptionDate().getValue().atStartOfDay());
        jpaEntity.setStatus(domainSubscription.getType());
        jpaEntity.setActive(domainSubscription.isActive());

        // Update payment method
        if (domainSubscription.getPaymentMethod() != null) {
            PaymentMethod pm = domainSubscription.getPaymentMethod();
            jpaEntity.setPaymentMethodType(pm.getType());
            jpaEntity.setPaymentExternalToken(pm.getExternalToken());
            jpaEntity.setPaymentLastFourDigits(pm.getLastFourDigits());
        }
    }
}

