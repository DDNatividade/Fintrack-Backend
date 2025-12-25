package com.apis.fintrack.application.mapper;

import com.apis.fintrack.domain.payment.model.Payment;
import com.apis.fintrack.domain.payment.port.input.HandlePaymentEventUseCase;

import java.util.Objects;

public final class HandlePaymentCommandMapper {

    private HandlePaymentCommandMapper() {}

    public static Payment toPayment(HandlePaymentEventUseCase.HandlePaymentEventCommand command) {
        Objects.requireNonNull(command, "Command cannot be null");

        return Payment.create(
                command.externalPaymentId(),
                command.amount(),
                command.subscriptionId(),

        );
    }
}
