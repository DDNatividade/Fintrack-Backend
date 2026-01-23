package com.apis.fintrack.application.analysis.usecases;

import com.apis.fintrack.application.analysis.readmodel.MonthlyCashflowRM;
import com.apis.fintrack.application.analysis.port.input.GetMonthlyCashflowUseCase;
import com.apis.fintrack.domain.analysis.model.AnalysisPeriod;
import com.apis.fintrack.domain.analysis.port.output.TransactionAggregationPort;
import com.apis.fintrack.domain.user.model.UserId;
import java.time.YearMonth;
import java.util.Map;
import java.util.Objects;

/**
 * Application-layer implementation of {@link GetMonthlyCashflowUseCase}.
 *
 * Responsibilities:
 * - Validate inputs
 * - Orchestrate aggregation ports
 * - Assemble MonthlyCashflowRM
 */
public final class GetMonthlyCashflowUseCaseImpl
        implements GetMonthlyCashflowUseCase {

    private final TransactionAggregationPort transactionAggregationPort;

    public GetMonthlyCashflowUseCaseImpl(
            TransactionAggregationPort transactionAggregationPort
    ) {
        this.transactionAggregationPort =
                Objects.requireNonNull(transactionAggregationPort);
    }

    @Override
    public MonthlyCashflowRM getMonthlyCashflow(
            Long userId,
            AnalysisPeriod period
    ) {
        validate(userId, period);

        UserId uid = UserId.of(userId);


        Map<YearMonth, java.math.BigDecimal> income =
                transactionAggregationPort.monthlyIncome(uid, period);

        Map<YearMonth, java.math.BigDecimal> expenses =
                transactionAggregationPort.monthlyExpenses(uid, period);

        return new MonthlyCashflowRM(income, expenses);
    }

    private void validate(Long userId, AnalysisPeriod period) {
        if (userId == null) {
            throw new IllegalArgumentException("userId must not be null");
        }
        if (period == null) {
            throw new IllegalArgumentException("period must not be null");
        }
        if (period.startDate().isAfter(period.endDate())) {
            throw new IllegalArgumentException("Invalid analysis period");
        }
    }
}
