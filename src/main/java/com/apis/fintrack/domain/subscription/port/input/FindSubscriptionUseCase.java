package com.apis.fintrack.domain.subscription.port.input;

import com.apis.fintrack.domain.subscription.model.Subscription;
import com.apis.fintrack.domain.user.model.UserId;

import java.util.Optional;

/**
 * Input port para buscar suscripciones.
 */
public interface FindSubscriptionUseCase {

    Optional<Subscription> findById(Long subscriptionId);

    Optional<Subscription> findByUserId(UserId userId);
}

