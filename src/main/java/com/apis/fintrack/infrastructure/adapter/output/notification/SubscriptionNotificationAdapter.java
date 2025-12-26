package com.apis.fintrack.infrastructure.adapter.output.notification;

import com.apis.fintrack.domain.subscription.model.Subscription;
import com.apis.fintrack.domain.subscription.port.output.SubscriptionNotificationPort;
import org.springframework.stereotype.Component;

@Component
public class SubscriptionNotificationAdapter implements SubscriptionNotificationPort {

    @Override
    public void notifyCancellation(Subscription subscription, String reason) {
        // Minimal: log or ignore. In a real adapter notify by email, webhook, etc.
        System.out.println("[Notification] Subscription " + (subscription.getId()!=null?subscription.getId().getValue():"?") + " canceled: " + reason);
    }
}

