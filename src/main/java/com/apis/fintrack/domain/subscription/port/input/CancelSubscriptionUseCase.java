package com.apis.fintrack.domain.subscription.port.input;

/**
 * Input port para cancelar suscripciones.
 */
public interface CancelSubscriptionUseCase {
    void cancel(Long subscriptionId);
}

