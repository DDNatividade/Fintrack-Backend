package com.apis.fintrack.application.analysis.port.input;

import com.apis.fintrack.application.analysis.readmodel.MonthlyCashflowRM;
import com.apis.fintrack.domain.analysis.model.AnalysisPeriod;

/**
 * Use case to retrieve monthly cashflow (income vs expenses).
 * Application-level input port.
 */
public interface GetMonthlyCashflowUseCase {

    /**
     * Returns the monthly cashflow for a user within a given period.
     *
     * @param userId user identifier
     * @param period analysis period
     * @return monthly cashflow read model
     */
    MonthlyCashflowRM getMonthlyCashflow(Long userId, AnalysisPeriod period);
}
