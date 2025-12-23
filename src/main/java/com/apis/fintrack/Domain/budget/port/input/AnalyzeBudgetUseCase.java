package com.apis.fintrack.domain.budget.port.input;

import com.apis.fintrack.domain.shared.model.Money;
import com.apis.fintrack.domain.transaction.model.TransactionCategoryEnum;

import java.math.BigDecimal;

/**
 * Input port for the budget analysis use case.
 *
 * Defines the contract that the application layer must implement
 * to analyze budget usage and status.
 */
public interface AnalyzeBudgetUseCase {

    /**
     * Result of a budget analysis.
     *
     * @param budgetId the budget ID
     * @param category the budget category
     * @param limit the budget limit
     * @param spent the amount spent
     * @param remaining the remaining amount
     * @param usagePercentage the usage percentage (0-100)
     * @param isExceeded whether the budget is exceeded
     * @param isNearLimit whether the budget is near limit (>= 80%)
     */
    record BudgetAnalysisResult(
        Long budgetId,
        TransactionCategoryEnum category,
        BigDecimal limit,
        BigDecimal spent,
        BigDecimal remaining,
        BigDecimal usagePercentage,
        boolean isExceeded,
        boolean isNearLimit
    ) {}

    /**
     * Checks if a budget is exceeded.
     *
     * @param budgetId the budget ID
     * @return true if exceeded
     * @throws com.apis.fintrack.domain.budget.exception.BudgetNotFoundException if not found
     */
    boolean isBudgetExceeded(Long budgetId);

    /**
     * Gets the usage percentage of a budget.
     *
     * @param budgetId the budget ID
     * @return the usage percentage (0-100)
     * @throws com.apis.fintrack.domain.budget.exception.BudgetNotFoundException if not found
     */
    BigDecimal getBudgetUsagePercentage(Long budgetId);

    /**
     * Gets the remaining amount of a budget.
     *
     * @param budgetId the budget ID
     * @return the remaining amount
     * @throws com.apis.fintrack.domain.budget.exception.BudgetNotFoundException if not found
     */
    Money getRemainingAmount(Long budgetId);

    /**
     * Gets a complete analysis of a budget.
     *
     * @param budgetId the budget ID
     * @return the analysis result
     * @throws com.apis.fintrack.domain.budget.exception.BudgetNotFoundException if not found
     */
    BudgetAnalysisResult analyzeBudget(Long budgetId);

    /**
     * Gets analysis for all budgets of a user.
     *
     * @param userId the user ID
     * @return list of analysis results for each budget
     */
    java.util.List<BudgetAnalysisResult> analyzeAllBudgetsByUserId(Long userId);
}

