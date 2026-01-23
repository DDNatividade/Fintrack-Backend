package com.apis.fintrack.application.analysis.port.input;

import com.apis.fintrack.application.analysis.readmodel.SpendingByCategoryRM;
import com.apis.fintrack.domain.analysis.model.AnalysisPeriod;

public interface GetSpendingByCategoryUseCase {
    SpendingByCategoryRM getSpendingByCategory(Long userId,
                                               AnalysisPeriod period);
}
