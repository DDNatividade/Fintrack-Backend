package com.apis.fintrack.domain.payment.model;

import lombok.Getter;

import java.util.Objects;

@Getter
public class PaymentMethod {
    private static final int LAST_DIGITS_LENGTH = 4;

    private final PaymentMethodType type;
    private final String externalToken;
    private final String lastFourDigits;

    private PaymentMethod(PaymentMethodType type, String externalToken, String lastFourDigits) {
        this.type = type;
        this.externalToken = externalToken;
        this.lastFourDigits = lastFourDigits;
    }

    public static PaymentMethod of(PaymentMethodType type, String externalToken, String lastFourDigits) {
        Objects.requireNonNull(type, "El tipo de mÃ©todo de pago no puede ser nulo");
        Objects.requireNonNull(externalToken, "El token externo no puede ser nulo");

        if (externalToken.isBlank()) {
            throw new IllegalArgumentException("El token externo no puede estar vacÃ­o");
        }

        validateLastFourDigits(lastFourDigits, type);

        return new PaymentMethod(type, externalToken, lastFourDigits);
    }

    private static void validateLastFourDigits(String lastFourDigits, PaymentMethodType type) {
        // Only validate for methods that have digits (cards)
        if (type == PaymentMethodType.CREDIT_CARD ||
            type == PaymentMethodType.DEBIT_CARD ||
            type == PaymentMethodType.APPLE_PAY ||
            type == PaymentMethodType.GOOGLE_PAY) {

            if (lastFourDigits == null || lastFourDigits.length() != LAST_DIGITS_LENGTH) {
                throw new IllegalArgumentException(
                    "Los Ãºltimos 4 dÃ­gitos deben tener exactamente " + LAST_DIGITS_LENGTH + " caracteres"
                );
            }

            if (!lastFourDigits.matches("\\d{4}")) {
                throw new IllegalArgumentException("Los Ãºltimos 4 dÃ­gitos deben ser numÃ©ricos");
            }
        }
    }

    public static PaymentMethod empty() {
        return new PaymentMethod(null, null, null);
    }

    public boolean isEmpty() {
        return type == null;
    }

    public boolean isCardPayment() {
        return type == PaymentMethodType.CREDIT_CARD || type == PaymentMethodType.DEBIT_CARD;
    }

    public boolean isDigitalWallet() {
        return type == PaymentMethodType.APPLE_PAY ||
               type == PaymentMethodType.GOOGLE_PAY ||
               type == PaymentMethodType.PAYPAL;
    }

    public String getMaskedDisplay() {
        if (isEmpty()) {
            return "Sin mÃ©todo de pago";
        }
        if (lastFourDigits != null) {
            return type.name() + " ****" + lastFourDigits;
        }
        return type.name();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PaymentMethod that = (PaymentMethod) o;
        return type == that.type &&
               Objects.equals(externalToken, that.externalToken);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, externalToken);
    }

    @Override
    public String toString() {
        return getMaskedDisplay();
    }
}


