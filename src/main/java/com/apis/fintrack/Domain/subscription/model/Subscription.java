package com.apis.fintrack.domain.subscription.model;

import com.apis.fintrack.domain.shared.model.Money;
import com.apis.fintrack.domain.subscription.model.payment.model.Payment;
import com.apis.fintrack.domain.subscription.model.payment.model.PaymentMethod;
import com.apis.fintrack.domain.user.model.UserId;
import lombok.Getter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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
            SubscriptionId id, SubscriptionDate subscriptionDate,
            SubscriptionType type, UserId userId, boolean isActive,
            PaymentMethod paymentMethod, List<Payment> payments) {
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
            SubscriptionType type, UserId userId,
            PaymentMethod paymentMethod) {
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

    // ==================== METODOS DE NEGOCIO ====================

    public SubscriptionType changeType(SubscriptionType newType) {
        Objects.requireNonNull(newType, "El tipo de suscripción no puede ser nulo");
        if (this.type == ANNUAL && !payments.isEmpty()) {
            throw new IllegalArgumentException("No se puede cambiar el tipo de suscripciÃ³n. Pago ya efectuado");
        }
        this.type = newType;
        return this.type;
    }

    public void addPayment(Payment payment) {
        Objects.requireNonNull(payment, "El pago no puede ser nulo");
        this.payments.add(payment);
    }

    public void deactivateSubscription() {
        if (hasPendingPayments()) {
            throw new IllegalArgumentException("No se puede cancelar la suscripciÃ³n. Hay pagos pendientes");
        }
        this.isActive = false;
    }

    public void reactivate() {
        if (this.isActive) {
            throw new IllegalStateException("La suscripciÃ³n ya estÃ¡ activa");
        }
        if (!hasPaymentMethod()) {
            throw new IllegalStateException("No se puede reactivar sin un mÃ©todo de pago configurado");
        }
        this.isActive = true;
    }

    public void changePaymentMethod(PaymentMethod paymentMethod) {
        Objects.requireNonNull(paymentMethod, "El mÃ©todo de pago no puede ser nulo");
        if (paymentMethod.isEmpty()) {
            throw new IllegalArgumentException("El mÃ©todo de pago no puede estar vacÃ­o");
        }
        this.paymentMethod = paymentMethod;
    }

    // ==================== MÃ‰TODOS DE CONSULTA ====================

    public boolean isExpired() {
        return subscriptionDate.isExpired(this.type);
    }

    public LocalDate getNextPaymentDate() {
        LocalDate expirationDate = subscriptionDate.getExpirationDate(this.type);
        if (LocalDate.now().isAfter(expirationDate)) {
            return LocalDate.now();
        }
        return expirationDate;
    }

    public Money getTotalPaid() {
        return payments.stream()
                .filter(Payment::isPaid)
                .map(Payment::getAmount)
                .reduce(Money.zero(), Money::add);
    }

    public List<Payment> getPendingPayments() {
        return payments.stream()
                .filter(payment -> !payment.isPaid())
                .collect(Collectors.toList());
    }

    public List<Payment> getPaidPayments() {
        return payments.stream()
                .filter(Payment::isPaid)
                .collect(Collectors.toList());
    }

    public boolean hasPendingPayments() {
        return payments.stream().anyMatch(payment -> !payment.isPaid());
    }

    public boolean hasPaymentMethod() {
        return paymentMethod != null && !paymentMethod.isEmpty();
    }

    public long daysUntilExpiration() {
        return subscriptionDate.daysUntilExpiration(this.type);
    }

    public boolean isAboutToExpire(int daysThreshold) {
        long daysRemaining = daysUntilExpiration();
        return daysRemaining >= 0 && daysRemaining <= daysThreshold;
    }









}

