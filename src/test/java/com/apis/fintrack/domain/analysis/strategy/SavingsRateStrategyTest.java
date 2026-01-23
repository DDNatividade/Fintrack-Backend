package com.apis.fintrack.domain.analysis.strategy;

import com.apis.fintrack.domain.analysis.model.kpi.AnalysisType;
import com.apis.fintrack.domain.analysis.model.metric.AnalysisMetric;
import com.apis.fintrack.domain.analysis.model.metric.MetricUnit;
import com.apis.fintrack.domain.analysis.model.metric.ScalarMetric;
import com.apis.fintrack.domain.transaction.model.Transaction;
import com.apis.fintrack.domain.transaction.model.TransactionAmount;
import com.apis.fintrack.domain.transaction.model.TransactionDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for SavingsRateStrategy.
 *
 * Tests follow specification-based testing approach, covering:
 * - supports() method contract
 * - analyze() with null inputs
 * - analyze() with empty list
 * - analyze() with valid income and expense transactions
 * - analyze() with edge cases (no income, no expenses, zero savings)
 * - analyze() with various savings rates (positive, negative, 100%)
 * - analyze() precision and rounding behavior
 */
@DisplayName("SavingsRateStrategy Tests")
class SavingsRateStrategyTest {

    private SavingsRateStrategy strategy;

    @BeforeEach
    void setUp() {
        strategy = new SavingsRateStrategy();
    }

    // ========================================
    // supports() Method Tests
    // ========================================

    @Nested
    @DisplayName("supports() method")
    class SupportsTests {

        /**
         * Verifies that the strategy correctly identifies itself as supporting
         * the SAVINGS_RATE analysis type.
         * <p>
         * This ensures the strategy can be properly registered and discovered
         * by the FinancialAnalysisService.
         */
        @Test
        @DisplayName("supports returns SAVINGS_RATE")
        void supports_returnsSavingsRate() {
            AnalysisType result = strategy.supports();

            assertEquals(AnalysisType.SAVINGS_RATE, result,
                    "Strategy should support SAVINGS_RATE analysis type");
        }
    }

    // ========================================
    // analyze() - Invalid Inputs
    // ========================================

    @Nested
    @DisplayName("analyze() - Invalid Inputs")
    class AnalyzeInvalidInputsTests {

        /**
         * Verifies that passing null as the transactions list throws NullPointerException.
         * <p>
         * This test ensures defensive programming and fail-fast behavior.
         * Bug it could reveal: Missing null validation, leading to unexpected NPE later.
         */
        @Test
        @DisplayName("analyze with null transactions throws NullPointerException")
        void analyze_nullTransactions_throwsNullPointer() {
            NullPointerException exception = assertThrows(
                    NullPointerException.class,
                    () -> strategy.analyze(null)
            );

            assertEquals("transactions must not be null", exception.getMessage(),
                    "Exception message should clearly indicate null transactions");
        }

        /**
         * Verifies that an empty transaction list returns 0% savings rate.
         * <p>
         * Expected behavior: totalIncome = 0, totalExpenses = 0,
         * result = 0% (no income means 0% savings rate).
         * Bug it could reveal: Division by zero or incorrect handling of empty list.
         */
        @Test
        @DisplayName("analyze with empty transactions returns zero savings rate")
        void analyze_emptyTransactions_returnsZeroSavingsRate() {
            AnalysisMetric result = strategy.analyze(Collections.emptyList());

            assertNotNull(result, "Result should not be null");
            assertTrue(result instanceof ScalarMetric, "Result should be ScalarMetric");
            ScalarMetric scalarResult = (ScalarMetric) result;
            assertEquals(BigDecimal.ZERO, scalarResult.value(),
                    "Savings rate should be zero for empty list");
            assertEquals(MetricUnit.PERCENT, scalarResult.unit(),
                    "Unit should be PERCENT");
        }

        /**
         * Verifies behavior when the transaction list contains null elements.
         * <p>
         * This test checks if the strategy handles or propagates NullPointer
         * when iterating over transactions.
         * Bug it could reveal: Missing null check on individual transactions.
         */
        @Test
        @DisplayName("analyze with list containing null transaction throws NullPointerException")
        void analyze_listContainingNullTransaction_throwsNullPointer() {
            Transaction validTransaction = createMockIncomeTransaction(
                    new BigDecimal("100.00"),
                    LocalDate.now()
            );

            List<Transaction> listWithNull = Arrays.asList(validTransaction, null);

            assertThrows(
                    NullPointerException.class,
                    () -> strategy.analyze(listWithNull),
                    "Should throw NullPointerException when list contains null transaction"
            );
        }
    }

