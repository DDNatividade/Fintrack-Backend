package com.apis.fintrack.domain.subscription.service.Impl;

import com.apis.fintrack.domain.payment.model.Payment;
import com.apis.fintrack.domain.payment.model.PaymentStatus;
import com.apis.fintrack.domain.payment.port.output.PaymentServicePort;
import com.apis.fintrack.domain.shared.model.Money;
import com.apis.fintrack.domain.subscription.Service.PaymentCalculationService;
import com.apis.fintrack.domain.subscription.model.Subscription;


public class PaymentCalculationServiceImpl implements PaymentCalculationService {

    private final PaymentServicePort repository;
    public PaymentCalculationServiceImpl(PaymentServicePort repository) {
        this.repository = repository;
    }

    @Override
    public Money calculateTotalPaid(Subscription subscription) {
        return repository.findPendingPayments(subscription)
                .stream()
                .filter(payment -> payment.getStatus() == PaymentStatus.SUCCEEDED)
                .map(Payment::getAmount)
                .reduce(Money.zero(), Money::add);
    }

    @Override
    public Money calculatePendingAmount(Subscription subscription) {
        return repository.findPendingPayments(subscription)
                .stream()
                .filter(payment -> payment.getStatus() == PaymentStatus.PENDING)
                .map(Payment::getAmount)
                .reduce(Money.zero(), Money::add);
    }

    @Override
    public boolean isFullyPaid(Subscription subscription) {
        return repository.findPendingPayments(subscription)
                .stream()
                .allMatch(payment -> payment.getStatus() == PaymentStatus.SUCCEEDED);
    }
}

