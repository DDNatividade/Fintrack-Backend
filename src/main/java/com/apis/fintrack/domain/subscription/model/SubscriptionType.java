package com.apis.fintrack.domain.subscription.model;

import java.util.Objects;

public enum SubscriptionType {
    ANNUAL,
    MONTHLY;


    public static SubscriptionType fromString(String type) {
        Objects.requireNonNull(type, "Subscription type cannot be null");
        try {
            return SubscriptionType.valueOf(type.trim().toUpperCase());
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("Unknown subscription type: " + type, ex);
        }
    }


    public boolean isAnnual() {
        return this == ANNUAL;
    };


}