    // ========================================
    // analyze() - No Income Edge Cases
    // ========================================

    @Nested
    @DisplayName("analyze() - No Income Cases")
    class AnalyzeNoIncomeTests {

        /**
         * Verifies that when there are only expenses (no income),
         * the savings rate is 0%.
         * <p>
         * Expected: totalIncome = 0, division by zero avoided, returns 0%.
         * Bug it could reveal: Division by zero error or incorrect handling.
         */
        @Test
        @DisplayName("analyze only expenses with no income returns zero savings rate")
        void analyze_onlyExpensesNoIncome_returnsZeroSavingsRate() {
            Transaction expense1 = createMockExpenseTransaction(new BigDecimal("100.00"), LocalDate.now());
            Transaction expense2 = createMockExpenseTransaction(new BigDecimal("200.00"), LocalDate.now());

            AnalysisMetric result = strategy.analyze(List.of(expense1, expense2));

            assertNotNull(result);
            ScalarMetric scalarResult = (ScalarMetric) result;
            assertEquals(BigDecimal.ZERO, scalarResult.value(),
                    "Savings rate should be 0% when there is no income");
            assertEquals(MetricUnit.PERCENT, scalarResult.unit());
        }

        /**
         * Verifies that when total income is exactly zero, the method returns 0%
         * without attempting division by zero.
         * <p>
         * Bug it could reveal: ArithmeticException due to division by zero.
         */
        @Test
        @DisplayName("analyze with zero income avoids division by zero")
        void analyze_zeroIncome_avoidsDivisionByZero() {
            Transaction expense = createMockExpenseTransaction(new BigDecimal("50.00"), LocalDate.now());

            assertDoesNotThrow(
                    () -> strategy.analyze(List.of(expense)),
                    "Should not throw exception when income is zero"
            );

            AnalysisMetric result = strategy.analyze(List.of(expense));
            ScalarMetric scalarResult = (ScalarMetric) result;
            assertEquals(BigDecimal.ZERO, scalarResult.value(),
                    "Should return 0% when income is zero");
        }
    }

    // ========================================
    // analyze() - Only Income (No Expenses)
    // ========================================

    @Nested
    @DisplayName("analyze() - Only Income Cases")
    class AnalyzeOnlyIncomeTests {

        /**
         * Verifies that when there are only income transactions (no expenses),
         * the savings rate is 100%.
         * <p>
         * Expected: totalIncome = X, totalExpenses = 0, savings = X,
         * savingsRate = (X / X) * 100 = 100%.
         * Bug it could reveal: Incorrect calculation when no expenses exist.
         */
        @Test
        @DisplayName("analyze only income with no expenses returns 100% savings rate")
        void analyze_onlyIncomeNoExpenses_returns100PercentSavingsRate() {
            Transaction income1 = createMockIncomeTransaction(new BigDecimal("1000.00"), LocalDate.now());
            Transaction income2 = createMockIncomeTransaction(new BigDecimal("500.00"), LocalDate.now());

            AnalysisMetric result = strategy.analyze(List.of(income1, income2));

            assertNotNull(result);
            ScalarMetric scalarResult = (ScalarMetric) result;
            // totalIncome = 1500, totalExpenses = 0
            // savings = 1500 - 0 = 1500
            // savingsRate = (1500 / 1500) * 100 = 100%
            assertEquals(new BigDecimal("100.0000"), scalarResult.value(),
                    "Savings rate should be 100% when there are no expenses");
            assertEquals(MetricUnit.PERCENT, scalarResult.unit());
        }

        /**
         * Verifies correct calculation with a single income transaction and no expenses.
         * <p>
         * Bug it could reveal: Edge case handling with single transaction.
         */
        @Test
        @DisplayName("analyze single income transaction returns 100% savings rate")
        void analyze_singleIncomeTransaction_returns100Percent() {
            Transaction income = createMockIncomeTransaction(new BigDecimal("2000.00"), LocalDate.now());

            AnalysisMetric result = strategy.analyze(List.of(income));

            assertNotNull(result);
            ScalarMetric scalarResult = (ScalarMetric) result;
            assertEquals(new BigDecimal("100.0000"), scalarResult.value(),
                    "Single income with no expenses should result in 100% savings");
        }
    }

