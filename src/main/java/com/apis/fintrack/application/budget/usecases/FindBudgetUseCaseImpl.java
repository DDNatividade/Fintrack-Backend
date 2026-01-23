package com.apis.fintrack.application.budget.usecases;

import com.apis.fintrack.domain.budget.model.Budget;
import com.apis.fintrack.domain.budget.model.BudgetID;
import com.apis.fintrack.domain.budget.port.input.FindBudgetUseCase;
import com.apis.fintrack.domain.budget.port.output.BudgetRepositoryPort;
import com.apis.fintrack.domain.budget.exception.BudgetNotFoundException;
import com.apis.fintrack.domain.user.model.UserId;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

/**
 * Application implementation for finding budgets.
 */
public class FindBudgetUseCaseImpl implements FindBudgetUseCase {

    private final BudgetRepositoryPort budgetRepository;

    public FindBudgetUseCaseImpl(BudgetRepositoryPort budgetRepository) {
        this.budgetRepository = Objects.requireNonNull(budgetRepository);
    }

    @Override
    public Budget findById(BudgetID budgetId) {
        Objects.requireNonNull(budgetId, "budgetId cannot be null");
        return budgetRepository.findById(budgetId)
                .orElseThrow(() -> new BudgetNotFoundException(budgetId.value()));
    }

    @Override
    public List<Budget> findByUserId(UserId userId) {
        Objects.requireNonNull(userId, "userId cannot be null");
        return budgetRepository.findByUserId(userId);
    }

    @Override
    public Budget findByUserIdAndCategory(UserId userId, com.apis.fintrack.domain.transaction.model.TransactionCategoryEnum category) {
        Objects.requireNonNull(userId, "userId cannot be null");
        Objects.requireNonNull(category, "category cannot be null");
        return budgetRepository.findByUserIdAndCategory(userId, category)
                .orElseThrow(() -> new BudgetNotFoundException(
                        "No budget for userId " + userId + " and category " + category
                ));
    }

    @Override
    public List<Budget> findActiveBudgetsByUserId(UserId userId) {
        Objects.requireNonNull(userId, "userId cannot be null");

        // Current period
        LocalDate start = LocalDate.now().withDayOfMonth(1);
        LocalDate end = start.plusMonths(1).minusDays(1);

        return budgetRepository.findByUserIdAndPeriod(userId, start, end);
    }
}
