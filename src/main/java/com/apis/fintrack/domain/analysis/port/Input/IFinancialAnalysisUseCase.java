package com.apis.fintrack.domain.analysis.port.Input;


import com.apis.fintrack.domain.analysis.model.kpi.AnalysisResult;
import com.apis.fintrack.domain.analysis.model.kpi.AnalysisType;
import com.apis.fintrack.domain.transaction.model.Transaction;
import java.util.List;

/**
 * Puerto de entrada (Input Port) para los casos de uso de análisis financiero.
 * Define el contrato que deben cumplir los servicios de análisis.
 * Arquitectura Hexagonal: Puerto de aplicación
 */
public interface IFinancialAnalysisUseCase {

    /**
     * Ejecuta un análisis financiero sobre una lista de transacciones.
     *
     * @param type El tipo de análisis a ejecutar
     * @param transactions La lista de transacciones a analizar
     * @return El resultado del análisis
     * @throws IllegalArgumentException si el tipo de análisis no es soportado
     */
    AnalysisResult analyze(AnalysisType type, List<Transaction> transactions);
}


