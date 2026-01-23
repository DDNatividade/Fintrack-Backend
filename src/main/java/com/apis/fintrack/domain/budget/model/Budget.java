package com.apis.fintrack.domain.budget.model;

import com.apis.fintrack.domain.shared.model.Money;
import com.apis.fintrack.domain.transaction.model.TransactionCategoryEnum;
import com.apis.fintrack.domain.user.model.UserId;
import lombok.Getter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

/**
 * Aggregate root for Budget.
 * <p>
 * This class enforces domain invariants in its constructor and domain methods.
 * Budget is modeled as an aggregate root so it is allowed to mutate its
 * lifecycle and attributes (limit, category, state). Mutation is justified
 * because Budget represents an entity with identity and lifecycle that
 * naturally evolves (renew, deactivate, update limit) under explicit domain
 * actions.
 */
@Getter
public class Budget {

    private final BudgetID budgetID;
    private final UserId userId;
    private Money limit;
    private BudgetPeriod period;
    private TransactionCategoryEnum category;
    private BudgetState state;

    /**
     * Create a new Budget aggregate.
     * All invariants are validated at construction time.
     *
     * @param budgetID unique identifier for the budget (maybe null for new transient budgets)
     * @param userId   owner of the budget
     * @param limit    positive Money limit (> 0)
     * @param category optional category; defaults to OTHER when null
     */
    public Budget(BudgetID budgetID, UserId userId, Money limit,TransactionCategoryEnum category) {
        Objects.requireNonNull(userId, "userId cannot be null");
        Objects.requireNonNull(limit, "limit cannot be null");
        if (limit.isZero() || limit.isNegative()) {
            throw new IllegalArgumentException("limit must be greater than zero");
        }
        if(category != null && category.equals(TransactionCategoryEnum.OTHER)){
            throw new IllegalArgumentException("Category OTHER is not allowed for budgets");
        }
        this.budgetID = budgetID;
        this.userId = userId;
        this.limit = limit;
        this.period = new BudgetPeriod();
        this.category = category;
        this.state = BudgetState.ACTIVE;
    }


    // --- Domain behaviors ---

    /**
     * Change the budget limit.
     * Mutation is explicit and intentional: budgets are configuration objects
     * that owners should be able to adjust. Invariants are re-checked.
     *
     * @param newLimit positive Money
     */
    public void changeLimit(Money newLimit) {
        Objects.requireNonNull(newLimit, "newLimit cannot be null");
        if (newLimit.isZero() || newLimit.isNegative()) {
            throw new IllegalArgumentException("limit must be greater than zero");
        }
        this.limit = newLimit;
    }

    /**
     * Change the category of the budget.
     * Simple domain operation, no extra invariants.
     */
    public void changeCategory(TransactionCategoryEnum newCategory) {
        Objects.requireNonNull(newCategory, "newCategory cannot be null");
        if(newCategory.equals(TransactionCategoryEnum.OTHER)){
            throw new IllegalArgumentException("Category OTHER is not allowed for budgets");
        }
        this.category = newCategory;
    }

    /**
     * Why amountSpent is not stored inside Budget:
     * Budget does not store amountSpent because transactions and their sums are
     * part of other aggregates (for example, Transaction or a read-model). The
     * Budget aggregate is a configuration and policy object; storing computed
     * aggregates (amountSpent) would introduce duplication and consistency
     * challenges. Therefore, amountSpent is provided by the caller when needed.
     */
    public boolean isExceeded(Money amountSpent) {
        Objects.requireNonNull(amountSpent, "amountSpent cannot be null");
        return amountSpent.isGreaterThanOrEqual(limit);
    }

    /**
     * Return remaining amount (limit - amountSpent). Never negative; returns Money.zero()
     * when amountSpent >= limit.
     */
    public Money getRemainingAmount(Money amountSpent) {
        Objects.requireNonNull(amountSpent, "amountSpent cannot be null");
        if (amountSpent.isGreaterThanOrEqual(limit)) {
            return Money.zero();
        }
        return limit.subtract(amountSpent);
    }

    /**
     * Return usage percentage in [0,100]. Uses Money amounts for calculation.
     */
    public BigDecimal getUsagePercentage(Money amountSpent) {
        Objects.requireNonNull(amountSpent, "amountSpent cannot be null");
        if (limit.isZero()) {
            return BigDecimal.ZERO;
        }
        BigDecimal usage = amountSpent.getAmount()
                .divide(limit.getAmount(), 4, RoundingMode.HALF_EVEN)
                .multiply(BigDecimal.valueOf(100));
        return usage.min(BigDecimal.valueOf(100));
    }

    /**
     * Why does renewBudget() exist and what does it represent in the domain?
     *
     * renewBudget is an explicit domain command that advances the Budget's
     * period to the next logical period. It represents an owner's intention to
     * continue using the same budget configuration for the following period.
     * Renewal is explicit because automatic renewal would hide intent and make
     * lifecycle reasoning harder.
     */
    public void renewBudget() {
        if (this.state == BudgetState.INACTIVE) {
            throw new IllegalStateException("Cannot renew an inactive budget");
        }
        this.period = this.period.nextPeriod();
    }

    /**
     * Why BudgetState is needed instead of deleting the budget:
     *
     * Keeping a Budget record and toggling its state to INACTIVE preserves
     * historical configuration and avoids accidental data loss. It also allows
     * safe auditing and simplifies references from other parts of the system
     * that may hold Budget identifiers.
     */
    public void deactivateBudget() {
        if (this.state == BudgetState.INACTIVE) {
            throw new IllegalStateException("Budget is already inactive");
        }
        this.state = BudgetState.INACTIVE;
    }

    // --- Equality & Representation ---

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Budget budget = (Budget) o;
        return Objects.equals(budgetID, budget.budgetID) && Objects.equals(userId, budget.userId) && Objects.equals(limit, budget.limit) && Objects.equals(period, budget.period) && category == budget.category;
    }

    @Override
    public int hashCode() {
        return Objects.hash(budgetID, userId, limit, period, category);
    }

    @Override
    public String toString() {
        return "Budget{" +
                "budgetID=" + budgetID +
                ", userId=" + userId +
                ", limit=" + limit +
                ", period=" + period +
                ", category=" + category +
                ", state=" + state +
                '}';
    }

}
