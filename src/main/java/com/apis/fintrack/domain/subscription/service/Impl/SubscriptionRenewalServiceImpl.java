package com.apis.fintrack.domain.subscription.Service.Impl;

import com.apis.fintrack.domain.subscription.Service.PaymentCalculationService;
import com.apis.fintrack.domain.subscription.Service.SubscriptionRenewalService;
import com.apis.fintrack.domain.subscription.model.Subscription;
import com.apis.fintrack.domain.subscription.model.SubscriptionDate;
import com.apis.fintrack.domain.payment.model.Payment;
import com.apis.fintrack.domain.payment.model.PaymentDate;

public class SubscriptionRenewalServiceImpl implements SubscriptionRenewalService {
    private PaymentCalculationService paymentCalculationService;

    @Override
    public boolean shouldRenew(Subscription subscription) {
        if (subscription.isExpired()) {
            return true;
        }
        return false;
    }

    @Override
    public Payment createRenewalPayment(Subscription subscription) {
        if (!shouldRenew(subscription)) {
            throw new IllegalStateException("No se puede renovar una suscripciÃ³n activa");
        }
        return Payment.create(
                PaymentDate.now(),
                subscription.getUserId(),
                subscription.getId(),
                paymentCalculationService.calculatePendingAmount(subscription)
        );
    }

    @Override
    public void processRenewal(Subscription subscription) {
        if (subscription.getPayments().stream().anyMatch(Payment::isPending)) {
            throw new IllegalStateException("Tiene pagos pendientes");
        }

        // Update subscription date to today (new base date)
        SubscriptionDate.now();

        // Activar si estaba inactiva
        if (!subscription.isActive()) {
            subscription.activateSubscription();
        }
    }


}