    // ========================================
    // analyze() - Mixed Income and Expenses
    // ========================================

    @Nested
    @DisplayName("analyze() - Mixed Income and Expenses")
    class AnalyzeMixedTransactionsTests {

        /**
         * Verifies correct savings rate calculation with typical income and expenses.
         * <p>
         * Formula: SavingsRate = ((totalIncome + totalExpenses) / totalIncome) * 100
         * <p>
         * Example:
         * - Income = 1000 (positive)
         * - Expenses = -400 (stored as negative in TransactionAmount)
         * - Savings = 1000 + (-400) = 600
         * - SavingsRate = (600 / 1000) * 100 = 60%
         * <p>
         * Bug it could reveal: Incorrect calculation of savings or rate.
         */
        @Test
        @DisplayName("analyze mixed transactions calculates correct savings rate")
        void analyze_mixedTransactions_calculatesCorrectSavingsRate() {
            Transaction income = createMockIncomeTransaction(new BigDecimal("1000.00"), LocalDate.now());
            Transaction expense = createMockExpenseTransaction(new BigDecimal("400.00"), LocalDate.now());

            AnalysisMetric result = strategy.analyze(List.of(income, expense));

            assertNotNull(result);
            ScalarMetric scalarResult = (ScalarMetric) result;
            // totalIncome = 1000 (positive)
            // totalExpenses = -400 (negative, expenses stored with negative sign)
            // savings = totalIncome + totalExpenses = 1000 + (-400) = 600
            // savingsRate = (600 / 1000) * 100 = 60%
            BigDecimal expectedRate = new BigDecimal("60.0000");
            assertEquals(expectedRate, scalarResult.value(),
                    "Savings rate should be 60% (saved 600 out of 1000 income)");
        }

        /**
         * Verifies calculation with multiple income and expense transactions.
         * <p>
         * Aggregates all incomes and all expenses before calculating rate.
         * Bug it could reveal: Incorrect aggregation of multiple transactions.
         */
        @Test
        @DisplayName("analyze multiple mixed transactions calculates aggregate savings rate")
        void analyze_multipleTransactions_calculatesAggregateSavingsRate() {
            Transaction income1 = createMockIncomeTransaction(new BigDecimal("1500.00"), LocalDate.now());
            Transaction income2 = createMockIncomeTransaction(new BigDecimal("500.00"), LocalDate.now());
            Transaction expense1 = createMockExpenseTransaction(new BigDecimal("300.00"), LocalDate.now());
            Transaction expense2 = createMockExpenseTransaction(new BigDecimal("200.00"), LocalDate.now());

            AnalysisMetric result = strategy.analyze(List.of(income1, income2, expense1, expense2));

            assertNotNull(result);
            ScalarMetric scalarResult = (ScalarMetric) result;
            // totalIncome = 1500 + 500 = 2000
            // totalExpenses = -300 + -200 = -500
            // savings = 2000 + (-500) = 1500
            // savingsRate = (1500 / 2000) * 100 = 75%
            BigDecimal expectedRate = new BigDecimal("75.0000");
            assertEquals(expectedRate, scalarResult.value(),
                    "Should aggregate all income and expenses correctly (saved 1500 out of 2000)");
        }

        /**
         * Verifies correct behavior when savings is exactly zero (income equals expenses).
         * <p>
         * When you spend exactly what you earn, savings rate = 0%.
         * Income = 1000, Expenses = 1000 (stored as -1000)
         * Savings = 1000 + (-1000) = 0
         * Rate = (0 / 1000) * 100 = 0%
         */
        @Test
        @DisplayName("analyze when income equals expenses returns zero savings rate")
        void analyze_incomeEqualsExpenses_returnsZeroSavingsRate() {
            Transaction income = createMockIncomeTransaction(new BigDecimal("1000.00"), LocalDate.now());
            Transaction expense = createMockExpenseTransaction(new BigDecimal("1000.00"), LocalDate.now());

            AnalysisMetric result = strategy.analyze(List.of(income, expense));

            assertNotNull(result);
            ScalarMetric scalarResult = (ScalarMetric) result;
            // totalIncome = 1000
            // totalExpenses = -1000
            // savings = 1000 + (-1000) = 0
            // savingsRate = (0 / 1000) * 100 = 0%
            BigDecimal expectedRate = new BigDecimal("0.0000");
            assertEquals(expectedRate, scalarResult.value(),
                    "Savings rate should be 0% when income equals expenses");
        }
    }

