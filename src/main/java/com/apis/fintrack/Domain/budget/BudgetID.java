package com.apis.fintrack.domain.budget;

import com.apis.fintrack.domain.transaction.model.TransactionId;
import lombok.Getter;

import java.util.Objects;

@Getter
public class BudgetID {

    private Long value;

    public BudgetID(Long value) {
        this.value = value;
    }

    public BudgetID empty() {
        this.value = null;
        return this;
    }

    public boolean isEmpty() {
        return this.value == null;
    }

    public BudgetID of(Long id) {
        if (value != null && value < 0) {
            throw new IllegalArgumentException("El ID del presupuesto no puede ser negativo");
        }
        return new BudgetID(value);


    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        BudgetID budgetID = (BudgetID) o;
        return Objects.equals(value, budgetID.value);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }

    @Override
    public String toString() {
        return value != null ? value.toString() : "NEW";
    }
}
