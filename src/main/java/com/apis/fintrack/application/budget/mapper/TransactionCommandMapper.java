package com.apis.fintrack.application.budget.mapper;

import com.apis.fintrack.domain.transaction.model.*;
import com.apis.fintrack.domain.transaction.port.input.CreateTransactionUseCase;
import com.apis.fintrack.domain.user.model.UserId;
import org.springframework.stereotype.Component;

@Component
public final class TransactionCommandMapper {
    private TransactionCommandMapper() {
    }
    public Transaction toTransaction(CreateTransactionUseCase.CreateTransactionCommand command) {
        return new Transaction(
            null,
            Description.of(command.description()),
            TransactionAmount.of(command.amount(), command.isIncome()),
            TransactionDate.of(command.transactionDate()),
            Category.of(command.category()),
            UserId.of(command.userId())
        );
    }
}
