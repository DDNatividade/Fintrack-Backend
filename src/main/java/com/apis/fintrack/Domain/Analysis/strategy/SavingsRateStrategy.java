package com.apis.fintrack.domain.analysis.strategy;

import com.apis.fintrack.domain.transaction.model.TransactionCategoryEnum;
import com.apis.fintrack.domain.transaction.model.Transaction;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Component
public class SavingsRateStrategy implements FinancialAnalysisStrategy {
    @Override
    public BigDecimal analyze(List<Transaction> transactions) {
        BigDecimal totalIncome = BigDecimal.ZERO;
        BigDecimal totalExpenses = BigDecimal.ZERO;

        for (Transaction t : transactions) {
            if (t.isIncome()) {
                totalIncome = totalIncome.add(t.getAbsoluteAmount());
            } else if (t.isExpense()) {
                totalExpenses = totalExpenses.add(t.getAbsoluteAmount());
            }
        }

        if (totalIncome.compareTo(BigDecimal.ZERO) == 0) {
            return BigDecimal.ZERO;
        }

        BigDecimal savings = totalIncome.subtract(totalExpenses);
        return savings.divide(totalIncome, 4, RoundingMode.HALF_EVEN)
                .multiply(BigDecimal.valueOf(100));
    }

    @Override
    public BigDecimal analyze(List<Transaction> transactions, TransactionCategoryEnum category) {
        return FinancialAnalysisStrategy.super.analyze(transactions, category);
    }
}

