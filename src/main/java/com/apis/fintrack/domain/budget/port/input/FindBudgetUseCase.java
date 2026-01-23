package com.apis.fintrack.domain.budget.port.input;

import com.apis.fintrack.domain.budget.model.Budget;
import com.apis.fintrack.domain.budget.model.BudgetID;
import com.apis.fintrack.domain.transaction.model.TransactionCategoryEnum;
import com.apis.fintrack.domain.user.model.UserId;

import java.util.List;

/**
 * Input port for the budget search use case.
 *
 * Defines the contract that the application layer must implement
 * to search budgets in the system.
 */
public interface FindBudgetUseCase {

    /**
     * Finds a budget by its ID.
     *
     * @param budgetId the budget ID
     * @return the found budget
     * @throws com.apis.fintrack.domain.budget.exception.BudgetNotFoundException if not found
     */
    Budget findById(BudgetID budgetId);

    /**
     * Finds all budgets for a user.
     *
     * @param userId the user ID
     * @return list of user's budgets
     */
    List<Budget> findByUserId(UserId userId);

    /**
     * Finds a budget by user and category.
     *
     * @param userId the user ID
     * @param category the transaction category
     * @return the found budget
     * @throws com.apis.fintrack.domain.budget.exception.BudgetNotFoundException if not found
     */
    Budget findByUserIdAndCategory(UserId userId, TransactionCategoryEnum category);

    /**
     * Finds all active budgets for a user (current period).
     *
     * @param userId the user ID
     * @return list of active budgets
     */
    List<Budget> findActiveBudgetsByUserId(UserId userId);
}

