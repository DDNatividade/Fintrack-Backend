package com.apis.fintrack.domain.analysis.strategy;

import com.apis.fintrack.domain.analysis.model.kpi.AnalysisType;
import com.apis.fintrack.domain.analysis.model.metric.AnalysisMetric;
import com.apis.fintrack.domain.analysis.model.metric.MetricUnit;
import com.apis.fintrack.domain.analysis.model.metric.ScalarMetric;
import com.apis.fintrack.domain.transaction.model.Transaction;
import com.apis.fintrack.domain.transaction.model.TransactionCategoryEnum;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Component
public class SpendingTrendStrategy implements FinancialAnalysisStrategy {

    private static final int TREND_WINDOW_DAYS = 30;

    @Override
    public AnalysisType supports() {
        return AnalysisType.SPENDING_TREND;
    }

    @Override
    public AnalysisMetric analyze(List<Transaction> transactions) {
        Objects.requireNonNull(transactions, "transactions must not be null");
        LocalDate now = LocalDate.now();
        LocalDate recentStart = now.minusDays(TREND_WINDOW_DAYS);
        LocalDate previousStart = now.minusDays(2 * TREND_WINDOW_DAYS);
        LocalDate previousEnd = recentStart;

        BigDecimal recentExpenses = sumExpenses(transactions, recentStart, now);
        BigDecimal previousExpenses = sumExpenses(transactions, previousStart, previousEnd);

        return calculateTrendPercentage(recentExpenses, previousExpenses);
    }

    @Override
    public AnalysisMetric analyze(List<Transaction> transactions, TransactionCategoryEnum category) {
        Objects.requireNonNull(transactions, "transactions must not be null");
        Objects.requireNonNull(category, "category must not be null");

        List<Transaction> filtered = transactions.stream()
                .filter(t -> t.getCategory().getValue().equals(category))
                .toList();
        return analyze(filtered);
    }

    /**
     * Calculates the spending trend percentage between two periods.
     * <p>
     * Formula: ((recentExpenses - previousExpenses) / |previousExpenses|) * 100
     * <p>
     * Since expenses are stored as negative values (e.g., -100 for 100€ spent),
     * we use absolute values for the calculation to get intuitive results:
     * - Positive result = spending increased
     * - Negative result = spending decreased
     * - Zero = no change
     *
     * @param recentExpenses   total expenses in the recent period (negative value)
     * @param previousExpenses total expenses in the previous period (negative value)
     * @return ScalarMetric with the trend percentage
     */
    private AnalysisMetric calculateTrendPercentage(BigDecimal recentExpenses, BigDecimal previousExpenses) {
        // If no previous expenses, we can't calculate a trend (avoid division by zero)
        if (previousExpenses.compareTo(BigDecimal.ZERO) == 0) {
            return new ScalarMetric(BigDecimal.ZERO, MetricUnit.PERCENT);
        }

        // Convert to absolute values for intuitive calculation
        // Expenses are stored as negative, so we negate them to get positive values
        BigDecimal recentAbsolute = recentExpenses.abs();
        BigDecimal previousAbsolute = previousExpenses.abs();

        // Calculate change: (recent - previous)
        BigDecimal change = recentAbsolute.subtract(previousAbsolute);

        // Calculate percentage: (change / previous) * 100
        BigDecimal trendPercentage = change
                .divide(previousAbsolute, 4, RoundingMode.HALF_EVEN)
                .multiply(BigDecimal.valueOf(100));

        return new ScalarMetric(trendPercentage, MetricUnit.PERCENT);
    }

    /**
     * Suma los gastos de una lista de transacciones dentro de un rango de fechas.
     */
    private BigDecimal sumExpenses(List<Transaction> transactions, LocalDate start, LocalDate end) {
        Objects.requireNonNull(transactions, "transactions must not be null");
        Objects.requireNonNull(start, "start date must not be null");
        Objects.requireNonNull(end, "end date must not be null");

        return transactions.stream()
                .filter(Transaction::isExpense)
                .filter(t -> {
                    LocalDate date = t.getTransactionDate().getValue();
                    return (date.isEqual(start) || date.isAfter(start)) &&
                            (date.isEqual(end) || date.isBefore(end));
                })
                .map(t -> t.getAmount().getValue())
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

}
