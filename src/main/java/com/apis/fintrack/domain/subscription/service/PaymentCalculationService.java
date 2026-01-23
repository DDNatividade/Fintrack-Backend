package com.apis.fintrack.domain.subscription.Service;

import com.apis.fintrack.domain.shared.model.Money;
import com.apis.fintrack.domain.subscription.model.Subscription;

public interface PaymentCalculationService {
    Money calculateTotalPaid(Subscription subscription);
    Money calculatePendingAmount(Subscription subscription);
    boolean isFullyPaid(Subscription subscription);
}
