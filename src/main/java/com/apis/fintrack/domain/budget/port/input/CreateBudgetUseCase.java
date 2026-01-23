package com.apis.fintrack.domain.budget.port.input;

import com.apis.fintrack.domain.budget.model.Budget;
import com.apis.fintrack.domain.shared.model.Money;
import com.apis.fintrack.domain.transaction.model.TransactionCategoryEnum;
import com.apis.fintrack.domain.user.model.UserId;

/**
 * Input port for the budget creation use case.
 * Defines the contract that the application layer must implement
 * to create new budgets in the system.
 */
public interface CreateBudgetUseCase {

    /**
     * Command that encapsulates the data needed to create a budget.
     *
     * @param userId the owner's user ID
     * @param limitAmount the budget limit amount
     * @param category the transaction category for this budget
     */
    record CreateBudgetCommand(
            UserId userId,
            Money limitAmount,
            TransactionCategoryEnum category
    ) {}

    /**
     * Creates a new budget for a user.
     *
     * @param command the command with budget data
     * @return the created budget with assigned ID
     * @throws com.apis.fintrack.domain.budget.exception.BudgetWithCategoryAlreadyExists if a budget already exists for the category
     */
    Budget create(CreateBudgetCommand command);
}

