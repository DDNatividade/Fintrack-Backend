package com.apis.fintrack.application.analysis.readmodel;

import java.math.BigDecimal;

/**
 * Read model representing budget usage.
 */
public record BudgetUsageRM(
        BigDecimal budgeted,
        BigDecimal spent,
        BigDecimal remaining,
        BigDecimal usagePercentage
) {
}
