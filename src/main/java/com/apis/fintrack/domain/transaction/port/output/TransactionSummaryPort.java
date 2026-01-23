
package com.apis.fintrack.domain.transaction.port.output;

import com.apis.fintrack.domain.shared.model.Money;
import com.apis.fintrack.domain.transaction.model.TransactionCategoryEnum;
import com.apis.fintrack.domain.user.model.UserId;
import java.time.LocalDate;

/**
 * Output port that exposes aggregate operations over transactions that are
 * useful for higher-level domains such as budgeting. Implementations are
 * expected to perform efficient aggregation (e.g. SQL SUM) at the
 * infrastructure layer.
 */
public interface TransactionSummaryPort {

    /**
     * Sums expenses (absolute value) for a user and category between dates (inclusive).
     * Returns Money.zero() if no transactions found.
     */
    Money sumExpensesByUserCategoryAndPeriod(UserId userId, TransactionCategoryEnum category, LocalDate startDate, LocalDate endDate);



}

