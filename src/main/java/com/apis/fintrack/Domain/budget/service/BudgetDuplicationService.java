package com.apis.fintrack.domain.budget.service;

import com.apis.fintrack.domain.budget.exception.BudgetWithCategoryAlreadyExists;
import com.apis.fintrack.domain.transaction.model.TransactionCategoryEnum;
import com.apis.fintrack.domain.user.model.User;
import com.apis.fintrack.infrastructure.adapter.output.persistence.repository.BudgetRepository;

public class BudgetDuplicationService {
    private final BudgetRepository budgetRepository;

    public BudgetDuplicationService(BudgetRepository budgetRepository) {
        this.budgetRepository = budgetRepository;
    }

    public void checkIfBudgetNameExistsForUser(User user, TransactionCategoryEnum category) {
        if (budgetRepository.findByUserIdAndCategory(user.getId().getValue(),category).isPresent()) {
            throw new BudgetWithCategoryAlreadyExists("Budget with category " + category + " already exists for user: " + user.getFullName());
        }
    }
}
