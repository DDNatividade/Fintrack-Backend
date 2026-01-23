package com.apis.fintrack.domain.analysis.strategy;

import com.apis.fintrack.domain.analysis.model.kpi.AnalysisType;
import com.apis.fintrack.domain.analysis.model.metric.AnalysisMetric;
import com.apis.fintrack.domain.analysis.model.metric.MetricUnit;
import com.apis.fintrack.domain.analysis.model.metric.ScalarMetric;
import com.apis.fintrack.domain.transaction.model.Transaction;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;

@Component
public class MonthlyAverageStrategy implements FinancialAnalysisStrategy {

    @Override
    public AnalysisType supports() {
        return AnalysisType.MONTHLY_AVERAGE_EXPENSES;
    }

    @Override
    public AnalysisMetric analyze(List<Transaction> transactions) {
        Objects.requireNonNull(transactions, "transactions must not be null");
        BigDecimal totalExpenses = BigDecimal.ZERO;
        LocalDate oldestDate = LocalDate.now();

        for (Transaction t : transactions) {
            if (t.isExpense()) {
                totalExpenses = totalExpenses.add(t.getAmount().getValue());
                if (t.getTransactionDate().getValue().isBefore(oldestDate)) {
                    oldestDate = t.getTransactionDate().getValue();
                }
            }
        }

        long monthsBetween = ChronoUnit.MONTHS.between(oldestDate, LocalDate.now());
        if (monthsBetween == 0) monthsBetween = 1;

        BigDecimal average = totalExpenses.divide(BigDecimal.valueOf(monthsBetween), 2);

        // Devuelve un ScalarMetric en lugar de un BigDecimal
        return new ScalarMetric(average, MetricUnit.CURRENCY);
    }


}
