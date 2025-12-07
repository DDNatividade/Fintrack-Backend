package com.apis.fintrack.domain.subscription.model.payment.model;

import com.apis.fintrack.domain.shared.model.Money;
import com.apis.fintrack.domain.subscription.model.SubscriptionId;
import com.apis.fintrack.domain.user.model.UserId;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class Payment {
    private final PaymentId id;
    private final PaymentDate paymentDate;
    private final UserId userId;
    private final SubscriptionId subscriptionId;
    private final Money amount;
    private boolean isPaid;
    private boolean isRefunded;
    private PaymentDate refundDate;

    public Payment(
            PaymentId id, PaymentDate paymentDate, UserId userId,
            SubscriptionId subscriptionId, Money amount,
            boolean isPaid, boolean isRefunded, PaymentDate refundDate) {
        this.id = id;
        this.paymentDate = paymentDate;
        this.userId = userId;
        this.subscriptionId = subscriptionId;
        this.amount = amount;
        this.isPaid = isPaid;
        this.isRefunded = isRefunded;
        this.refundDate = refundDate;
    }

    // Constructor simplificado para compatibilidad
    public Payment(
            PaymentId id, PaymentDate paymentDate, UserId userId,
            SubscriptionId subscriptionId, Money amount, boolean isPaid) {
        this(id, paymentDate, userId, subscriptionId, amount, isPaid, false, null);
    }

    public static Payment create(
            PaymentDate paymentDate,
            UserId userId, SubscriptionId subscriptionId, Money amount) {
        return new Payment(
            PaymentId.empty(),
            paymentDate,
            userId,
            subscriptionId,
            amount,
            false,
            false,
            null
        );
    }

    // ==================== MÃ‰TODOS DE NEGOCIO ====================

    public void markAsPaid() {
        if (this.isRefunded) {
            throw new IllegalStateException("No se puede marcar como pagado un pago reembolsado");
        }
        this.isPaid = true;
    }

    public void refund() {
        if (!this.isPaid) {
            throw new IllegalStateException("No se puede reembolsar un pago que no ha sido realizado");
        }
        if (this.isRefunded) {
            throw new IllegalStateException("El pago ya ha sido reembolsado");
        }
        this.isRefunded = true;
        this.refundDate = PaymentDate.now();
    }

    // ==================== MÃ‰TODOS DE CONSULTA ====================

    public boolean isOverdue() {
        if (this.isPaid) {
            return false;
        }
        return LocalDate.now().isAfter(paymentDate.getValue());
    }

    public boolean isPending() {
        return !this.isPaid && !this.isRefunded;
    }

    public boolean isCompleted() {
        return this.isPaid && !this.isRefunded;
    }

    public long daysOverdue() {
        if (!isOverdue()) {
            return 0;
        }
        return java.time.temporal.ChronoUnit.DAYS.between(paymentDate.getValue(), LocalDate.now());
    }

    public long daysUntilDue() {
        if (isPaid || isOverdue()) {
            return 0;
        }
        return java.time.temporal.ChronoUnit.DAYS.between(LocalDate.now(), paymentDate.getValue());
    }
}

