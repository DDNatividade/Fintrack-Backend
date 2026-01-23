package com.apis.fintrack.domain.budget.port.input;

import com.apis.fintrack.domain.budget.model.Budget;
import com.apis.fintrack.domain.transaction.model.TransactionCategoryEnum;
import java.math.BigDecimal;

/**
 * Input port for the budget update use case.
 * Defines the contract that the application layer must implement
 * to update existing budgets in the system.
 */
public interface UpdateBudgetUseCase {

    /**
     * Command that encapsulates the data needed to update a budget.
     *
     * @param budgetId the budget ID to update
     * @param newLimitAmount the new limit amount (optional, null to keep current)
     * @param newCategory the new category (optional, null to keep current)
     */
    record UpdateBudgetCommand(
        Long budgetId,
        BigDecimal newLimitAmount,
        String newCategory
    ) {}

    /**
     * Updates a budget's limit.
     *
     * @param budgetId the budget ID
     * @param newLimit the new limit amount
     * @return the updated budget
     * @throws com.apis.fintrack.domain.budget.exception.BudgetNotFoundException if not found
     */
    Budget updateLimit(Long budgetId, BigDecimal newLimit);

    /**
     * Updates a budget's category.
     *
     * @param budgetId the budget ID
     * @param newCategory the new category
     * @return the updated budget
     * @throws com.apis.fintrack.domain.budget.exception.BudgetNotFoundException if not found
     * @throws com.apis.fintrack.domain.budget.exception.BudgetWithCategoryAlreadyExists if category already has a budget
     */
    Budget updateCategory(Long budgetId, TransactionCategoryEnum newCategory);

    /**
     * Updates a budget using the command.
     *
     * @param command the update command
     * @return the updated budget
     */
    Budget update(UpdateBudgetCommand command);
}

