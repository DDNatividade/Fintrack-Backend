package com.apis.fintrack.application.subscription.mapper;

import com.apis.fintrack.domain.subscription.model.Subscription;
import com.apis.fintrack.domain.subscription.model.SubscriptionDate;
import com.apis.fintrack.domain.subscription.port.input.CreateSubscriptionUseCase;
import java.util.Objects;

public final class CreateSubscriptionCommandMapper {

    private CreateSubscriptionCommandMapper() {
    }

    public static Subscription toSubscription(CreateSubscriptionUseCase.CreateSubscriptionCommand command) {
        Objects.requireNonNull(command, "Command must not be null");

        return Subscription.create(
                SubscriptionDate.now(),
                command.type(),
                command.userId(),
                command.paymentMethod()
        );
    }
}
