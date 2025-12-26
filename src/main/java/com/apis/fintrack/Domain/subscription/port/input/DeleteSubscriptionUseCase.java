package com.apis.fintrack.domain.subscription.port.input;

/**
 * Input port para eliminar suscripciones.
 */
public interface DeleteSubscriptionUseCase {
    void deleteById(Long subscriptionId);
}

