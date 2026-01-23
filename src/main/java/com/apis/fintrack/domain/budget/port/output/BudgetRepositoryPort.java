package com.apis.fintrack.domain.budget.port.output;


import com.apis.fintrack.domain.budget.model.Budget;
import com.apis.fintrack.domain.budget.model.BudgetID;
import com.apis.fintrack.domain.transaction.model.TransactionCategoryEnum;
import com.apis.fintrack.domain.user.model.UserId;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Output port for Budget persistence.
 *
 * Defines the contract that the infrastructure layer must implement
 * to persist and retrieve budgets.
 *
 * This interface is PURE domain, without framework dependencies.
 */
public interface BudgetRepositoryPort {

    /**
     * Saves a budget (new or existing).
     *
     * @param budget the budget to save
     * @return the saved budget with assigned ID
     */
    Budget save(Budget budget);

    /**
     * Finds a budget by its ID.
     *
     * @param id the budget ID
     * @return Optional with the budget if exists
     */
    Optional<Budget> findById(BudgetID id);

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
     * @return Optional with the budget if exists
     */
    Optional<Budget> findByUserIdAndCategory(UserId userId, TransactionCategoryEnum category);

    /**
     * Finds all budgets for a user within a specific period.
     *
     * @param userId the user ID
     * @param startDate period start date
     * @param endDate period end date
     * @return list of budgets in the period
     */
    List<Budget> findByUserIdAndPeriod(UserId userId, LocalDate startDate, LocalDate endDate);

    /**
     * Finds the current month's budget for a user and category.
     *
     * @param userId the user ID
     * @param category the transaction category
     * @return Optional with the current budget if exists
     */
    Optional<Budget> findCurrentBudget(UserId userId, TransactionCategoryEnum category);

    /**
     * Deletes a budget by its ID.
     *
     * @param id the budget ID
     */
    void deleteById(BudgetID id);

    /**
     * Deletes all budgets for a user.
     *
     * @param userId the user ID
     */
    void deleteAllByUserId(UserId userId);

    /**
     * Checks if a budget exists with the given ID.
     *
     * @param id the ID to check
     * @return true if exists
     */
    boolean existsById(BudgetID id);

    /**
     * Checks if a budget exists for a user and category.
     *
     * @param userId the user ID
     * @param category the transaction category
     * @return true if exists
     */
    boolean existsByUserIdAndCategory(UserId userId, TransactionCategoryEnum category);
}
