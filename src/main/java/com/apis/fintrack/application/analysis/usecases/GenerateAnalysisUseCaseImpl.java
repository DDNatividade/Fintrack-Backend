package com.apis.fintrack.application.analysis.usecases;

import com.apis.fintrack.domain.analysis.model.AnalysisPeriod;
import com.apis.fintrack.domain.analysis.model.kpi.AnalysisResult;
import com.apis.fintrack.domain.analysis.model.kpi.AnalysisType;
import com.apis.fintrack.domain.analysis.port.Input.GenerateKpiAnalysisUseCase;
import com.apis.fintrack.domain.analysis.port.Input.IFinancialAnalysisUseCase;
import com.apis.fintrack.domain.transaction.model.Transaction;
import com.apis.fintrack.domain.transaction.port.input.FindTransactionUseCase;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * Application-layer implementation of {@link com.apis.fintrack.domain.analysis.port.Input.GenerateKpiAnalysisUseCase}.
 * <p>
 * Responsibilities:
 * - Validate inputs (fail-fast).
 * - Retrieve domain transactions for the given user and period via {@link FindTransactionUseCase}.
 * - Delegate the analysis calculation to the domain analysis service ({@link IFinancialAnalysisUseCase}).
 * <p>
 * This class contains no infrastructure code and depends only on domain/application ports.
 */
public final class GenerateAnalysisUseCaseImpl implements GenerateKpiAnalysisUseCase {

    private final FindTransactionUseCase findTransactionUseCase;
    private final IFinancialAnalysisUseCase financialAnalysisService;

    public GenerateAnalysisUseCaseImpl(FindTransactionUseCase findTransactionUseCase,
                                       IFinancialAnalysisUseCase financialAnalysisService) {
        this.findTransactionUseCase = Objects.requireNonNull(findTransactionUseCase, "findTransactionUseCase must not be null");
        this.financialAnalysisService = Objects.requireNonNull(financialAnalysisService, "financialAnalysisService must not be null");
    }



    @Override
    public AnalysisResult generateKpi(AnalysisType type, Long userId, AnalysisPeriod period) {
        Objects.requireNonNull(type, "Analysis type must not be null");
        Objects.requireNonNull(userId, "User ID must not be null");
        Objects.requireNonNull(period, "Analysis period must not be null");

        // Retrieve transactions for the user within the period. We use the FindTransactionUseCase (input port)
        // and request an unpaged result to obtain all transactions in the range.
        Page<Transaction> page = findTransactionUseCase.findByDateBetween(period.startDate(), period.endDate(), Pageable.unpaged());
        List<Transaction> transactions = page == null ? Collections.emptyList() : page.getContent();

        // Delegate the actual analysis to the domain-level analysis service (strategy dispatcher).
        return financialAnalysisService.analyze(type, transactions);
    }
}

