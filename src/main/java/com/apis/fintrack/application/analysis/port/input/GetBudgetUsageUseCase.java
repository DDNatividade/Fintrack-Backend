package com.apis.fintrack.application.analysis.port.input;

import com.apis.fintrack.application.analysis.readmodel.BudgetUsageRM;
import com.apis.fintrack.domain.analysis.model.AnalysisPeriod;
import com.apis.fintrack.domain.transaction.model.TransactionCategoryEnum;

/**
 * Use case to retrieve budget usage information for a user.
 */
public interface GetBudgetUsageUseCase {

    /**
     * Returns budget usage for a user in a given period.
     *
     * @param userId user identifier
     * @param period analysis period
     * @param category transaction category
     * @return budget usage read model
     */
    BudgetUsageRM getBudgetUsage(Long userId, TransactionCategoryEnum category,
                                 AnalysisPeriod period);
}
