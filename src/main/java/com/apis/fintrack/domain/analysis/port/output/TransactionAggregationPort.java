package com.apis.fintrack.domain.analysis.port.output;

import com.apis.fintrack.domain.analysis.model.AnalysisPeriod;
import com.apis.fintrack.domain.transaction.model.TransactionCategoryEnum;
import com.apis.fintrack.domain.user.model.UserId;

import java.math.BigDecimal;
import java.time.YearMonth;
import java.util.Map;

/**
 * Output port for retrieving aggregated transaction data
 * required by financial analysis and KPI computation.
 * Implementations are responsible for efficient aggregation
 * (e.g. database-level grouping).
 */
public interface TransactionAggregationPort {

    /**
     * Total expenses within a period.
     */
    BigDecimal totalExpenses(UserId userId, AnalysisPeriod period);

    /**
     * Total income within a period.
     */
    BigDecimal totalIncome(UserId userId, AnalysisPeriod period);

    /**
     * Expenses grouped by category.
     * Used for pie / bar charts.
     */
    Map<TransactionCategoryEnum, BigDecimal> expensesByCategory(
            UserId userId,
            AnalysisPeriod period
    );

    /**
     * Monthly expenses time series.
     * Used for trends and line charts.
     */
    Map<YearMonth, BigDecimal> monthlyExpenses(
            UserId userId,
            AnalysisPeriod period
    );

    /**
     * Monthly income time series.
     */
    Map<YearMonth, BigDecimal> monthlyIncome(
            UserId userId,
            AnalysisPeriod period
    );
}
