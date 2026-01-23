package com.apis.fintrack.application.analysis.usecases;

import com.apis.fintrack.application.analysis.port.input.GetSpendingSummaryUseCase;
import com.apis.fintrack.application.analysis.SpendingSummary;
import com.apis.fintrack.domain.analysis.model.AnalysisPeriod;
import com.apis.fintrack.domain.shared.model.Money;
import com.apis.fintrack.domain.transaction.model.TransactionCategoryEnum;
import com.apis.fintrack.domain.transaction.port.output.TransactionCountPort;
import com.apis.fintrack.domain.transaction.port.output.TransactionSummaryPort;
import com.apis.fintrack.domain.user.model.UserId;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collection;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Application-layer implementation of {@link GetSpendingSummaryUseCase}.
 * <p>
 * Responsibilities:
 * - Validate input (fail-fast).
 * - Orchestrate calls to output ports (TransactionSummaryPort) to collect aggregates.
 * - Convert domain Value Objects (Money) to DTO-friendly types and assemble a {@link SpendingSummary}.
 * <p>
 * This class contains no infrastructure code and delegates aggregation to the provided port.
 */
public final class GetSpendingSummaryUseCaseImpl implements GetSpendingSummaryUseCase {

    private final TransactionSummaryPort transactionSummaryPort;
    private final TransactionCountPort transactionCountPortPort;

    public GetSpendingSummaryUseCaseImpl(TransactionSummaryPort transactionSummaryPort, TransactionCountPort transactionCountPortPort) {
        this.transactionSummaryPort = Objects.requireNonNull(transactionSummaryPort, "transactionSummaryPort must not be null");
        this.transactionCountPortPort = transactionCountPortPort;
    }

    @Override
    public SpendingSummary getSummary(Long userId, AnalysisPeriod period) {
        validateInputs(userId, period);

        UserId uid = createUserId(userId);
        LocalDate start = period.startDate();
        LocalDate end = period.endDate();

        Map<TransactionCategoryEnum, Money> moneyByCategory = sumExpensesByCategory(uid, start, end);
        Money total = sumTotal(moneyByCategory.values());

        if (total == null || total.isZero()) {
            return SpendingSummary.empty(start, end);
        }

        Map<String, BigDecimal> breakdown = toBigDecimalMap(moneyByCategory);

        int countTransactions = transactionCountPortPort.countTransactionsByUserAndPeriod(uid, start, end);

        return new SpendingSummary(total.getAmount(), start, end, breakdown, countTransactions);
    }

    private void validateInputs(Long userId, AnalysisPeriod period) {
        if (userId == null) {
            throw new IllegalArgumentException("userId must not be null");
        }
        if (period == null) {
            throw new IllegalArgumentException("period must not be null");
        }
        LocalDate start = period.startDate();
        LocalDate end = period.endDate();
        if (start == null || end == null) {
            throw new IllegalArgumentException("period start and end must not be null");
        }
        if (start.isAfter(end)) {
            throw new IllegalArgumentException("period start must be on or before period end");
        }
    }

    private UserId createUserId(Long userId) {
        return UserId.of(userId);
    }

    private Map<TransactionCategoryEnum, Money> sumExpensesByCategory(UserId userId, LocalDate start, LocalDate end) {
        Map<TransactionCategoryEnum, Money> result = new EnumMap<>(TransactionCategoryEnum.class);
        for (TransactionCategoryEnum category : TransactionCategoryEnum.values()) {
            Money sum = transactionSummaryPort.sumExpensesByUserCategoryAndPeriod(userId, category, start, end);
            result.put(category, sum == null ? Money.zero() : sum);
        }
        return result;
    }

    private Money sumTotal(Collection<Money> values) {
        Money total = Money.zero();
        for (Money m : values) {
            total = total.add(m == null ? Money.zero() : m);
        }
        return total;
    }

    private Map<String, BigDecimal> toBigDecimalMap(Map<TransactionCategoryEnum, Money> byCategory) {
        Map<String, BigDecimal> out = new HashMap<>(byCategory.size());
        for (Map.Entry<TransactionCategoryEnum, Money> e : byCategory.entrySet()) {
            String key = e.getKey().name();
            BigDecimal value = (e.getValue() == null) ? BigDecimal.ZERO : e.getValue().getAmount();
            out.put(key, value);
        }
        return out;
    }
}

