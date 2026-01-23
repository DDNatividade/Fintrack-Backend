package com.apis.fintrack.domain.budget.model;

import lombok.Getter;
import java.time.LocalDate;
import java.util.Objects;

/**
 * Value Object representing a budget period.
 * BudgetPeriod is immutable: once created it cannot be changed. This avoids
 * accidental shared-state bugs and makes reasoning about budget periods
 * straightforward. When a budget must move to the next period, a new
 * BudgetPeriod instance is produced using {@link #nextPeriod()}.
 */
@Getter
public final class BudgetPeriod {

    private final LocalDate startDate;
    private final LocalDate endDate;

    /**
     * Create a BudgetPeriod that starts at the beginning of the given date's month
     * and ends at the last day of that month.
     *
     * @param reference LocalDate used to compute the month (must not be null)
     */
    public BudgetPeriod(LocalDate reference) {
        Objects.requireNonNull(reference, "reference date cannot be null");
        this.startDate = reference.withDayOfMonth(1);
        this.endDate = this.startDate.plusMonths(1).minusDays(1);
    }

    /**
     * Convenience constructor for "current month".
     */
    public BudgetPeriod() {
        this(LocalDate.now());
    }


    /**
     * Produce the next consecutive BudgetPeriod.
     * renewPeriod semantic: returns a new BudgetPeriod representing the next
     * month. Immutable design means we don't mutate the current instance.
     */
    public BudgetPeriod nextPeriod() {
        LocalDate newStart = this.startDate.plusMonths(1);
        return new BudgetPeriod(newStart);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BudgetPeriod that = (BudgetPeriod) o;
        return Objects.equals(startDate, that.startDate) && Objects.equals(endDate, that.endDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(startDate, endDate);
    }

    @Override
    public String toString() {
        return "BudgetPeriod{" +
                "startDate=" + startDate +
                ", endDate=" + endDate +
                '}';
    }
}
