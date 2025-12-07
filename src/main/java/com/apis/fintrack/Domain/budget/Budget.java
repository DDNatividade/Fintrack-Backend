package com.apis.fintrack.domain.budget;

import com.apis.fintrack.domain.shared.model.Money;
import com.apis.fintrack.domain.transaction.model.TransactionCategoryEnum;
import com.apis.fintrack.domain.user.model.UserId;
import lombok.Getter;

import java.util.Objects;

@Getter
public class Budget {
    private final BudgetID budgetID;
    private final UserId userId;
    private Money limit;
    private final BudgetPeriod period;
    private TransactionCategoryEnum categoryEnum;

    public Budget(BudgetID budgetID, UserId userId, Money limit, BudgetPeriod period) {
        this.budgetID = budgetID;
        this.userId = userId;
        this.limit = limit;
        this.period = period;
        this.categoryEnum = categoryEnum!=null ? categoryEnum : TransactionCategoryEnum.OTHER;
    }

    public void changeLimit(Money newLimit) {
        this.limit = newLimit;
    }

    public void changeCategory(TransactionCategoryEnum newCategory) {
        this.categoryEnum = newCategory;
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
