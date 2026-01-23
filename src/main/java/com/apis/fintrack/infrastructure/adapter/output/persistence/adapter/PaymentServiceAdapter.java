package com.apis.fintrack.infrastructure.adapter.output.persistence.adapter;

import com.apis.fintrack.domain.payment.model.Payment;
import com.apis.fintrack.domain.payment.model.PaymentMethod;
import com.apis.fintrack.domain.payment.model.PaymentStatus;
import com.apis.fintrack.domain.subscription.model.Subscription;
import com.apis.fintrack.domain.payment.port.output.PaymentServicePort;
import com.apis.fintrack.infrastructure.adapter.output.persistence.entity.SubscriptionJPAEntity;
import com.apis.fintrack.infrastructure.adapter.output.persistence.mapper.PaymentPersistenceMapper;
import com.apis.fintrack.infrastructure.adapter.output.persistence.mapper.SubscriptionPersistenceMapper;
import com.apis.fintrack.infrastructure.adapter.output.persistence.repository.PaymentRepository;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.Objects;

@Component
public class PaymentServiceAdapter implements PaymentServicePort {

    private final PaymentRepository paymentRepository;
    private final SubscriptionPersistenceMapper subscriptionMapper;
    private final PaymentPersistenceMapper paymentMapper;

    private PaymentServiceAdapter(PaymentRepository paymentRepository, SubscriptionPersistenceMapper subscriptionMapper, PaymentPersistenceMapper paymentMapper) {
    this.paymentRepository  = Objects.requireNonNull(paymentRepository);
        this.subscriptionMapper =Objects.requireNonNull(subscriptionMapper);
        this.paymentMapper = paymentMapper;
    }

    @Override
    public boolean validatePaymentMethod(PaymentMethod paymentMethod) {
        // Minimal implementation: accept non-null, non-empty token methods
        if (paymentMethod == null) return true;
        return paymentMethod.isEmpty();
    }

    @Override
    public List<Payment> findPendingPayments(Subscription subscription) {
        Objects.requireNonNull(subscription, "subscription cannot be null");
        SubscriptionJPAEntity subscriptionEntity =
                subscriptionMapper.toJpaEntity(subscription);

        return paymentRepository.findBySubscriptionAndStatus(subscriptionEntity, PaymentStatus.PENDING)
                .stream()
                .map(paymentMapper::toDomain)
                .toList();
    }
}

