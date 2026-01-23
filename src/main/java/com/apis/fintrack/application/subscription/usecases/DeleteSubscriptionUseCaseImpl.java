package com.apis.fintrack.application.subscription.usecases;

import com.apis.fintrack.domain.subscription.port.input.DeleteSubscriptionUseCase;
import com.apis.fintrack.domain.subscription.port.output.SubscriptionRepositoryPort;
import com.apis.fintrack.domain.payment.port.output.PaymentServicePort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class DeleteSubscriptionUseCaseImpl implements DeleteSubscriptionUseCase {

    private final SubscriptionRepositoryPort subscriptionRepository;
    private final PaymentServicePort paymentService;

    public DeleteSubscriptionUseCaseImpl(SubscriptionRepositoryPort subscriptionRepository,
                                         PaymentServicePort paymentService) {
        this.subscriptionRepository = subscriptionRepository;
        this.paymentService = paymentService;
    }

    @Override
    public void deleteById(Long subscriptionId) {
        // Buscar la suscripción
        var maybe = subscriptionRepository.findAll(org.springframework.data.domain.PageRequest.of(0,50))
                .stream().filter(s -> s.getId() != null && s.getId().getValue().equals(subscriptionId)).findFirst();
        if (maybe.isEmpty()) throw new IllegalArgumentException("Subscription not found");

        var subscription = maybe.get();
        // Validar pagos pendientes
        var pending = paymentService.findPendingPayments(subscription);
        if (pending != null && !pending.isEmpty()) {
            throw new IllegalStateException("Subscription has pending payments and cannot be deleted");
        }

        subscriptionRepository.deleteById(subscriptionId);
    }
}

