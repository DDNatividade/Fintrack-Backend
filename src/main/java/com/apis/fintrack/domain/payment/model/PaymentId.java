package com.apis.fintrack.domain.payment.model;

import lombok.Getter;

import java.util.Objects;

@Getter
public class PaymentId {
    private final Long value;
    private PaymentId(Long value) {
        this.value = value;
    }
    public static PaymentId of(Long value) {
        if (value == null) {
            throw new IllegalArgumentException("PaymentId no puede ser nulo");
        }
        if (value <= 0) {
            throw new IllegalArgumentException("PaymentId debe ser un número positivo");
        }
        return new PaymentId(value);
    }
    public static PaymentId empty() {
        return new PaymentId(null);
    }

    public boolean isEmpty() {
        return value == null;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PaymentId paymentId = (PaymentId) o;
        return Objects.equals(value, paymentId.value);
    }
    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}

