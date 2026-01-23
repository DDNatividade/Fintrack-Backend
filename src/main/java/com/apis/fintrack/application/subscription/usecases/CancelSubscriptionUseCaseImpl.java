package com.apis.fintrack.application.subscription.usecases;

import com.apis.fintrack.domain.subscription.model.Subscription;
import com.apis.fintrack.domain.subscription.port.input.CancelSubscriptionUseCase;
import com.apis.fintrack.domain.payment.port.output.PaymentServicePort;
import com.apis.fintrack.domain.subscription.port.output.SubscriptionNotificationPort;
import com.apis.fintrack.domain.subscription.port.output.SubscriptionRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CancelSubscriptionUseCaseImpl implements CancelSubscriptionUseCase {

    private final SubscriptionRepositoryPort subscriptionRepository;
    private final PaymentServicePort paymentService;
    private final SubscriptionNotificationPort notificationPort;

    public CancelSubscriptionUseCaseImpl(SubscriptionRepositoryPort subscriptionRepository,
                                         PaymentServicePort paymentService,
                                         SubscriptionNotificationPort notificationPort) {
        this.subscriptionRepository = subscriptionRepository;
        this.paymentService = paymentService;
        this.notificationPort = notificationPort;
    }

    @Override
    public void cancel(Long subscriptionId) {
        var maybe = subscriptionRepository.findAll(org.springframework.data.domain.PageRequest.of(0,50))
                .stream().filter(s -> s.getId() != null && s.getId().getValue().equals(subscriptionId)).findFirst();
        if (maybe.isEmpty()) throw new IllegalArgumentException("Subscription not found");

        Subscription subscription = maybe.get();
        var pending = paymentService.findPendingPayments(subscription);
        if (pending != null && !pending.isEmpty()) {
            throw new IllegalStateException("Subscription has pending payments and cannot be canceled");
        }

        subscription.deactivateSubscription();
        subscriptionRepository.save(subscription);

        notificationPort.notifyCancellation(subscription, "User requested cancellation");
    }
}

