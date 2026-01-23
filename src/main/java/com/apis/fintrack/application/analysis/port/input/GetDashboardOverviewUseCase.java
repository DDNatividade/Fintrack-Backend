package com.apis.fintrack.application.analysis.port.input;

import com.apis.fintrack.application.analysis.readmodel.DashboardOverviewRM;
import com.apis.fintrack.domain.analysis.model.AnalysisPeriod;

public interface GetDashboardOverviewUseCase {
    DashboardOverviewRM getOverview(Long userId, AnalysisPeriod period);
}
