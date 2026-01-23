package com.apis.fintrack.domain.analysis.strategy;

import com.apis.fintrack.domain.analysis.model.kpi.AnalysisType;
import com.apis.fintrack.domain.analysis.model.metric.AnalysisMetric;
import com.apis.fintrack.domain.analysis.model.metric.MetricUnit;
import com.apis.fintrack.domain.analysis.model.metric.ScalarMetric;
import com.apis.fintrack.domain.transaction.model.Transaction;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Objects;

@Component
public class SavingsRateStrategy implements FinancialAnalysisStrategy {

    @Override
    public AnalysisType supports() {
        return AnalysisType.SAVINGS_RATE;
    }

    /**
     * Calcula la tasa de ahorro como porcentaje sobre los ingresos totales.
     */
    @Override
    public AnalysisMetric analyze(List<Transaction> transactions) {
        Objects.requireNonNull(transactions, "transactions must not be null");
        BigDecimal totalIncome = BigDecimal.ZERO;
        BigDecimal totalExpenses = BigDecimal.ZERO;

        for (Transaction t : transactions) {
            if (t.isIncome()) {
                totalIncome = totalIncome.add(t.getAmount().getValue());
            } else if (t.isExpense()) {
                totalExpenses = totalExpenses.add(t.getAmount().getValue());
            }
        }

        return getAnalysisMetric(totalIncome, totalIncome.add(totalExpenses), totalExpenses);
    }

    static AnalysisMetric getAnalysisMetric(BigDecimal totalIncome, BigDecimal subtract, BigDecimal totalExpenses) {
        Objects.requireNonNull(totalExpenses, "totalExpenses must not be null");
        Objects.requireNonNull(totalIncome, "totalIncome must not be null");
        Objects.requireNonNull(subtract, "subtract must not be null");

        if (totalIncome.compareTo(BigDecimal.ZERO) == 0) {
            // Retornamos 0% como ScalarMetric si no hay ingresos
            return new ScalarMetric(BigDecimal.ZERO, MetricUnit.PERCENT);
        }

        BigDecimal savingsRate = subtract
                .divide(totalIncome, 4, RoundingMode.HALF_EVEN)
                .multiply(BigDecimal.valueOf(100));

        return new ScalarMetric(savingsRate, MetricUnit.PERCENT);
    }
}
