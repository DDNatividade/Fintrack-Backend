package com.apis.fintrack.domain.payment.model;

import lombok.Getter;

import java.util.Objects;

/**
 * Value Object representing a payment method.
 *
 * <p>MVP scope: Only card payments (credit/debit) are supported.
 * All card payments require last 4 digits for display purposes.</p>
 */
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

    /**
     * Creates a new PaymentMethod for card payments.
     *
     * @param type           the card type (CREDIT_CARD or DEBIT_CARD)
     * @param externalToken  the payment provider token (e.g., Stripe token)
     * @param lastFourDigits the last 4 digits of the card for display
     * @return a new PaymentMethod instance
     * @throws NullPointerException     if type or externalToken is null
     * @throws IllegalArgumentException if validation fails
     */
    public static PaymentMethod of(PaymentMethodType type, String externalToken, String lastFourDigits) {
        Objects.requireNonNull(type, "Payment method type cannot be null");
        Objects.requireNonNull(externalToken, "External token cannot be null");

        if (externalToken.isBlank()) {
            throw new IllegalArgumentException("External token cannot be empty");
        }

        validateLastFourDigits(lastFourDigits);

        return new PaymentMethod(type, externalToken, lastFourDigits);
    }

    private static void validateLastFourDigits(String lastFourDigits) {
        if (lastFourDigits == null || lastFourDigits.length() != LAST_DIGITS_LENGTH) {
            throw new IllegalArgumentException(
                "Last 4 digits must have exactly " + LAST_DIGITS_LENGTH + " characters"
            );
        }

        if (!lastFourDigits.matches("\\d{4}")) {
            throw new IllegalArgumentException("Last 4 digits must be numeric");
        }
    }

    /**
     * Creates an empty PaymentMethod instance.
     * Used for unpersisted or uninitialized payment methods.
     */
    public static PaymentMethod empty() {
        return new PaymentMethod(null, null, null);
    }

    /**
     * Checks if this payment method is empty (not initialized).
     */
    public boolean isEmpty() {
        return type == null;
    }

    /**
     * Checks if this is a credit card payment.
     */
    public boolean isCreditCard() {
        return type == PaymentMethodType.CREDIT_CARD;
    }

    /**
     * Checks if this is a debit card payment.
     */
    public boolean isDebitCard() {
        return type == PaymentMethodType.DEBIT_CARD;
    }

    /**
     * Returns a masked display string for UI purposes.
     * Example: "CREDIT_CARD ****1234"
     */
    public String getMaskedDisplay() {
        if (isEmpty()) {
            return "No payment method";
        }
        return type.name() + " ****" + lastFourDigits;
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
