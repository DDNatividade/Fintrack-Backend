package com.apis.fintrack.application.transaction.mapper;

import com.apis.fintrack.domain.transaction.model.Transaction;
import com.apis.fintrack.domain.transaction.port.input.UpdateTransactionUseCase.UpdateTransactionCommand;
import org.springframework.stereotype.Component;

@Component
public final class TransactionUpdateCommandMapper {
    private TransactionUpdateCommandMapper() {
    }
    public void applyUpdateCommand(Transaction transaction, UpdateTransactionCommand command) {
        transaction.changeDescription(command.description());
        transaction.changeType(command.isIncome());
        transaction.changeAmount(command.amount());
        transaction.changeCategory(command.category());
        if (command.transactionDate() != null) {
            transaction.changeDate(command.transactionDate());
        }
    }
}

