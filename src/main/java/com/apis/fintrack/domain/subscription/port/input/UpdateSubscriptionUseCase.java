package com.apis.fintrack.domain.subscription.port.input;

import com.apis.fintrack.domain.payment.model.PaymentMethod;
import com.apis.fintrack.domain.subscription.model.Subscription;
import com.apis.fintrack.domain.subscription.model.SubscriptionType;

import java.util.Optional;

/**
 * Input port para actualizar suscripciones.
 */
public interface UpdateSubscriptionUseCase {

    record UpdateSubscriptionCommand(
            Long subscriptionId,
            Optional<SubscriptionType> newType,
            Optional<PaymentMethod> newPaymentMethod,
            Optional<Boolean> reactivate
    ) {}

    Subscription update(UpdateSubscriptionCommand command);
}

