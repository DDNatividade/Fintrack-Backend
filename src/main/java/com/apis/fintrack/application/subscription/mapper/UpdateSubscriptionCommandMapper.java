package com.apis.fintrack.application.subscription.mapper;

import com.apis.fintrack.domain.subscription.model.Subscription;
import com.apis.fintrack.domain.subscription.port.input.UpdateSubscriptionUseCase;
import java.util.Objects;

public final class UpdateSubscriptionCommandMapper {
    private UpdateSubscriptionCommandMapper() {
    }

    public void applyUpdateCommand(Subscription subscription,
                                   UpdateSubscriptionUseCase.UpdateSubscriptionCommand command) {
        if(!Objects.equals(subscription.getId().getValue(), command.subscriptionId())){
            throw new IllegalArgumentException("Subscription ID mismatch");
        }
        command.newType().ifPresent(subscription::changeType);
        command.newPaymentMethod().ifPresent(subscription::changePaymentMethod);
        command.reactivate().ifPresent(shouldReactivate -> {
            if (shouldReactivate) {
                subscription.activateSubscription();
            } else {
                subscription.deactivateSubscription();
            }
        });

    }

}
