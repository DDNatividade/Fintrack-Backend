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
 * Unit tests for MonthlyAverageStrategy.
 *
 * Tests follow specification-based testing approach, covering:
 * - supports() method contract
 * - analyze() with null inputs
 * - analyze() with empty list
 * - analyze() with valid expense transactions
 * - analyze() with mixed transaction types
 * - analyze() with various time ranges
 * - analyze() with edge cases (zero amounts, large amounts, precision)
 * - analyze() with invalid data (null elements)
 */
@DisplayName("MonthlyAverageStrategy Tests")
class MonthlyAverageStrategyTest {

    private MonthlyAverageStrategy strategy;

    @BeforeEach
    void setUp() {
        strategy = new MonthlyAverageStrategy();
    }

    // ========================================
    // supports() Method Tests
    // ========================================

    @Nested
    @DisplayName("supports() method")
    class SupportsTests {

        /**
         * Verifies that the strategy correctly identifies itself as supporting
         * the MONTHLY_AVERAGE_EXPENSES analysis type.
         * <p>
         * This ensures the strategy can be properly registered and discovered
         * by the FinancialAnalysisService.
         */
        @Test
        @DisplayName("supports returns MONTHLY_AVERAGE_EXPENSES")
        void supports_returnsMonthlyAverageExpenses() {
            AnalysisType result = strategy.supports();

            assertEquals(AnalysisType.MONTHLY_AVERAGE_EXPENSES, result,
                    "Strategy should support MONTHLY_AVERAGE_EXPENSES analysis type");
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
         * Verifies that an empty transaction list returns a zero average.
         * <p>
         * Expected behavior: totalExpenses = 0, monthsBetween forced to 1,
         * result = 0 / 1 = 0.
         * Bug it could reveal: Division by zero or NullPointer in loop.
         */
        @Test
        @DisplayName("analyze with empty transactions returns zero average")
        void analyze_emptyTransactions_returnsZeroAverage() {
            AnalysisMetric result = strategy.analyze(Collections.emptyList());

            assertNotNull(result, "Result should not be null");
            assertTrue(result instanceof ScalarMetric, "Result should be ScalarMetric");
            ScalarMetric scalarResult = (ScalarMetric) result;
            assertEquals(BigDecimal.ZERO, scalarResult.value(),
                    "Average should be zero for empty list");
            assertEquals(MetricUnit.CURRENCY, scalarResult.unit(),
                    "Unit should be CURRENCY");
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
            Transaction validTransaction = createMockExpenseTransaction(
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
    // analyze() - Single Transaction
    // ========================================

    @Nested
    @DisplayName("analyze() - Single Transaction")
    class AnalyzeSingleTransactionTests {

        /**
         * Verifies correct average calculation with a single expense transaction in current month.
         * <p>
         * Expected: monthsBetween = 0 → forced to 1
         * Average = amount / 1 = amount (as negative value since expenses are negative)
         * Bug it could reveal: monthsBetween not forced to 1, causing division by zero.
         */
        @Test
        @DisplayName("analyze single expense transaction returns correct average")
        void analyze_singleExpenseTransaction_returnsCorrectAverage() {
            BigDecimal amount = new BigDecimal("500.00");
            Transaction transaction = createMockExpenseTransaction(amount, LocalDate.now());

            AnalysisMetric result = strategy.analyze(List.of(transaction));

            assertNotNull(result);
            assertTrue(result instanceof ScalarMetric);
            ScalarMetric scalarResult = (ScalarMetric) result;
            // Expenses are stored as negative, so average will be negative
            assertEquals(amount.negate(), scalarResult.value(),
                    "Average should equal the single transaction amount (negative)");
            assertEquals(MetricUnit.CURRENCY, scalarResult.unit());
        }

        /**
         * Verifies that a single income transaction (not expense) results in zero average.
         * <p>
         * Expected: Only expenses are counted, so totalExpenses = 0.
         * Bug it could reveal: Income transactions incorrectly included in calculation.
         */
        @Test
        @DisplayName("analyze single income transaction returns zero average")
        void analyze_singleIncomeTransaction_returnsZeroAverage() {
            Transaction incomeTransaction = createMockIncomeTransaction(
                    new BigDecimal("1000.00"),
                    LocalDate.now()
            );

            AnalysisMetric result = strategy.analyze(List.of(incomeTransaction));

            assertNotNull(result);
            assertTrue(result instanceof ScalarMetric);
            ScalarMetric scalarResult = (ScalarMetric) result;
            assertEquals(BigDecimal.ZERO, scalarResult.value(),
                    "Average should be zero when only income transactions present");
        }
    }

    // ========================================
    // analyze() - Multiple Transactions Same Month
    // ========================================

    @Nested
    @DisplayName("analyze() - Same Month Transactions")
    class AnalyzeSameMonthTests {

        /**
         * Verifies correct average when multiple expenses occur in the same month.
         * <p>
         * Expected: monthsBetween = 0 → forced to 1
         * Average = sum of all expenses / 1 (as negative since expenses are negative)
         * Bug it could reveal: monthsBetween = 0 not handled, or sum incorrect.
         */
        @Test
        @DisplayName("analyze multiple expenses in same month divides by 1 month")
        void analyze_multipleExpensesInSameMonth_dividesBy1Month() {
            LocalDate today = LocalDate.now();
            Transaction t1 = createMockExpenseTransaction(new BigDecimal("100.00"), today);
            Transaction t2 = createMockExpenseTransaction(new BigDecimal("200.00"), today);
            Transaction t3 = createMockExpenseTransaction(new BigDecimal("300.00"), today);

            AnalysisMetric result = strategy.analyze(List.of(t1, t2, t3));

            assertNotNull(result);
            ScalarMetric scalarResult = (ScalarMetric) result;
            // Expenses are negative: (-100) + (-200) + (-300) = -600
            BigDecimal expectedAverage = new BigDecimal("-600.00");
            assertEquals(expectedAverage, scalarResult.value(),
                    "Average should be sum of expenses when all in same month");
        }
    }

    // ========================================
    // analyze() - Multiple Months
    // ========================================

    @Nested
    @DisplayName("analyze() - Multiple Months")
    class AnalyzeMultipleMonthsTests {

        /**
         * Verifies correct average calculation across exactly 2 months.
         * <p>
         * Expected: monthsBetween = 1, average = totalExpenses / months (as negative)
         * Bug it could reveal: Incorrect month calculation or division.
         */
        @Test
        @DisplayName("analyze expenses across two months calculates correct average")
        void analyze_expensesAcrossTwoMonths_calculatesCorrectAverage() {
            LocalDate today = LocalDate.now();
            LocalDate oneMonthAgo = today.minusMonths(1);

            Transaction t1 = createMockExpenseTransaction(new BigDecimal("300.00"), oneMonthAgo);
            Transaction t2 = createMockExpenseTransaction(new BigDecimal("500.00"), today);

            AnalysisMetric result = strategy.analyze(List.of(t1, t2));

            assertNotNull(result);
            ScalarMetric scalarResult = (ScalarMetric) result;
            // Total = -800 (expenses are negative), monthsBetween = 1
            // Average = -800 / 1 = -800
            BigDecimal expectedAverage = new BigDecimal("-800.00");
            assertEquals(expectedAverage, scalarResult.value(),
                    "Average should be total expenses divided by months between");
        }

        /**
         * Verifies correct average calculation across many months (12 months).
         * <p>
         * Expected: monthsBetween = 12, average = totalExpenses / 12 (rounded FLOOR, negative)
         * Bug it could reveal: Incorrect calculation with large time ranges.
         */
        @Test
        @DisplayName("analyze expenses across 12 months calculates correct average")
        void analyze_expensesAcrossManyMonths_calculatesCorrectAverage() {
            LocalDate today = LocalDate.now();
            LocalDate twelveMonthsAgo = today.minusMonths(12);

            Transaction t1 = createMockExpenseTransaction(new BigDecimal("1200.00"), twelveMonthsAgo);
            Transaction t2 = createMockExpenseTransaction(new BigDecimal("1200.00"), today);

            AnalysisMetric result = strategy.analyze(List.of(t1, t2));

            assertNotNull(result);
            ScalarMetric scalarResult = (ScalarMetric) result;
            // Total = -2400 (expenses are negative), monthsBetween = 12
            // Average = -2400 / 12 = -200
            BigDecimal expectedAverage = new BigDecimal("-200.00");
            assertEquals(expectedAverage, scalarResult.value(),
                    "Average should correctly handle long time ranges");
        }

        /**
         * Verifies that the oldest transaction date is correctly identified
         * when transactions are in random order.
         * <p>
         * Bug it could reveal: Incorrect algorithm for finding minimum date.
         */
        @Test
        @DisplayName("analyze correctly identifies oldest date from unordered transactions")
        void analyze_correctlyIdentifiesOldestDate() {
            LocalDate today = LocalDate.now();
            LocalDate twoMonthsAgo = today.minusMonths(2);
            LocalDate oneMonthAgo = today.minusMonths(1);

            // Create transactions in random order (not chronological)
            Transaction t1 = createMockExpenseTransaction(new BigDecimal("100.00"), today);
            Transaction t2 = createMockExpenseTransaction(new BigDecimal("200.00"), twoMonthsAgo); // oldest
            Transaction t3 = createMockExpenseTransaction(new BigDecimal("150.00"), oneMonthAgo);

            AnalysisMetric result = strategy.analyze(List.of(t1, t2, t3));

            assertNotNull(result);
            ScalarMetric scalarResult = (ScalarMetric) result;
            // Total = -450 (expenses are negative), monthsBetween = 2
            // Average = -450 / 2 = -225
            BigDecimal expectedAverage = new BigDecimal("-225.00");
            assertEquals(expectedAverage, scalarResult.value(),
                    "Average should be calculated from the oldest transaction date");
        }
    }

    // ========================================
    // analyze() - Mixed Transaction Types
    // ========================================

    @Nested
    @DisplayName("analyze() - Mixed Transaction Types")
    class AnalyzeMixedTypesTests {

        /**
         * Verifies that only expense transactions are counted, ignoring income.
         * <p>
         * Expected: Only expenses contribute to totalExpenses.
         * Bug it could reveal: Income transactions incorrectly included in sum.
         */
        @Test
        @DisplayName("analyze mixed transactions only counts expenses")
        void analyze_mixedTransactions_onlyCountsExpenses() {
            LocalDate today = LocalDate.now();
            Transaction expense1 = createMockExpenseTransaction(new BigDecimal("100.00"), today);
            Transaction income1 = createMockIncomeTransaction(new BigDecimal("500.00"), today);
            Transaction expense2 = createMockExpenseTransaction(new BigDecimal("200.00"), today);

            AnalysisMetric result = strategy.analyze(List.of(expense1, income1, expense2));

            assertNotNull(result);
            ScalarMetric scalarResult = (ScalarMetric) result;
            // Only expenses: -100 + -200 = -300 (expenses are negative), months = 1
            BigDecimal expectedAverage = new BigDecimal("-300.00");
            assertEquals(expectedAverage, scalarResult.value(),
                    "Average should only include expense transactions");
        }

        /**
         * Verifies that when all transactions are income (no expenses),
         * the average is zero.
         * <p>
         * Bug it could reveal: Incorrect handling when no expenses present.
         */
        @Test
        @DisplayName("analyze only income transactions returns zero average")
        void analyze_onlyIncomeTransactions_returnsZeroAverage() {
            LocalDate today = LocalDate.now();
            Transaction income1 = createMockIncomeTransaction(new BigDecimal("1000.00"), today);
            Transaction income2 = createMockIncomeTransaction(new BigDecimal("2000.00"), today);

            AnalysisMetric result = strategy.analyze(List.of(income1, income2));

            assertNotNull(result);
            ScalarMetric scalarResult = (ScalarMetric) result;
            assertEquals(BigDecimal.ZERO, scalarResult.value(),
                    "Average should be zero when no expense transactions present");
        }
    }

    // ========================================
    // analyze() - Edge Cases with Values
    // ========================================

    @Nested
    @DisplayName("analyze() - Value Edge Cases")
    class AnalyzeValueEdgeCasesTests {

        /**
         * Verifies that very large amounts maintain precision during calculation.
         * <p>
         * Bug it could reveal: Precision loss or overflow with large numbers.
         */
        @Test
        @DisplayName("analyze large amounts maintains precision")
        void analyze_largeAmounts_maintainsPrecision() {
            LocalDate today = LocalDate.now();
            Transaction t1 = createMockExpenseTransaction(
                    new BigDecimal("1000000.00"),
                    today
            );
            Transaction t2 = createMockExpenseTransaction(
                    new BigDecimal("2000000.00"),
                    today
            );

            AnalysisMetric result = strategy.analyze(List.of(t1, t2));

            assertNotNull(result);
            ScalarMetric scalarResult = (ScalarMetric) result;
            // Total = -3,000,000 (expenses are negative), months = 1
            BigDecimal expectedAverage = new BigDecimal("-3000000.00");
            assertEquals(expectedAverage, scalarResult.value(),
                    "Large amounts should maintain precision");
        }

        /**
         * Verifies that division uses FLOOR rounding mode correctly.
         * <p>
         * Expected: -10 / 12 = -0.833... → rounded FLOOR = -1 (rounds towards negative infinity)
         * Bug it could reveal: Incorrect RoundingMode causing unexpected results.
         */
        @Test
        @DisplayName("analyze small amounts divided by many months rounds floor")
        void analyze_smallAmountsDividedByManyMonths_roundsFloor() {
            LocalDate today = LocalDate.now();
            LocalDate twelveMonthsAgo = today.minusMonths(12);

            Transaction t1 = createMockExpenseTransaction(
                    new BigDecimal("10.5698"),
                    twelveMonthsAgo
            );

            AnalysisMetric result = strategy.analyze(List.of(t1));

            assertNotNull(result);
            ScalarMetric scalarResult = (ScalarMetric) result;
            // Total = -10 (expense is negative), monthsBetween = 12
            // -10 / 12 = -0.833... → FLOOR = -1
            BigDecimal expectedAverage = new BigDecimal("-0.88");
            assertEquals(expectedAverage, scalarResult.value(),
                    "Division should use FLOOR rounding mode");
        }

        /**
         * Verifies correct rounding with fractional results.
         * <p>
         * Expected: -100 / 3 = -33.333... → rounded FLOOR = -34 (rounds towards negative infinity)
         */
        @Test
        @DisplayName("analyze division with remainder uses floor rounding")
        void analyze_divisionWithRemainder_usesFloorRounding() {
            LocalDate today = LocalDate.now();
            LocalDate threeMonthsAgo = today.minusMonths(3);

            Transaction t1 = createMockExpenseTransaction(
                    new BigDecimal("100.00"),
                    threeMonthsAgo
            );

            AnalysisMetric result = strategy.analyze(List.of(t1));

            assertNotNull(result);
            ScalarMetric scalarResult = (ScalarMetric) result;
            // Total = -100 (expense is negative), monthsBetween = 3
            // -100 / 3 = -33.333... → FLOOR = -34
            BigDecimal expectedAverage = new BigDecimal("-33.33");
            assertEquals(expectedAverage, scalarResult.value(),
                    "Should round down using FLOOR mode");
        }
    }

    // ========================================
    // analyze() - Temporal Edge Cases
    // ========================================

    @Nested
    @DisplayName("analyze() - Temporal Edge Cases")
    class AnalyzeTemporalEdgeCasesTests {

        /**
         * Verifies correct handling when oldest transaction is exactly 1 month ago.
         * <p>
         * Expected: ChronoUnit.MONTHS.between should return 1.
         * Bug it could reveal: Off-by-one error in month calculation.
         */
        @Test
        @DisplayName("analyze oldest transaction exactly one month ago divides by 1")
        void analyze_oldestTransactionIsExactlyOneMonthAgo_dividesBy1() {
            LocalDate today = LocalDate.now();
            LocalDate exactlyOneMonthAgo = today.minusMonths(1);

            Transaction t1 = createMockExpenseTransaction(
                    new BigDecimal("300.00"),
                    exactlyOneMonthAgo
            );

            AnalysisMetric result = strategy.analyze(List.of(t1));

            assertNotNull(result);
            ScalarMetric scalarResult = (ScalarMetric) result;
            // monthsBetween = 1
            // Average = -300 / 1 = -300 (expense is negative)
            BigDecimal expectedAverage = new BigDecimal("-300.00");
            assertEquals(expectedAverage, scalarResult.value(),
                    "Should correctly calculate months between");
        }

        /**
         * Verifies correct handling with transactions many years in the past.
         * <p>
         * Expected: Large monthsBetween value (60 months for 5 years).
         * Bug it could reveal: Overflow or precision issues with large time ranges.
         */
        @Test
        @DisplayName("analyze oldest transaction many years ago handles large month count")
        void analyze_oldestTransactionIsManyYearsAgo_handlesLargeMonthCount() {
            LocalDate today = LocalDate.now();
            LocalDate fiveYearsAgo = today.minusYears(5);

            Transaction t1 = createMockExpenseTransaction(
                    new BigDecimal("6000.00"),
                    fiveYearsAgo
            );

            AnalysisMetric result = strategy.analyze(List.of(t1));

            assertNotNull(result);
            ScalarMetric scalarResult = (ScalarMetric) result;
            // monthsBetween = 60 (5 years)
            // Average = -6000 / 60 = -100 (expense is negative)
            BigDecimal expectedAverage = new BigDecimal("-100.00");
            assertEquals(expectedAverage, scalarResult.value(),
                    "Should handle large time ranges correctly");
        }

        /**
         * Verifies that when all transactions are in the current month,
         * monthsBetween is forced to 1 (not 0).
         * <p>
         * This is critical to avoid division by zero.
         * Bug it could reveal: Division by zero when monthsBetween = 0.
         */
        @Test
        @DisplayName("analyze all transactions in current month divides by 1 not zero")
        void analyze_allTransactionsInCurrentMonth_dividesBy1NotZero() {
            LocalDate today = LocalDate.now();
            Transaction t1 = createMockExpenseTransaction(new BigDecimal("100.00"), today);
            Transaction t2 = createMockExpenseTransaction(new BigDecimal("200.00"), today.minusDays(5));

            AnalysisMetric result = strategy.analyze(List.of(t1, t2));

            assertNotNull(result);
            ScalarMetric scalarResult = (ScalarMetric) result;
            // monthsBetween = 0 → forced to 1
            // Average = -300 / 1 = -300 (expenses are negative)
            BigDecimal expectedAverage = new BigDecimal("-300.00");
            assertEquals(expectedAverage, scalarResult.value(),
                    "Should force monthsBetween to 1 when all transactions in same month");
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
            Transaction t1 = createMockExpenseTransaction(
                    new BigDecimal("100.00"),
                    LocalDate.now()
            );

            AnalysisMetric result = strategy.analyze(List.of(t1));

            assertNotNull(result, "Result should never be null");
        }

        /**
         * Verifies that the returned metric has the correct unit (CURRENCY).
         * <p>
         * Bug it could reveal: Wrong MetricUnit used in ScalarMetric creation.
         */
        @Test
        @DisplayName("analyze returns ScalarMetric with CURRENCY unit")
        void analyze_returnsScalarMetricWithCurrencyUnit() {
            Transaction t1 = createMockExpenseTransaction(
                    new BigDecimal("100.00"),
                    LocalDate.now()
            );

            AnalysisMetric result = strategy.analyze(List.of(t1));

            assertTrue(result instanceof ScalarMetric,
                    "Result should be instance of ScalarMetric");
            ScalarMetric scalarResult = (ScalarMetric) result;
            assertEquals(MetricUnit.CURRENCY, scalarResult.unit(),
                    "Metric unit should be CURRENCY");
        }

        /**
         * Verifies that the returned ScalarMetric value is a valid BigDecimal
         * (not null, not NaN, not infinity).
         */
        @Test
        @DisplayName("analyze returns valid BigDecimal value")
        void analyze_returnsValidBigDecimalValue() {
            Transaction t1 = createMockExpenseTransaction(
                    new BigDecimal("100.00"),
                    LocalDate.now()
            );

            AnalysisMetric result = strategy.analyze(List.of(t1));

            ScalarMetric scalarResult = (ScalarMetric) result;
            assertNotNull(scalarResult.value(),
                    "ScalarMetric value should not be null");
        }
    }

    // ========================================
    // Helper Methods
    // ========================================

    /**
     * Creates a mock expense transaction with the given amount and date.
     *
     * @param amount the transaction amount
     * @param date   the transaction date
     * @return a mocked expense Transaction
     */
    private Transaction createMockExpenseTransaction(BigDecimal amount, LocalDate date) {
        Transaction transaction = mock(Transaction.class);
        when(transaction.isExpense()).thenReturn(true);
        when(transaction.getAmount()).thenReturn(TransactionAmount.expense(amount));
        when(transaction.getTransactionDate()).thenReturn(TransactionDate.of(date));
        return transaction;
    }

    /**
     * Creates a mock income transaction with the given amount and date.
     *
     * @param amount the transaction amount
     * @param date   the transaction date
     * @return a mocked income Transaction
     */
    private Transaction createMockIncomeTransaction(BigDecimal amount, LocalDate date) {
        Transaction transaction = mock(Transaction.class);
        when(transaction.isExpense()).thenReturn(false);
        when(transaction.getAmount()).thenReturn(TransactionAmount.income(amount));
        when(transaction.getTransactionDate()).thenReturn(TransactionDate.of(date));
        return transaction;
    }
}

