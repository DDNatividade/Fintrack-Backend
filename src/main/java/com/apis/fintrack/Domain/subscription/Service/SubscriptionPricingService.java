package com.apis.fintrack.domain.subscription.Service;

import com.apis.fintrack.domain.shared.model.Money;
import com.apis.fintrack.domain.subscription.model.SubscriptionType;

import java.time.LocalDate;

public interface SubscriptionPricingService {
    Money calculatePrice(SubscriptionType type);
    Money calculateProratedPrice(SubscriptionType type, LocalDate startDate);
    Money applyDiscount(Money price, DiscountCode code);
}
