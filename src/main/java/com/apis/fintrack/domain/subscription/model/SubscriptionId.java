package com.apis.fintrack.domain.subscription.model;

import java.util.Objects;

public class SubscriptionId {
    private final Long value;
    private SubscriptionId(Long value) {
        this.value = value;
    }

    public static SubscriptionId of(Long value) {
        if (value == null) {
            throw new IllegalArgumentException("SubscriptionId no puede ser nulo");
        }
        if (value <= 0) {
            throw new IllegalArgumentException("SubscriptionId debe ser un nÃºmero positivo");
        }
        return new SubscriptionId(value);
    }

    public static SubscriptionId empty() {
        return new SubscriptionId(null);
    }

    public Long getValue() {
        return value;
    }

    public boolean isEmpty() {
        return value == null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SubscriptionId subscriptionId = (SubscriptionId) o;
        return Objects.equals(value, subscriptionId.value);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }
}

