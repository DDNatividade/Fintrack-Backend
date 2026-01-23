package com.apis.fintrack.application.analysis.mapper;

import com.apis.fintrack.application.analysis.readmodel.BudgetUsageRM;
import com.apis.fintrack.application.analysis.readmodel.DashboardOverviewRM;
import com.apis.fintrack.application.analysis.readmodel.MonthlyCashflowRM;
import com.apis.fintrack.application.analysis.readmodel.SpendingByCategoryRM;
import com.apis.fintrack.domain.analysis.model.metric.*;
import com.apis.fintrack.domain.transaction.model.TransactionCategoryEnum;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Maps domain AnalysisMetric objects into client-facing Read Models.
 */
public final class AnalysisReadModelMapper {

    private AnalysisReadModelMapper() {
        // utility class
    }

    /* =========================
       Spending by Category
       ========================= */

    public static SpendingByCategoryRM toSpendingByCategoryRM(
            DistributionMetric metric
    ) {
        Objects.requireNonNull(metric, "metric must not be null");

        Map<TransactionCategoryEnum, BigDecimal> distribution = metric.distribution();

        BigDecimal total = distribution.values()
                .stream()
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new SpendingByCategoryRM(distribution, total);
    }

    /* =========================
       Monthly Cashflow
       ========================= */

    public static MonthlyCashflowRM toMonthlyCashflowRM(
            SeriesMetric income,
            SeriesMetric expenses
    ) {
        Objects.requireNonNull(income, "income metric must not be null");
        Objects.requireNonNull(expenses, "expenses metric must not be null");

        return new MonthlyCashflowRM(
                income.series(),
                expenses.series()
        );
    }

    /* =========================
       Budget Usage
       ========================= */

    public static BudgetUsageRM toBudgetUsageRM(
            ScalarMetric budgeted,
            ScalarMetric spent
    ) {
        Objects.requireNonNull(budgeted, "budgeted must not be null");
        Objects.requireNonNull(spent, "spent must not be null");

        BigDecimal remaining = budgeted.value().subtract(spent.value());

        BigDecimal usagePercentage = BigDecimal.ZERO;
        if (budgeted.value().compareTo(BigDecimal.ZERO) > 0) {
            usagePercentage = spent.value()
                    .divide(budgeted.value(), 4, BigDecimal.ROUND_HALF_EVEN)
                    .multiply(BigDecimal.valueOf(100));
        }

        return new BudgetUsageRM(
                budgeted.value(),
                spent.value(),
                remaining,
                usagePercentage
        );
    }

    /* =========================
       Dashboard Overview
       ========================= */

    public static DashboardOverviewRM toDashboardOverviewRM(
            SpendingByCategoryRM spendingByCategory,
            MonthlyCashflowRM monthlyCashflow,
            List<BudgetUsageRM> budgetUsage
    ) {
        return new DashboardOverviewRM(
                spendingByCategory,
                monthlyCashflow,
                budgetUsage
        );
    }
}
