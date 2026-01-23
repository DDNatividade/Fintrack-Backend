package com.apis.fintrack.domain.subscription.model;

import com.apis.fintrack.domain.payment.model.Payment;
import com.apis.fintrack.domain.shared.model.Money;
import com.apis.fintrack.domain.payment.model.PaymentMethod;
import com.apis.fintrack.domain.user.model.UserId;
import lombok.Getter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.apis.fintrack.domain.subscription.model.SubscriptionType.ANNUAL;

@Getter
public class Subscription {

    private final SubscriptionId id;
    private final SubscriptionDate subscriptionDate;
    private SubscriptionType type;
    private final UserId userId;
    private boolean isActive;
    private PaymentMethod paymentMethod;
    private final List<Payment> payments;

    public Subscription(
            SubscriptionId id,
            SubscriptionDate subscriptionDate,
            SubscriptionType type,
            UserId userId,
            boolean isActive,
            PaymentMethod paymentMethod,
            List<Payment> payments
    ) {
        this.id = id;
        this.subscriptionDate = subscriptionDate;
        this.type = type;
        this.userId = userId;
        this.isActive = isActive;
        this.paymentMethod = paymentMethod;
        this.payments = new ArrayList<>(payments != null ? payments : List.of());
    }

    public static Subscription create(
            SubscriptionDate subscriptionDate,
            SubscriptionType type,
            UserId userId,
            PaymentMethod paymentMethod
    ) {
        return new Subscription(
                SubscriptionId.empty(),
                subscriptionDate,
                type,
                userId,
                true,
                paymentMethod,
                new ArrayList<>()
        );
    }

    // ==================== COMPORTAMIENTO DE DOMINIO ====================

    public void registerPaymentSucceeded(
            Payment payment
    ) {
        Objects.requireNonNull(payment, "Payment must not be null");

        // Idempotencia básica: si ya existe un pago SUCCEEDED para esta fecha/amount, ignoramos
        boolean alreadyPaid = payments.stream()
                .anyMatch(p -> p.isPaid()
                        && p.getAmount().equals(payment.getAmount())
                        && p.getPaymentDate().equals(payment.getPaymentDate()));

        if (alreadyPaid) {
            return;
        }

        payment.markAsSucceeded();
        payments.add(payment);

        // Un pago exitoso mantiene la suscripción activa
        this.isActive = true;
    }

    public void registerPaymentFailed(
            Payment payment
    ) {
        Objects.requireNonNull(payment, "Payment must not be null");

        payment.markAsFailed();
        payments.add(payment);
        // Para MVP: un fallo NO desactiva automáticamente la suscripción
    }

    public void changeType(SubscriptionType newType) {
        Objects.requireNonNull(newType, "El tipo de suscripción no puede ser nulo");
        if (this.type == ANNUAL && !payments.isEmpty()) {
            throw new IllegalArgumentException("No se puede cambiar el tipo de suscripción. Pago ya efectuado");
        }
        this.type = newType;
    }

    public void deactivateSubscription() {
        if (hasPendingPayments()) {
            throw new IllegalArgumentException("No se puede cancelar la suscripción. Hay pagos pendientes");
        }
        this.isActive = false;
    }

    public void activateSubscription() {
        if( hasPendingPayments()) {
            throw new IllegalArgumentException("No se puede activar la suscripción. Hay pagos pendientes");
        }
        this.isActive = true;
    }

    public void changePaymentMethod(PaymentMethod newPaymentMethod) {
        Objects.requireNonNull(newPaymentMethod, "El método de pago no puede ser nulo");
        this.paymentMethod = newPaymentMethod;
    }

    // ==================== CONSULTAS ====================

    public boolean hasPendingPayments() {
        return payments.stream().anyMatch(Payment::isPending);
    }

    public boolean hasPaymentMethod() {
        return paymentMethod != null && !paymentMethod.isEmpty();
    }

    public Money getTotalPaid() {
        return payments.stream()
                .filter(Payment::isPaid)
                .map(Payment::getAmount)
                .reduce(Money.zero(), Money::add);
    }

    public boolean isExpired() {
        return subscriptionDate.isExpired(this.type);
    }

    public LocalDate getNextPaymentDate() {
        LocalDate expirationDate = subscriptionDate.getExpirationDate(this.type);
        return LocalDate.now().isAfter(expirationDate)
                ? LocalDate.now()
                : expirationDate;
    }
}
