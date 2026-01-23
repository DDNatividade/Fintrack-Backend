package com.apis.fintrack.application.analysis.usecases;

import com.apis.fintrack.application.analysis.readmodel.BudgetUsageRM;
import com.apis.fintrack.application.analysis.port.input.GetBudgetUsageUseCase;
import com.apis.fintrack.domain.analysis.model.AnalysisPeriod;
import com.apis.fintrack.domain.analysis.port.output.BudgetAggregationPort;
import com.apis.fintrack.domain.transaction.model.TransactionCategoryEnum;
import com.apis.fintrack.domain.user.model.UserId;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public final class GetBudgetUsageUseCaseImpl
        implements GetBudgetUsageUseCase {

    private final BudgetAggregationPort budgetAggregationPort;

    public GetBudgetUsageUseCaseImpl(
            BudgetAggregationPort budgetAggregationPort
    ) {
        this.budgetAggregationPort = Objects.requireNonNull(budgetAggregationPort);
    }

    @Override
    public BudgetUsageRM getBudgetUsage(
            Long userId,
            TransactionCategoryEnum category,
            AnalysisPeriod period
    ) {
        validate(userId, category, period);

        UserId uid = UserId.of(userId);

        BigDecimal spent = budgetAggregationPort.totalSpentBudget(
                uid,
                category,
                period
        );

        BigDecimal limit = budgetAggregationPort.getBudgetLimit(uid, category);
        BigDecimal remain = limit.subtract(spent);

        BigDecimal usage = calculateUsage(spent, limit);

        return new BudgetUsageRM(
                limit,
                spent,
                remain,
                usage
        );
    }

    private BigDecimal calculateUsage(BigDecimal spent, BigDecimal limit) {
        if (limit == null || limit.signum() <= 0) {
            return BigDecimal.ZERO;
        }
        return spent
                .divide(limit, 4, RoundingMode.HALF_EVEN)
                .multiply(BigDecimal.valueOf(100));
    }

    private void validate(
            Long userId,
            TransactionCategoryEnum category,
            AnalysisPeriod period
    ) {
        if (userId == null) {
            throw new IllegalArgumentException("userId must not be null");
        }
        if (category == null) {
            throw new IllegalArgumentException("category must not be null");
        }
        if (period == null || period.startDate().isAfter(period.endDate())) {
            throw new IllegalArgumentException("Invalid analysis period");
        }
    }
}
