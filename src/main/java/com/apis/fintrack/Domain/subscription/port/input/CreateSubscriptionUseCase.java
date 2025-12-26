package com.apis.fintrack.domain.subscription.port.input;

import com.apis.fintrack.domain.payment.model.PaymentMethod;
import com.apis.fintrack.domain.subscription.model.Subscription;
import com.apis.fintrack.domain.user.model.UserId;

/**
 * Input port para crear una suscripción.
 */
public interface CreateSubscriptionUseCase {

    record CreateSubscriptionCommand(
            UserId userId,
            com.apis.fintrack.domain.subscription.model.SubscriptionType type,
            PaymentMethod paymentMethod
    ) {}

    Subscription create(CreateSubscriptionCommand command);
}

