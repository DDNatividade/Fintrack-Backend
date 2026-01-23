package com.apis.fintrack.domain.payment.model;

/**
 * Payment method types supported by the system.
 *
 * <p>MVP scope: Only card payments (credit/debit) are supported.
 * Future versions may include digital wallets (Apple Pay, Google Pay, PayPal).</p>
 */
public enum PaymentMethodType {
    /**
     * Credit card payment processed via Stripe.
     */
    CREDIT_CARD,

    /**
     * Debit card payment processed via Stripe.
     */
    DEBIT_CARD
}

