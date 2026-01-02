package com.apis.fintrack.domain.analysis.port.Input;

import com.apis.fintrack.domain.analysis.model.AnalysisPeriod;
import com.apis.fintrack.domain.analysis.model.kpi.AnalysisResult;
import com.apis.fintrack.domain.analysis.model.kpi.AnalysisType;

/**
 * Input port to generate a financial analysis for a given user and period.
 * This port belongs to the domain layer and defines the contract that the
 * application layer must implement to orchestrate the creation of an analysis
 * (fetching transactions, delegating to analysis strategies, and returning
 * a domain AnalysisResult).
 */
public interface GenerateAnalysisUseCase {

    /**
     * Generates an analysis of the provided type for the given user and period.
     *
     * @param type   the analysis type to execute (e.g. MONTHLY_AVERAGE)
     * @param userId the id of the user to analyze
     * @param period the analysis period (start/end)
     * @return the resulting AnalysisResult
     * @throws IllegalArgumentException when inputs are invalid
     */
    AnalysisResult generate(AnalysisType type, Long userId, AnalysisPeriod period);
}

