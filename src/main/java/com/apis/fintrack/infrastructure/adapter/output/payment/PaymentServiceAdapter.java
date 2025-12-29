package com.apis.fintrack.infrastructure.adapter.output.payment;

import com.apis.fintrack.domain.payment.model.Payment;
import com.apis.fintrack.domain.payment.model.PaymentMethod;
import com.apis.fintrack.domain.subscription.model.Subscription;
import com.apis.fintrack.domain.payment.port.output.PaymentServicePort;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class PaymentServiceAdapter implements PaymentServicePort {

    @Override
    public boolean validatePaymentMethod(PaymentMethod paymentMethod) {
        // Minimal implementation: accept non-null, non-empty token methods
        if (paymentMethod == null) return true;
        return paymentMethod.isEmpty();
    }

    @Override
    public List<Payment> findPendingPayments(Subscription subscription) {
        // Minimal adapter returns empty list — real implementation should call payment gateway or DB
        return Collections.emptyList();
    }
}

