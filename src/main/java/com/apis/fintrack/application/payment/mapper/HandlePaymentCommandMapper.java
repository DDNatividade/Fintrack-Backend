package com.apis.fintrack.application.payment.mapper;

import com.apis.fintrack.domain.payment.model.Payment;
import com.apis.fintrack.domain.payment.model.PaymentDate;
import com.apis.fintrack.domain.payment.port.input.HandlePaymentEventUseCase;

import java.time.LocalDate;
import java.util.Objects;

public final class HandlePaymentCommandMapper {

    private HandlePaymentCommandMapper() {}

    public static Payment toPayment(HandlePaymentEventUseCase.HandlePaymentEventCommand command) {
        Objects.requireNonNull(command, "Command cannot be null");

        return Payment.create(
                PaymentDate.of(LocalDate.now()),
                command.userId(),
                command.subscriptionId(),
                command.amount()
        );
    }
}
