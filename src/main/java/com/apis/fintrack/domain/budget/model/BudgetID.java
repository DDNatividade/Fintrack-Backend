package com.apis.fintrack.domain.budget.model;

import java.util.Objects;

/**
 * Value Object for Budget identifier.
 * Immutable and simple wrapper around a Long value.
 */
public record BudgetID(Long value) {

    public BudgetID {
        if (value != null && value < 0) {
            throw new IllegalArgumentException("Budget id cannot be negative");
        }
    }

    public static BudgetID of(Long id) {
        return new BudgetID(id);
    }

    public static BudgetID empty() {
        return new BudgetID(null);
    }

    public boolean isEmpty() {
        return this.value == null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BudgetID budgetID = (BudgetID) o;
        return Objects.equals(value, budgetID.value);
    }

    @Override
    public String toString() {
        return value != null ? value.toString() : "NEW";
    }
}