    // ========================================
    // analyze() - Negative Savings (Expenses > Income)
    // ========================================

    @Nested
    @DisplayName("analyze() - Negative Savings (Deficit)")
    class AnalyzeNegativeSavingsTests {

        /**
         * Verifies behavior when expenses exceed income (deficit scenario).
         * <p>
         * When you spend more than you earn, savings rate becomes negative.
         * Income = 500, Expenses = 1000 (stored as -1000)
         * Savings = 500 + (-1000) = -500
         * Rate = (-500 / 500) * 100 = -100%
         * <p>
         * Bug it could reveal: Incorrect handling of deficit scenarios.
         */
        @Test
        @DisplayName("analyze when expenses exceed income returns negative savings rate")
        void analyze_expensesExceedIncome_returnsNegativeSavingsRate() {
            Transaction income = createMockIncomeTransaction(new BigDecimal("500.00"), LocalDate.now());
            Transaction expense = createMockExpenseTransaction(new BigDecimal("1000.00"), LocalDate.now());

            AnalysisMetric result = strategy.analyze(List.of(income, expense));

            assertNotNull(result);
            ScalarMetric scalarResult = (ScalarMetric) result;
            // totalIncome = 500
            // totalExpenses = -1000
            // savings = 500 + (-1000) = -500
            // savingsRate = (-500 / 500) * 100 = -100%
            BigDecimal expectedRate = new BigDecimal("-100.0000");
            assertEquals(expectedRate, scalarResult.value(),
                    "Savings rate should be -100% when expenses are double the income");
        }

        /**
         * Verifies calculation with severe deficit (expenses much higher than income).
         * <p>
         * Income = 100, Expenses = 500 (stored as -500)
         * Savings = 100 + (-500) = -400
         * Rate = (-400 / 100) * 100 = -400%
         */
        @Test
        @DisplayName("analyze with severe deficit returns large negative rate")
        void analyze_severeDeficit_returnsLargeNegativeRate() {
            Transaction income = createMockIncomeTransaction(new BigDecimal("100.00"), LocalDate.now());
            Transaction expense = createMockExpenseTransaction(new BigDecimal("500.00"), LocalDate.now());

            AnalysisMetric result = strategy.analyze(List.of(income, expense));

            assertNotNull(result);
            ScalarMetric scalarResult = (ScalarMetric) result;
            // totalIncome = 100
            // totalExpenses = -500
            // savings = 100 + (-500) = -400
            // savingsRate = (-400 / 100) * 100 = -400%
            BigDecimal expectedRate = new BigDecimal("-400.0000");
            assertEquals(expectedRate, scalarResult.value(),
                    "Savings rate should be -400% when deficit is 4x the income");
        }
    }

    // ========================================
    // analyze() - Precision and Rounding
    // ========================================

    @Nested
    @DisplayName("analyze() - Precision and Rounding")
    class AnalyzePrecisionTests {

        /**
         * Verifies that the calculation maintains 4 decimal places precision.
         * <p>
         * Bug it could reveal: Loss of precision in division or multiplication.
         */
        @Test
        @DisplayName("analyze maintains 4 decimal places precision")
        void analyze_maintains4DecimalPlaces() {
            // Create scenario that results in repeating decimal
            Transaction income = createMockIncomeTransaction(new BigDecimal("3.00"), LocalDate.now());
            Transaction expense = createMockExpenseTransaction(new BigDecimal("1.00"), LocalDate.now());

            AnalysisMetric result = strategy.analyze(List.of(income, expense));

            assertNotNull(result);
            ScalarMetric scalarResult = (ScalarMetric) result;
            // totalIncome = 3
            // totalExpenses = -1
            // savings = 3 + (-1) = 2
            // rate = (2 / 3) = 0.6666... → con 4 decimales HALF_EVEN = 0.6667
            // rate * 100 = 66.67 (BigDecimal mantiene escala, resultado: 66.6700)
            BigDecimal expectedRate = new BigDecimal("66.6700");
            assertEquals(expectedRate, scalarResult.value(),
                    "Should maintain 4 decimal places with HALF_EVEN rounding");
        }

