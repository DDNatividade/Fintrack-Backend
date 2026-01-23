package com.apis.fintrack.application.budget.usecases;

import com.apis.fintrack.domain.budget.model.BudgetID;
import com.apis.fintrack.domain.budget.port.input.AnalyzeBudgetUseCase;
import com.apis.fintrack.domain.budget.port.output.BudgetRepositoryPort;
import com.apis.fintrack.domain.shared.model.Money;
import com.apis.fintrack.domain.transaction.port.output.TransactionSummaryPort;
import java.math.BigDecimal;
import java.util.Objects;


/**
 * Application implementation that orchestrates budget analysis.
 * Delegates aggregation to TransactionSummaryPort to avoid loading all transactions.
 */
public class AnalyzeBudgetUseCaseImpl implements AnalyzeBudgetUseCase {

    private final BudgetRepositoryPort budgetRepository;
    private final TransactionSummaryPort transactionSummary;

    public AnalyzeBudgetUseCaseImpl(BudgetRepositoryPort budgetRepository, TransactionSummaryPort transactionSummary) {
        this.budgetRepository = Objects.requireNonNull(budgetRepository);
        this.transactionSummary = Objects.requireNonNull(transactionSummary);
    }


    @Override
    public boolean isBudgetExceeded(BudgetID budgetId) {
        return budgetRepository.findById(budgetId)
            .map(budget -> {
                Money spent = transactionSummary.sumExpensesByUserCategoryAndPeriod(
                    budget.getUserId(),
                    budget.getCategory(),
                    budget.getPeriod().getStartDate(),
                    budget.getPeriod().getEndDate()
                );
                return spent.isGreaterThan(budget.getLimit());
            })
            .orElseThrow(() -> new RuntimeException("Budget not found"));
    }

    @Override
    public BigDecimal getBudgetUsagePercentage(BudgetID budgetId) {
        return (BigDecimal) budgetRepository.findById(budgetId)
                .map(budget -> {;
                Money spent = transactionSummary.sumExpensesByUserCategoryAndPeriod(
                    budget.getUserId(),
                    budget.getCategory(),
                    budget.getPeriod().getStartDate(),
                    budget.getPeriod().getEndDate()
                );
                if (budget.getLimit().isZero()) {
                    return BigDecimal.ZERO;
                }
                return spent.divide(budget.getLimit().getAmount()).multiply(BigDecimal.valueOf(100));
            }).orElseThrow(() -> new RuntimeException("Budget not found"));
    }

    @Override
    public Money getRemainingAmount(BudgetID budgetId) {
        return budgetRepository.findById(budgetId)
                .map(budget -> {;
                Money spent = transactionSummary.sumExpensesByUserCategoryAndPeriod(
                    budget.getUserId(),
                    budget.getCategory(),
                    budget.getPeriod().getStartDate(),
                    budget.getPeriod().getEndDate()
                );
                return budget.getLimit().subtract(spent);
            })
            .orElseThrow(() -> new RuntimeException("Budget not found"));
    }

    @Override
    public BudgetAnalysisResult analyzeBudget(BudgetID budgetId) {
        return budgetRepository.findById(budgetId)
                .map(budget -> {;
                Money spent = transactionSummary.sumExpensesByUserCategoryAndPeriod(
                    budget.getUserId(),
                    budget.getCategory(),
                    budget.getPeriod().getStartDate(),
                    budget.getPeriod().getEndDate()
                );
                Money remaining = budget.getLimit().subtract(spent);
                BigDecimal usagePercentage = BigDecimal.ZERO;
                if (!budget.getLimit().isZero()) {
                    usagePercentage = spent.divide(budget.getLimit().getAmount()).multiply(BigDecimal.valueOf(100)).getAmount();
                }
                boolean isExceeded = spent.isGreaterThan(budget.getLimit());
                boolean isNearLimit = usagePercentage.compareTo(BigDecimal.valueOf(80)) >= 0 && !isExceeded;

                return new BudgetAnalysisResult(
                    budget.getCategory(),
                    budget.getLimit().getAmount(),
                    spent.getAmount(),
                    remaining.getAmount(),
                    usagePercentage,
                    isExceeded,
                    isNearLimit
                );
            })
            .orElseThrow(() -> new RuntimeException("Budget not found"));
    }

}

