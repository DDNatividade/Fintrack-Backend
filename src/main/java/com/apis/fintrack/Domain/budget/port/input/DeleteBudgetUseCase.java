package com.apis.fintrack.domain.budget.port.input;

/**
 * Input port for the budget deletion use case.
 *
 * Defines the contract that the application layer must implement
 * to delete budgets from the system.
 */
public interface DeleteBudgetUseCase {

    /**
     * Deletes a budget by its ID.
     *
     * @param budgetId the budget ID to delete
     * @throws com.apis.fintrack.domain.budget.exception.BudgetNotFoundException if not found
     */
    void deleteById(Long budgetId);

    /**
     * Deletes all budgets for a user.
     *
     * @param userId the user ID
     */
    void deleteAllByUserId(Long userId);
}

