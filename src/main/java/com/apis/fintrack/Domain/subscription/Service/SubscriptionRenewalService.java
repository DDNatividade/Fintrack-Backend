package com.apis.fintrack.domain.subscription.Service;

import com.apis.fintrack.domain.subscription.model.Subscription;
import com.apis.fintrack.domain.subscription.model.payment.model.Payment;

import java.time.LocalDate;

public interface SubscriptionRenewalService {
    boolean shouldRenew(Subscription subscription);
    Payment createRenewalPayment(Subscription subscription);
    void processRenewal(Subscription subscription);
}

