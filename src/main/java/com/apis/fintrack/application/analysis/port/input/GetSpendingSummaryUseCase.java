package com.apis.fintrack.application.analysis.port.input;

import com.apis.fintrack.application.analysis.SpendingSummary;
import com.apis.fintrack.domain.analysis.model.AnalysisPeriod;

/**
 * Input port to retrieve a spending summary for a user over a period.
 * This is an application-level contract;
 * Implementations should orchestrate retrieval of aggregated transaction
 * data and map it to a SpendingSummary domain model.
 */
public interface GetSpendingSummaryUseCase {

    /**
     * Retrieves a spending summary for the given user and analysis period.
     *
     * @param userId the id of the user
     * @param period the analysis period
     * @return a SpendingSummary containing aggregated metrics
     * @throws IllegalArgumentException when inputs are invalid
     */
    SpendingSummary getSummary(Long userId, AnalysisPeriod period);
}

