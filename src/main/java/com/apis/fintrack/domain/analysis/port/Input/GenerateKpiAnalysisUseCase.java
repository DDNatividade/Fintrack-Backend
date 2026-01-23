package com.apis.fintrack.domain.analysis.port.Input;

import com.apis.fintrack.domain.analysis.model.AnalysisPeriod;
import com.apis.fintrack.domain.analysis.model.kpi.AnalysisResult;
import com.apis.fintrack.domain.analysis.model.kpi.AnalysisType;

/**
 * Input port to generate KPI-based financial analysis for a given user and period.
 *
 * This port defines the domain contract for producing financial KPIs
 * (e.g. savings rate, spending trends, averages) that can be consumed
 * directly by clients for dashboards and charts.
 *
 * The application layer is responsible for:
 * - Validating inputs
 * - Retrieving transactions
 * - Delegating KPI computation to domain strategies
 */
public interface GenerateKpiAnalysisUseCase {

    /**
     * Generates a KPI analysis of the given type for a user and period.
     *
     * @param type   the KPI analysis type to execute
     * @param userId the id of the user to analyze
     * @param period the analysis period (start/end)
     * @return the resulting KPI AnalysisResult
     * @throws IllegalArgumentException when inputs are invalid
     */
    AnalysisResult generateKpi(AnalysisType type, Long userId, AnalysisPeriod period);
}
