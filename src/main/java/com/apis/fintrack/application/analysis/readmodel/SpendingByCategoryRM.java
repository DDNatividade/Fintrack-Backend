package com.apis.fintrack.application.analysis.readmodel;

import com.apis.fintrack.domain.transaction.model.TransactionCategoryEnum;
import java.math.BigDecimal;
import java.util.Map;

/**
 * Read model representing spending distribution by category.
 * Optimized for pie / bar charts.
 */
public record SpendingByCategoryRM(
        Map<TransactionCategoryEnum, BigDecimal> amounts,
        BigDecimal totalExpenses
) {
}
