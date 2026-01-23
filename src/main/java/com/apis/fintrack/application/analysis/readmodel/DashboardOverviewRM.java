package com.apis.fintrack.application.analysis.readmodel;

import java.util.List;

/**
 * Read model aggregating all dashboard KPIs and charts.
 */
public record DashboardOverviewRM(
        SpendingByCategoryRM spendingByCategory,
        MonthlyCashflowRM monthlyCashflow,
        List<BudgetUsageRM> budgetUsage
) {
}
