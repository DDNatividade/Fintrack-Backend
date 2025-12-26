package com.apis.fintrack.domain.analysis.service;


import com.apis.fintrack.domain.analysis.model.AnalysisResult;
import com.apis.fintrack.domain.analysis.model.AnalysisType;
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

/**
 * Implementación del caso de uso de análisis financiero.
 * Arquitectura Hexagonal: Adaptador (Implementación de Puerto de Entrada)
 * Gestiona el registro de estrategias de análisis y orquesta su ejecución.
 */
@Service
public class FinancialAnalysisService implements IFinancialAnalysisUseCase {

    private final Map<AnalysisType, FinancialAnalysisStrategy> strategies;

    /**
     * Constructor con inyección de dependencias de todas las estrategias.
     *
     * @param monthlyAverageStrategy Estrategia de promedio mensual
     * @param savingsRateStrategy Estrategia de tasa de ahorro
     * @param spendingTrendStrategy Estrategia de tendencia de gasto
     */
    public FinancialAnalysisService(
            MonthlyAverageStrategy monthlyAverageStrategy,
            SavingsRateStrategy savingsRateStrategy,
            SpendingTrendStrategy spendingTrendStrategy) {

        this.strategies = Map.of(
                AnalysisType.MONTHLY_AVERAGE, monthlyAverageStrategy,
                AnalysisType.SAVINGS_RATE, savingsRateStrategy,
                AnalysisType.SPENDING_TREND, spendingTrendStrategy
        );
    }

    /**
     * Ejecuta el análisis financiero seleccionando la estrategia correspondiente.
     *
     * @param type El tipo de anÃ¡lisis a ejecutar
     * @param transactions Las transacciones a analizar
     * @return El resultado del anÃ¡lisis
     * @throws IllegalArgumentException si el tipo de anÃ¡lisis no existe
     */
    @Override
    public AnalysisResult analyze(AnalysisType type, List<Transaction> transactions) {
        FinancialAnalysisStrategy strategy = strategies.get(type);

        if (strategy == null) {
            throw new IllegalArgumentException("Tipo de análisis no soportado: " + type);
        }

        // Validaciones centralizadas en la capa de orquestación
        Objects.requireNonNull(transactions, "La lista de transacciones no puede ser nula");
        if (transactions.isEmpty()) {
            return new AnalysisResult(type, BigDecimal.ZERO);
        }
        if (transactions.stream().anyMatch(Objects::isNull)) {
            throw new IllegalArgumentException("La lista de transacciones contiene elementos nulos");
        }

        var result = strategy.analyze(transactions);
        return new AnalysisResult(type, result);
    }
}
