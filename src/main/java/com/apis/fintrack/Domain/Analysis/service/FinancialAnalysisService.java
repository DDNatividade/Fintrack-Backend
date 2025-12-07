package com.apis.fintrack.domain.Analysis.service;

import com.apis.fintrack.domain.Analysis.model.AnalysisResult;
import com.apis.fintrack.domain.Analysis.model.AnalysisType;
import com.apis.fintrack.domain.Analysis.Port.Input.IFinancialAnalysisUseCase;
import com.apis.fintrack.domain.Analysis.strategy.FinancialAnalysisStrategy;
import com.apis.fintrack.domain.Analysis.strategy.MonthlyAverageStrategy;
import com.apis.fintrack.domain.Analysis.strategy.SavingsRateStrategy;
import com.apis.fintrack.domain.Analysis.strategy.SpendingTrendStrategy;
import com.apis.fintrack.domain.transaction.model.Transaction;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * ImplementaciÃ³n del caso de uso de anÃ¡lisis financiero.
 *
 * Arquitectura Hexagonal: Adaptador (ImplementaciÃ³n de Puerto de Entrada)
 * Gestiona el registro de estrategias de anÃ¡lisis y orquesta su ejecuciÃ³n.
 */
@Service
public class FinancialAnalysisService implements IFinancialAnalysisUseCase {

    private final Map<AnalysisType, FinancialAnalysisStrategy> strategies;

    /**
     * Constructor con inyecciÃ³n de dependencias de todas las estrategias.
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
     * Ejecuta el anÃ¡lisis financiero seleccionando la estrategia correspondiente.
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
            throw new IllegalArgumentException("Tipo de anÃ¡lisis no soportado: " + type);
        }

        var result = strategy.analyze(transactions);
        return new AnalysisResult(type, result);
    }
}