        /**
         * Verifies HALF_EVEN rounding mode behavior.
         * <p>
         * HALF_EVEN rounds to the nearest neighbor, or to the even neighbor if equidistant.
         */
        @Test
        @DisplayName("analyze uses HALF_EVEN rounding mode")
        void analyze_usesHalfEvenRounding() {
            // Test case where rounding matters
            Transaction income = createMockIncomeTransaction(new BigDecimal("7.00"), LocalDate.now());
            Transaction expense = createMockExpenseTransaction(new BigDecimal("2.00"), LocalDate.now());

            AnalysisMetric result = strategy.analyze(List.of(income, expense));

            assertNotNull(result);
            ScalarMetric scalarResult = (ScalarMetric) result;
            // totalIncome = 7
            // totalExpenses = -2
            // savings = 7 + (-2) = 5
            // rate = (5 / 7) = 0.714285... → con 4 decimales HALF_EVEN = 0.7143
            // rate * 100 = 71.43 (BigDecimal mantiene escala, resultado: 71.4300)
            BigDecimal expectedRate = new BigDecimal("71.4300");
            assertEquals(expectedRate, scalarResult.value(),
                    "Should use HALF_EVEN rounding mode");
        }

        /**
         * Verifies correct handling of very large amounts.
         * <p>
         * Income = 1,000,000, Expenses = -250,000
         * Savings = 1,000,000 + (-250,000) = 750,000
         * Rate = (750,000 / 1,000,000) * 100 = 75%
         * <p>
         * Bug it could reveal: Overflow or precision loss with large numbers.
         */
        @Test
        @DisplayName("analyze handles large amounts correctly")
        void analyze_largeAmounts_maintainsPrecision() {
            Transaction income = createMockIncomeTransaction(new BigDecimal("1000000.00"), LocalDate.now());
            Transaction expense = createMockExpenseTransaction(new BigDecimal("250000.00"), LocalDate.now());

            AnalysisMetric result = strategy.analyze(List.of(income, expense));

            assertNotNull(result);
            ScalarMetric scalarResult = (ScalarMetric) result;
            // totalIncome = 1,000,000
            // totalExpenses = -250,000
            // savings = 1,000,000 + (-250,000) = 750,000
            // rate = (750,000 / 1,000,000) * 100 = 75%
            BigDecimal expectedRate = new BigDecimal("75.0000");
            assertEquals(expectedRate, scalarResult.value(),
                    "Should maintain precision with large amounts");
        }

        /**
         * Verifies correct handling of very small amounts.
         * <p>
         * Income = 0.10, Expenses = -0.03
         * Savings = 0.10 + (-0.03) = 0.07
         * Rate = (0.07 / 0.10) = 0.7 = 70%
         * <p>
         * Bug it could reveal: Rounding errors with small numbers.
         */
        @Test
        @DisplayName("analyze handles small amounts correctly")
        void analyze_smallAmounts_maintainsPrecision() {
            Transaction income = createMockIncomeTransaction(new BigDecimal("0.10"), LocalDate.now());
            Transaction expense = createMockExpenseTransaction(new BigDecimal("0.03"), LocalDate.now());

            AnalysisMetric result = strategy.analyze(List.of(income, expense));

            assertNotNull(result);
            ScalarMetric scalarResult = (ScalarMetric) result;
            // totalIncome = 0.10
            // totalExpenses = -0.03
            // savings = 0.10 + (-0.03) = 0.07
            // rate = (0.07 / 0.10) = 0.7 → con 4 decimales = 0.7000
            // rate * 100 = 70.00
            BigDecimal expectedRate = new BigDecimal("70.0000");
            assertEquals(expectedRate, scalarResult.value(),
                    "Should maintain precision with small amounts");
        }

