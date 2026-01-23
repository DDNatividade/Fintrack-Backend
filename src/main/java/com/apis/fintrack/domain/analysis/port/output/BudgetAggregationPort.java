package com.apis.fintrack.domain.analysis.port.output;

import com.apis.fintrack.domain.analysis.model.AnalysisPeriod;
import com.apis.fintrack.domain.transaction.model.TransactionCategoryEnum;
import com.apis.fintrack.domain.user.model.UserId;

import java.math.BigDecimal;
import java.util.Map;

/**
 * Output port for retrieving aggregated budget data
 * needed for financial analysis.
 */
public interface BudgetAggregationPort {

    /**
     * Budgeted amount by category.
     */
    Map<String, BigDecimal> budgetByCategory(
            UserId userId,
            TransactionCategoryEnum category,
            AnalysisPeriod period
    );

    /**
     * Remaining budget (budget - actual expenses).
     */
    BigDecimal remainingBudget(UserId userId,
                               TransactionCategoryEnum category,
                               AnalysisPeriod period);

    /**
     * Total spent budget in the given period.
     */
    BigDecimal totalSpentBudget(UserId userId,
                               TransactionCategoryEnum category,
                               AnalysisPeriod period);



    BigDecimal getBudgetLimit(UserId userId,
                           TransactionCategoryEnum category);
}
