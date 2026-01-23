package com.apis.fintrack.application.subscription.usecases;

import com.apis.fintrack.domain.subscription.model.Subscription;
import com.apis.fintrack.domain.subscription.port.input.UpdateSubscriptionUseCase;
import com.apis.fintrack.domain.payment.port.output.PaymentServicePort;
import com.apis.fintrack.domain.subscription.port.output.SubscriptionRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UpdateSubscriptionUseCaseImpl implements UpdateSubscriptionUseCase {

    private final SubscriptionRepositoryPort subscriptionRepository;
    private final PaymentServicePort paymentService;

    public UpdateSubscriptionUseCaseImpl(SubscriptionRepositoryPort subscriptionRepository,
                                         PaymentServicePort paymentService) {
        this.subscriptionRepository = subscriptionRepository;
        this.paymentService = paymentService;
    }

    @Override
    public Subscription update(UpdateSubscriptionCommand command) {
        // Buscar suscripción por usuario o id - reusar FindSubscriptionUseCaseImpl no para evitar dependencia circular
        var maybeSubscription = subscriptionRepository.findAll(org.springframework.data.domain.PageRequest.of(0,50))
                .stream()
                .filter(s -> s.getId() != null && s.getId().getValue().equals(command.subscriptionId()))
                .findFirst();

        if (maybeSubscription.isEmpty()) {
            throw new IllegalArgumentException("Subscription not found");
        }

        Subscription subscription = maybeSubscription.get();

        // Aplicar cambios delegando a la entidad
        command.newType().ifPresent(subscription::changeType);
        command.newPaymentMethod().ifPresent(pm -> {
            if (paymentService.validatePaymentMethod(pm)) {
                throw new IllegalArgumentException("Payment method invalid");
            }
            subscription.changePaymentMethod(pm);
        });
        command.reactivate().ifPresent(reactivate -> {
            if (reactivate) subscription.activateSubscription();
        });

        return subscriptionRepository.save(subscription);
    }
}

