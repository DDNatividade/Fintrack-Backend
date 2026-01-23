package com.apis.fintrack.domain.budget.exception;

/**
 * Exception thrown when a budget is not found.
 */
public class BudgetNotFoundException extends RuntimeException {

    public BudgetNotFoundException(Long budgetId) {
        super("Budget not found with ID: " + budgetId);
    }

    public BudgetNotFoundException(String message) {
        super(message);
    }
}

