package com.apis.fintrack.domain.analysis.strategy;

import com.apis.fintrack.domain.analysis.model.AnalysisPeriod;
import com.apis.fintrack.domain.transaction.model.TransactionCategoryEnum;
import com.apis.fintrack.domain.transaction.model.Transaction;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

@Component
public class SpendingTrendStrategy implements FinancialAnalysisStrategy {
    @Override
    public BigDecimal analyze
            (List<Transaction> transactions) {
        LocalDate thirtyDaysAgo = LocalDate.now().minusDays(30);
        LocalDate sixtyDaysAgo = LocalDate.now().minusDays(60);
        AnalysisPeriod period = new AnalysisPeriod(thirtyDaysAgo, LocalDate.now());

        BigDecimal recentExpenses = BigDecimal.ZERO;
        BigDecimal previousExpenses = BigDecimal.ZERO;

        for (Transaction t : transactions) {
            if (t.isExpense()) {
                LocalDate date = t.getTransactionDateValue();

                if (date.isAfter(thirtyDaysAgo)) {
                    recentExpenses = recentExpenses.add(t.getAbsoluteAmount());
                } else if (date.isAfter(sixtyDaysAgo) && date.isBefore(thirtyDaysAgo)) {
                    previousExpenses = previousExpenses.add(t.getAbsoluteAmount());
                }
            }
        }

        if (previousExpenses.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }

        BigDecimal difference = recentExpenses.subtract(previousExpenses);
        return difference.divide(previousExpenses, 4, RoundingMode.HALF_EVEN)
                .multiply(BigDecimal.valueOf(100));
    }

    @Override
    public BigDecimal analyze(List<Transaction> transactions, TransactionCategoryEnum category) {
        return FinancialAnalysisStrategy.super.analyze(transactions, category);
    }

}

