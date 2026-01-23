package com.apis.fintrack.domain.payment.model;

import com.apis.fintrack.domain.shared.model.Money;
import com.apis.fintrack.domain.subscription.model.SubscriptionId;
import com.apis.fintrack.domain.user.model.UserId;
import lombok.Getter;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Getter
public class Payment {

    private final PaymentId id;
    private final PaymentDate paymentDate;
    private final UserId userId;
    private final SubscriptionId subscriptionId;
    private final Money amount;
    private PaymentStatus status;

    private Payment(
            PaymentId id,
            PaymentDate paymentDate,
            UserId userId,
            SubscriptionId subscriptionId,
            Money amount,
            PaymentStatus status
    ) {
        this.id = id;
        this.paymentDate = paymentDate;
        this.userId = userId;
        this.subscriptionId = subscriptionId;
        this.amount = amount;
        this.status = status;
    }

    // ==================== FACTORY ====================

    public static Payment create(
            PaymentDate paymentDate,
            UserId userId,
            SubscriptionId subscriptionId,
            Money amount
    ) {
        return new Payment(
                PaymentId.empty(),
                paymentDate,
                userId,
                subscriptionId,
                amount,
                PaymentStatus.PENDING
        );
    }

    // ==================== COMPORTAMIENTO DE DOMINIO ====================

    public void markAsSucceeded() {
        if (status == PaymentStatus.SUCCEEDED) {
            return; // idempotente
        }
        if (status == PaymentStatus.FAILED) {
            throw new IllegalStateException("Cannot mark a failed payment as succeeded");
        }
        this.status = PaymentStatus.SUCCEEDED;
    }

    public void markAsFailed() {
        if (status == PaymentStatus.FAILED) {
            return; // idempotente
        }
        if (status == PaymentStatus.SUCCEEDED) {
            throw new IllegalStateException("Cannot mark a succeeded payment as failed");
        }
        this.status = PaymentStatus.FAILED;
    }



    // ==================== CONSULTAS ====================

    public boolean isPaid() {
        return status == PaymentStatus.SUCCEEDED;
    }

    public boolean isPending() {
        return status == PaymentStatus.PENDING;
    }

    public boolean isFailed() {
        return status == PaymentStatus.FAILED;
    }

    public boolean isOverdue() {
        return status == PaymentStatus.PENDING
                && LocalDate.now().isAfter(paymentDate.getValue());
    }

    public long daysOverdue() {
        if (!isOverdue()) {
            return 0;
        }
        return ChronoUnit.DAYS.between(paymentDate.getValue(), LocalDate.now());
    }

    public long daysUntilDue() {
        if (status != PaymentStatus.PENDING || isOverdue()) {
            return 0;
        }
        return ChronoUnit.DAYS.between(LocalDate.now(), paymentDate.getValue());
    }
}
