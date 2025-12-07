package com.apis.fintrack.domain.Analysis.strategy;

import com.apis.fintrack.domain.transaction.model.TransactionCategoryEnum;
import com.apis.fintrack.domain.transaction.model.Transaction;
import java.math.BigDecimal;
import java.util.List;

public interface FinancialAnalysisStrategy {
    BigDecimal analyze(List<Transaction> transactions);

    /*FILTRAMOS POR CATEGORÃA PARA POSTERIORMENTE APLICAR EL ALGORITMO DE ANALYSIS*/
    default BigDecimal analyze(List<Transaction> transactions, TransactionCategoryEnum category) {
        List<Transaction> filtered = transactions.stream()
                .filter(t -> t.getCategoryValue() == category)
                .toList();
        return analyze(filtered);
    }
}

