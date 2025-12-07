package com.apis.fintrack.domain.subscription.Service.Impl;

import com.apis.fintrack.domain.shared.model.Money;
import com.apis.fintrack.domain.subscription.Service.PaymentCalculationService;
import com.apis.fintrack.domain.subscription.model.Subscription;
import com.apis.fintrack.domain.subscription.model.payment.model.Payment;

import java.util.List;

public class PaymentCalculationServiceImpl implements PaymentCalculationService {

    @Override
    public Money calculateTotalPaid(Subscription subscription) {
       Money totalPaid = subscription.getPaidPayments().stream()
                .map(Payment::getAmount).reduce(Money.zero(), Money::add);
        return totalPaid;

    }

    @Override
    public Money calculatePendingAmount(Subscription subscription) {
        Money pendingAmount = subscription.getPendingPayments().stream()
                .map(Payment::getAmount).reduce(Money.zero(), Money::add);
        return pendingAmount;
    }

    @Override
    public boolean isFullyPaid(Subscription subscription) {
        if (subscription.getPendingPayments().isEmpty()) {
            return true;
        }
        return false;
    }
}

