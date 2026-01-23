package com.apis.fintrack.domain.analysis.strategy;

import com.apis.fintrack.domain.analysis.model.kpi.AnalysisType;
import com.apis.fintrack.domain.analysis.model.metric.AnalysisMetric;
import com.apis.fintrack.domain.transaction.model.Transaction;
import com.apis.fintrack.domain.transaction.model.TransactionCategoryEnum;
import java.util.List;

public interface FinancialAnalysisStrategy {

    AnalysisType supports();


    /**
     * Ejecuta un análisis sobre la lista de transacciones y devuelve un AnalysisMetric.
     */
    AnalysisMetric analyze(List<Transaction> transactions);

    /**
     * Filtra las transacciones por categoría y luego ejecuta el análisis.
     */
    default AnalysisMetric analyze(List<Transaction> transactions, TransactionCategoryEnum category) {
        List<Transaction> filtered = transactions.stream()
                .filter(t -> t.getCategory().equals(category))
                .toList();
        return analyze(filtered);
    }
}

