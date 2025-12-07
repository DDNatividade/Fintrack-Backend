package com.apis.fintrack.domain.Analysis.Port.Input;

import com.apis.fintrack.domain.Analysis.model.AnalysisResult;
import com.apis.fintrack.domain.Analysis.model.AnalysisType;
import com.apis.fintrack.domain.transaction.model.Transaction;
import java.util.List;

/**
 * Puerto de entrada (Input Port) para los casos de uso de anÃ¡lisis financiero.
 * Define el contrato que deben cumplir los servicios de anÃ¡lisis.
 *
 * Arquitectura Hexagonal: Puerto de aplicaciÃ³n
 */
public interface IFinancialAnalysisUseCase {

    /**
     * Ejecuta un anÃ¡lisis financiero sobre una lista de transacciones.
     *
     * @param type El tipo de anÃ¡lisis a ejecutar
     * @param transactions La lista de transacciones a analizar
     * @return El resultado del anÃ¡lisis
     * @throws IllegalArgumentException si el tipo de anÃ¡lisis no es soportado
     */
    AnalysisResult analyze(AnalysisType type, List<Transaction> transactions);
}


