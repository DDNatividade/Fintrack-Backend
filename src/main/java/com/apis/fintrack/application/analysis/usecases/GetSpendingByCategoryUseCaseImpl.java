package com.apis.fintrack.application.analysis.usecases;

import com.apis.fintrack.application.analysis.readmodel.SpendingByCategoryRM;
import com.apis.fintrack.application.analysis.port.input.GetSpendingByCategoryUseCase;
import com.apis.fintrack.domain.analysis.model.AnalysisPeriod;
import com.apis.fintrack.domain.analysis.port.output.TransactionAggregationPort;
import com.apis.fintrack.domain.transaction.model.TransactionCategoryEnum;
import com.apis.fintrack.domain.user.model.UserId;

import java.math.BigDecimal;
import java.util.Map;
import java.util.Objects;

/**
 * Application service that builds a SpendingByCategory read model.
 */
public final class GetSpendingByCategoryUseCaseImpl
        implements GetSpendingByCategoryUseCase {

    private final TransactionAggregationPort transactionAggregationPort;

    public GetSpendingByCategoryUseCaseImpl(
            TransactionAggregationPort transactionAggregationPort
    ) {
        this.transactionAggregationPort =
                Objects.requireNonNull(transactionAggregationPort);
    }

    @Override
    public SpendingByCategoryRM getSpendingByCategory(Long userId,
                                                      AnalysisPeriod period) {
        validate(userId, period);

        UserId uid = UserId.of(userId);

        Map<TransactionCategoryEnum, BigDecimal> amounts =
                transactionAggregationPort.expensesByCategory(
                        uid, period
                );

        BigDecimal totalExpenses = amounts.values().stream()
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return new SpendingByCategoryRM(amounts, totalExpenses);
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
