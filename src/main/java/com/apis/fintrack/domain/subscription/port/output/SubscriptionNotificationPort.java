package com.apis.fintrack.domain.subscription.port.output;

import com.apis.fintrack.domain.subscription.model.Subscription;

/**
 * Puerto de salida para notificaciones relacionadas con suscripciones.
 */
public interface SubscriptionNotificationPort {
    void notifyCancellation(Subscription subscription, String reason);
}

