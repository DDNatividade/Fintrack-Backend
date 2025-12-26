package com.apis.fintrack.domain.analysis.strategy;

import com.apis.fintrack.domain.transaction.model.TransactionCategoryEnum;
import com.apis.fintrack.domain.transaction.model.Transaction;
import org.springframework.stereotype.Component;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Component
public class MonthlyAverageStrategy implements FinancialAnalysisStrategy{

    @Override
    public BigDecimal analyze(List<Transaction> transactions) {
        BigDecimal totalExpenses = BigDecimal.ZERO;
        LocalDate oldestDate = LocalDate.now();
        for (Transaction t : transactions) {
            if (t.isExpense()) {
                totalExpenses = totalExpenses.add(t.getAbsoluteAmount());
                if (t.getTransactionDateValue().isBefore(oldestDate)) {
                    oldestDate = t.getTransactionDateValue();
                }
            }
        }

        long monthsBetween = ChronoUnit.MONTHS.between(oldestDate, LocalDate.now());
        if (monthsBetween == 0) monthsBetween = 1;

        return totalExpenses.divide(BigDecimal.valueOf(monthsBetween),
                RoundingMode.FLOOR);
    }




    @Override
    public BigDecimal analyze(List<Transaction> transactions, TransactionCategoryEnum category) {
        return FinancialAnalysisStrategy.super.analyze(transactions, category);
    }



}