        /**
         * Verifies rounding behavior with HALF_EVEN when exactly at midpoint.
         * <p>
         * Income = 200, Expenses = -1
         * Savings = 200 + (-1) = 199
         * Rate = (199 / 200) * 100 = 99.5%
         * With 4 decimals: 99.5000 (no rounding needed)
         */
        @Test
        @DisplayName("analyze with exact half values uses banker's rounding")
        void analyze_exactHalfValue_usesBankersRounding() {
            Transaction income = createMockIncomeTransaction(new BigDecimal("200.00"), LocalDate.now());
            Transaction expense = createMockExpenseTransaction(new BigDecimal("1.00"), LocalDate.now());

            AnalysisMetric result = strategy.analyze(List.of(income, expense));

            assertNotNull(result);
            ScalarMetric scalarResult = (ScalarMetric) result;
            // totalIncome = 200
            // totalExpenses = -1
            // savings = 199
            // rate = (199 / 200) * 100 = 99.5
            BigDecimal expectedRate = new BigDecimal("99.5000");
            assertEquals(expectedRate, scalarResult.value(),
                    "Should handle exact half values correctly");
        }
    }

    // ========================================
    // analyze() - Return Value Validation
    // ========================================

    @Nested
    @DisplayName("analyze() - Return Value Validation")
    class AnalyzeReturnValueTests {

        /**
         * Verifies that analyze() always returns a ScalarMetric (never null).
         * <p>
         * Bug it could reveal: Null return in some code path.
         */
        @Test
        @DisplayName("analyze result is not null")
        void analyze_resultIsNotNull() {
            Transaction income = createMockIncomeTransaction(new BigDecimal("100.00"), LocalDate.now());

            AnalysisMetric result = strategy.analyze(List.of(income));

            assertNotNull(result, "Result should never be null");
        }

        /**
         * Verifies that the returned metric has the correct unit (PERCENT).
         * <p>
         * Bug it could reveal: Wrong MetricUnit used in ScalarMetric creation.
         */
        @Test
        @DisplayName("analyze returns ScalarMetric with PERCENT unit")
        void analyze_returnsScalarMetricWithPercentUnit() {
            Transaction income = createMockIncomeTransaction(new BigDecimal("100.00"), LocalDate.now());

            AnalysisMetric result = strategy.analyze(List.of(income));

            assertTrue(result instanceof ScalarMetric,
                    "Result should be instance of ScalarMetric");
            ScalarMetric scalarResult = (ScalarMetric) result;
            assertEquals(MetricUnit.PERCENT, scalarResult.unit(),
                    "Metric unit should be PERCENT");
        }

        /**
         * Verifies that the returned ScalarMetric value is a valid BigDecimal
         * (not null).
         */
        @Test
        @DisplayName("analyze returns valid BigDecimal value")
        void analyze_returnsValidBigDecimalValue() {
            Transaction income = createMockIncomeTransaction(new BigDecimal("100.00"), LocalDate.now());

            AnalysisMetric result = strategy.analyze(List.of(income));

            ScalarMetric scalarResult = (ScalarMetric) result;
            assertNotNull(scalarResult.value(),
                    "ScalarMetric value should not be null");
        }
    }

    // ========================================
    // Helper Methods
    // ========================================

    /**
     * Creates a mock income transaction with the given amount and date.
     *
     * @param amount the transaction amount (positive)
     * @param date   the transaction date
     * @return a mocked income Transaction
     */
    private Transaction createMockIncomeTransaction(BigDecimal amount, LocalDate date) {
        Transaction transaction = mock(Transaction.class);
        when(transaction.isIncome()).thenReturn(true);
        when(transaction.isExpense()).thenReturn(false);
        when(transaction.getAmount()).thenReturn(TransactionAmount.income(amount));
        when(transaction.getTransactionDate()).thenReturn(TransactionDate.of(date));
        return transaction;
    }

    /**
     * Creates a mock expense transaction with the given amount and date.
     *
     * @param amount the transaction amount (positive, will be stored as negative)
     * @param date   the transaction date
     * @return a mocked expense Transaction
     */
    private Transaction createMockExpenseTransaction(BigDecimal amount, LocalDate date) {
        Transaction transaction = mock(Transaction.class);
        when(transaction.isIncome()).thenReturn(false);
        when(transaction.isExpense()).thenReturn(true);
        when(transaction.getAmount()).thenReturn(TransactionAmount.expense(amount));
        when(transaction.getTransactionDate()).thenReturn(TransactionDate.of(date));
        return transaction;
    }
}

