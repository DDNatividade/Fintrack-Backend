package com.apis.fintrack.application.subscription.usecases;

import com.apis.fintrack.domain.subscription.model.Subscription;
import com.apis.fintrack.domain.subscription.port.input.CreateSubscriptionUseCase;
import com.apis.fintrack.domain.payment.port.output.PaymentServicePort;
import com.apis.fintrack.domain.subscription.port.output.SubscriptionRepositoryPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementación del caso de uso de creación de suscripción.
 */
@Service
@Transactional
public class CreateSubscriptionUseCaseImpl implements CreateSubscriptionUseCase {

    private final SubscriptionRepositoryPort subscriptionRepository;
    private final PaymentServicePort paymentService;

    public CreateSubscriptionUseCaseImpl(SubscriptionRepositoryPort subscriptionRepository,
                                         PaymentServicePort paymentService) {
        this.subscriptionRepository = subscriptionRepository;
        this.paymentService = paymentService;
    }

    @Override
    public Subscription create(CreateSubscriptionCommand command) {
        var existing = subscriptionRepository.findById(command.userId().getValue());
        // Validar que el usuario no tenga suscripción activa
        if (existing.isPresent() && existing.get().isActive()) {
            throw new IllegalStateException("User already has an active subscription");
        }

        // Validar método de pago externo
        if (paymentService.validatePaymentMethod(command.paymentMethod())) {
            throw new IllegalArgumentException("Payment method is not valid");
        }

        // Crear la suscripción en dominio
        Subscription subscription = Subscription.create(
                com.apis.fintrack.domain.subscription.model.SubscriptionDate.now(),
                command.type(),
                command.userId(),
                command.paymentMethod()
        );

        // Asociar userId
        subscription = new Subscription(
                subscription.getId(),
                subscription.getSubscriptionDate(),
                subscription.getType(),
                command.userId(),
                true,
                subscription.getPaymentMethod(),
                subscription.getPayments()
        );

        return subscriptionRepository.save(subscription);
    }
}

