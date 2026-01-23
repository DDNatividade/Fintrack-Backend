package com.apis.fintrack.application.payment.usecases;

import com.apis.fintrack.application.payment.mapper.HandlePaymentCommandMapper;
import com.apis.fintrack.domain.payment.model.Payment;
import com.apis.fintrack.domain.payment.port.input.HandlePaymentEventUseCase;
import com.apis.fintrack.domain.subscription.model.Subscription;
import com.apis.fintrack.domain.subscription.port.output.SubscriptionRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@Transactional
public class HandlePaymentEventUseCaseImpl implements HandlePaymentEventUseCase {

    private final SubscriptionRepositoryPort subscriptionRepository;

    public HandlePaymentEventUseCaseImpl(SubscriptionRepositoryPort subscriptionRepository) {
        this.subscriptionRepository = subscriptionRepository;
    }

    @Override
    public void handle(HandlePaymentEventCommand command) {
        Objects.requireNonNull(command, "Command must not be null");
        Objects.requireNonNull(command.subscriptionId(), "SubscriptionId must not be null");

        Subscription subscription = subscriptionRepository
                .findById(command.subscriptionId().getValue())
                .orElseThrow(() -> new IllegalArgumentException("Subscription not found"));

        Payment payment = HandlePaymentCommandMapper.toPayment(command);

        switch (command.event()) {
            case PAYMENT_SUCCEEDED -> subscription.registerPaymentSucceeded(payment);
            case PAYMENT_FAILED -> subscription.registerPaymentFailed(payment);
            case PAYMENT_PENDING -> {
                // explícito: no hacemos nada para MVP
            }
        }

        subscriptionRepository.save(subscription);
    }
}

