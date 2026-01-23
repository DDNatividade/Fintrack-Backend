package com.apis.fintrack.domain.transaction.port.output;

import com.apis.fintrack.domain.user.model.UserId;

import java.time.LocalDate;

public interface TransactionCountPort {
    /**
     * Counts the number of transactions for a user between dates (inclusive).
     */
    int countTransactionsByUserAndPeriod(UserId userId, LocalDate startDate, LocalDate endDate);
}
