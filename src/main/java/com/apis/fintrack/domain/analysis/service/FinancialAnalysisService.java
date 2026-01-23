package com.apis.fintrack.domain.analysis.service;

import com.apis.fintrack.domain.analysis.model.kpi.AnalysisResult;
import com.apis.fintrack.domain.analysis.model.kpi.AnalysisType;
import com.apis.fintrack.domain.analysis.model.metric.AnalysisMetric;
import com.apis.fintrack.domain.analysis.model.metric.ScalarMetric;
import com.apis.fintrack.domain.analysis.port.Input.IFinancialAnalysisUseCase;
import com.apis.fintrack.domain.analysis.strategy.FinancialAnalysisStrategy;
import com.apis.fintrack.domain.analysis.strategy.MonthlyAverageStrategy;
import com.apis.fintrack.domain.analysis.strategy.SavingsRateStrategy;
import com.apis.fintrack.domain.analysis.strategy.SpendingTrendStrategy;
import com.apis.fintrack.domain.transaction.model.Transaction;
import org.springframework.stereotype.Service;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Implementación del caso de uso de análisis financiero.
 * Arquitectura Hexagonal: Adaptador (Implementación de Puerto de Entrada)
 * Gestiona el registro de estrategias de análisis y orquesta su ejecución.
 */
@Service
public class FinancialAnalysisService implements IFinancialAnalysisUseCase {

    private final Map<AnalysisType, FinancialAnalysisStrategy> strategies;


    public FinancialAnalysisService(List<FinancialAnalysisStrategy> strategies) {
        Objects.requireNonNull(strategies, "Strategies list must not be null");
        if (strategies.isEmpty()) {
            throw new IllegalArgumentException("At least one strategy must be provided");
        }
        this.strategies = strategies.stream()
                .collect(Collectors.toUnmodifiableMap(
                        FinancialAnalysisStrategy::supports,
                        Function.identity()
                    ));
        }


        /**
     * Ejecuta el análisis financiero seleccionando la estrategia correspondiente.
     *
     * @param type El tipo de análisis a ejecutar
     * @param transactions Las transacciones a analizar
     * @return El resultado del análisis
     * @throws IllegalArgumentException si el tipo de análisis no existe
     */

    @Override
    public AnalysisResult analyze(AnalysisType type, List<Transaction> transactions) {
        Objects.requireNonNull(type, "AnalysisType must not be null");
        Objects.requireNonNull(transactions, "Transactions must not be null");

        if(transactions.isEmpty())  throw new IllegalArgumentException("Transactions list must not be empty");

        FinancialAnalysisStrategy strategy =strategies.get(type) ;

        AnalysisMetric metric = strategy.analyze(transactions);

        return  AnalysisResult.of(type, metric);
    }



}
