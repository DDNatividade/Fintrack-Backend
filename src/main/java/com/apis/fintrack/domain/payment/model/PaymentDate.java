package com.apis.fintrack.domain.payment.model;

import lombok.Getter;
import java.time.LocalDate;
import java.util.Objects;

@Getter
public class PaymentDate {
    private final LocalDate value;
    private PaymentDate(LocalDate value) {
        this.value = value;
    }
    public static PaymentDate of(LocalDate value) {
        if (value == null) {
            throw new IllegalArgumentException("La fecha de pago no puede ser nula");
        }
        return new PaymentDate(value);
    }
    public static PaymentDate now() {
        return new PaymentDate(LocalDate.now());
    }
    public static PaymentDate fromStorage(LocalDate storedDate) {
        if (storedDate == null) {
            throw new IllegalArgumentException("La fecha almacenada no puede ser nula");
        }
        return new PaymentDate(storedDate);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PaymentDate that = (PaymentDate) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}

