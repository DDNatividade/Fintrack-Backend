package com.apis.fintrack.domain.budget.model;

import com.apis.fintrack.domain.shared.model.Money;
import com.apis.fintrack.domain.transaction.model.TransactionCategoryEnum;
import com.apis.fintrack.domain.user.model.UserId;
import lombok.Getter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

@Getter
public class Budget {
    private final BudgetID budgetID;
    private final UserId userId;
    private Money limit;
    private final BudgetPeriod period;
    private TransactionCategoryEnum categoryEnum;

    public Budget(BudgetID budgetID, UserId userId, Money limit, BudgetPeriod period, TransactionCategoryEnum categoryEnum) {
        Objects.requireNonNull(userId, "El userId no puede ser nulo");
        Objects.requireNonNull(limit, "El límite no puede ser nulo");
        Objects.requireNonNull(period, "El período no puede ser nulo");
        if (limit.isZero() || limit.isNegative()) {
            throw new IllegalArgumentException("El límite debe ser mayor a cero");
        }
        this.budgetID = budgetID;
        this.userId = userId;
        this.limit = limit;
        this.period = period;
        this.categoryEnum = categoryEnum!=null ? categoryEnum : TransactionCategoryEnum.OTHER;
    }

    public void changeLimit(Money newLimit) {
        Objects.requireNonNull(newLimit, "El nuevo límite no puede ser nulo");
        if (newLimit.isZero() || newLimit.isNegative()) {
            throw new IllegalArgumentException("El límite debe ser mayor a cero");
        }
        this.limit= newLimit;
    }

    public void changeCategory(TransactionCategoryEnum newCategory) {
        this.categoryEnum = newCategory;
    }

    public boolean isExceeded(Money amountSpent) {
        return amountSpent.isGreaterThanOrEqual(limit);
    }

    public Money getRemainingAmount(Money amountSpent) {
        Objects.requireNonNull(amountSpent, "Amount spent cannot be null");
        if( amountSpent.isGreaterThanOrEqual(limit)) {
            return Money.zero();
        }
        return limit.subtract(amountSpent);
    }

    public BigDecimal getUsagePercentage(Money amountSpent) {
        Objects.requireNonNull(amountSpent, "Amount spent cannot be null");
        if (limit.isZero()) {
            return BigDecimal.ZERO;
        }
        BigDecimal usage = amountSpent.getAmount()
                .divide(limit.getAmount(), 4, RoundingMode.HALF_EVEN)
                .multiply(BigDecimal.valueOf(100));
        return usage.min(BigDecimal.valueOf(100));
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Budget budget = (Budget) o;
        return Objects.equals(budgetID, budget.budgetID) && Objects.equals(userId, budget.userId) && Objects.equals(limit, budget.limit) && Objects.equals(period, budget.period) && categoryEnum == budget.categoryEnum;
    }

    @Override
    public int hashCode() {
        return Objects.hash(budgetID, userId, limit, period, categoryEnum);
    }

    @Override
    public String toString() {
        return "Budget{" +
                "budgetID=" + budgetID +
                ", userId=" + userId +
                ", limit=" + limit +
                ", period=" + period +
                ", categoryEnum=" + categoryEnum +
                '}';
    }


}
