package com.apis.fintrack.application.analysis.usecases;

import com.apis.fintrack.application.analysis.port.input.GetBudgetUsageUseCase;
import com.apis.fintrack.application.analysis.port.input.GetDashboardOverviewUseCase;
import com.apis.fintrack.application.analysis.port.input.GetMonthlyCashflowUseCase;
import com.apis.fintrack.application.analysis.port.input.GetSpendingByCategoryUseCase;
import com.apis.fintrack.application.analysis.readmodel.*;
import com.apis.fintrack.domain.analysis.model.AnalysisPeriod;
import com.apis.fintrack.domain.transaction.model.TransactionCategoryEnum;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * Application-layer use case that aggregates multiple analysis read models
 * into a single dashboard overview.
 * This class contains no business logic and delegates all calculations
 * to specialized use cases.
 */
public final class GetDashboardOverviewUseCaseImpl
        implements GetDashboardOverviewUseCase {

    private final GetSpendingByCategoryUseCase spendingByCategoryUseCase;
    private final GetMonthlyCashflowUseCase monthlyCashflowUseCase;
    private final GetBudgetUsageUseCase budgetUsageUseCase;

    public GetDashboardOverviewUseCaseImpl(
            GetSpendingByCategoryUseCase spendingByCategoryUseCase,
            GetMonthlyCashflowUseCase monthlyCashflowUseCase,
            GetBudgetUsageUseCase budgetUsageUseCase
    ) {
        this.spendingByCategoryUseCase = Objects.requireNonNull(spendingByCategoryUseCase);
        this.monthlyCashflowUseCase = Objects.requireNonNull(monthlyCashflowUseCase);
        this.budgetUsageUseCase = Objects.requireNonNull(budgetUsageUseCase);
    }

    @Override
    public DashboardOverviewRM getOverview(Long userId, AnalysisPeriod period) {
        validate(userId, period);

        SpendingByCategoryRM spendingByCategory =
                spendingByCategoryUseCase.getSpendingByCategory(userId, period);

        MonthlyCashflowRM monthlyCashflow =
                monthlyCashflowUseCase.getMonthlyCashflow(userId, period);

        List<BudgetUsageRM> budgetUsage =
                loadBudgetUsage(userId, period);

        return new DashboardOverviewRM(
                spendingByCategory,
                monthlyCashflow,
                budgetUsage
        );
    }

    private List<BudgetUsageRM> loadBudgetUsage(Long userId, AnalysisPeriod period) {
        return Arrays.stream(TransactionCategoryEnum.values())
                .filter(category -> category != TransactionCategoryEnum.SALARY)
                .map(category ->
                        budgetUsageUseCase.getBudgetUsage(userId, category, period)
                )
                .toList();
    }

    private void validate(Long userId, AnalysisPeriod period) {
        if (userId == null) {
            throw new IllegalArgumentException("userId must not be null");
        }
        if (period == null || period.startDate().isAfter(period.endDate())) {
            throw new IllegalArgumentException("Invalid analysis period");
        }
    }
}
