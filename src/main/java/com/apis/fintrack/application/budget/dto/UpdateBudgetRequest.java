package com.apis.fintrack.application.budget.dto;

import java.math.BigDecimal;

/**
 * Primitive DTO for update budget operations.
 * Fields are nullable to indicate optional updates.
 *
 * @param newLimitAmount nullable
 * @param newCategory    nullable
 */
public record UpdateBudgetRequest(Long budgetId, BigDecimal newLimitAmount, String newCategory) {

}

