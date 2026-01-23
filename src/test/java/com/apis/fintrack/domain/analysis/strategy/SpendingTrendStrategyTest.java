package com.apis.fintrack.domain.analysis.strategy;

import com.apis.fintrack.domain.analysis.model.kpi.AnalysisType;
import com.apis.fintrack.domain.analysis.model.metric.AnalysisMetric;
import com.apis.fintrack.domain.analysis.model.metric.MetricUnit;
import com.apis.fintrack.domain.analysis.model.metric.ScalarMetric;
import com.apis.fintrack.domain.transaction.model.Category;
import com.apis.fintrack.domain.transaction.model.Transaction;
import com.apis.fintrack.domain.transaction.model.TransactionAmount;
import com.apis.fintrack.domain.transaction.model.TransactionCategoryEnum;
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
 * Unit tests for SpendingTrendStrategy.
 *
 * Tests follow specification-based testing approach, covering:
 * - supports() method contract
 * - analyze() with null inputs
 * - analyze() with empty list
 * - analyze() with valid expense transactions in different time periods
 * - analyze() with category filtering
 * - analyze() with various trend scenarios (increasing, decreasing, stable)
 * - analyze() with edge cases (no previous expenses, no recent expenses, both zero)
 * - analyze() with boundary dates
 */
@DisplayName("SpendingTrendStrategy Tests")
class SpendingTrendStrategyTest {

    private SpendingTrendStrategy strategy;

    @BeforeEach
    void setUp() {
        strategy = new SpendingTrendStrategy();
    }

    // ========================================
    // supports() Method Tests
    // ========================================

    @Nested
    @DisplayName("supports() method")
    class SupportsTests {

        /**
         * Verifies that the strategy correctly identifies itself as supporting
         * the SPENDING_TREND analysis type.
         * <p>
         * This ensures the strategy can be properly registered and discovered
         * by the FinancialAnalysisService.
         */
        @Test
        @DisplayName("supports returns SPENDING_TREND")
        void supports_returnsSpendingTrend() {
            AnalysisType result = strategy.supports();

            assertEquals(AnalysisType.SPENDING_TREND, result,
                    "Strategy should support SPENDING_TREND analysis type");
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
         * Verifies that an empty transaction list returns 0% trend (no change).
         * <p>
         * Expected behavior: previousExpenses = 0, recentExpenses = 0,
         * result = 0% (no expenses means no trend).
         * Bug it could reveal: Division by zero or incorrect handling of empty list.
         */
        @Test
        @DisplayName("analyze with empty transactions returns zero trend")
        void analyze_emptyTransactions_returnsZeroTrend() {
            AnalysisMetric result = strategy.analyze(Collections.emptyList());

            assertNotNull(result, "Result should not be null");
            assertTrue(result instanceof ScalarMetric, "Result should be ScalarMetric");
            ScalarMetric scalarResult = (ScalarMetric) result;
            assertEquals(BigDecimal.ZERO, scalarResult.value(),
                    "Trend should be zero for empty list");
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
            LocalDate today = LocalDate.now();
            Transaction validTransaction = createMockExpenseTransaction(
                    new BigDecimal("100.00"),
                    today.minusDays(10),
                    TransactionCategoryEnum.ALIMENTATION
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
    // analyze() - No Previous Expenses
    // ========================================

    @Nested
    @DisplayName("analyze() - No Previous Expenses")
    class AnalyzeNoPreviousExpensesTests {

        /**
         * Verifies behavior when there are no expenses in the previous period
         * but there are expenses in the recent period.
         * <p>
         * Expected: previousExpenses = 0, division by zero avoided, returns 0%.
         * Bug it could reveal: Division by zero error or incorrect handling.
         */
        @Test
        @DisplayName("analyze with no previous expenses returns zero trend")
        void analyze_noPreviousExpenses_returnsZeroTrend() {
            LocalDate today = LocalDate.now();
            // Only recent expenses (last 10 days)
            Transaction recentExpense1 = createMockExpenseTransaction(
                    new BigDecimal("100.00"),
                    today.minusDays(10),
                    TransactionCategoryEnum.ALIMENTATION
            );
            Transaction recentExpense2 = createMockExpenseTransaction(
                    new BigDecimal("50.00"),
                    today.minusDays(5),
                    TransactionCategoryEnum.TRANSPORTATION
            );

            AnalysisMetric result = strategy.analyze(List.of(recentExpense1, recentExpense2));

            assertNotNull(result);
            ScalarMetric scalarResult = (ScalarMetric) result;
            assertEquals(BigDecimal.ZERO, scalarResult.value(),
                    "Trend should be 0% when there are no previous expenses");
            assertEquals(MetricUnit.PERCENT, scalarResult.unit());
        }

        /**
         * Verifies that when previous expenses are exactly zero,
         * the method returns 0% without attempting division by zero.
         * <p>
         * Bug it could reveal: ArithmeticException due to division by zero.
         */
        @Test
        @DisplayName("analyze with zero previous expenses avoids division by zero")
        void analyze_zeroPreviousExpenses_avoidsDivisionByZero() {
            LocalDate today = LocalDate.now();
            Transaction recentExpense = createMockExpenseTransaction(
                    new BigDecimal("200.00"),
                    today.minusDays(15),
                    TransactionCategoryEnum.ALIMENTATION
            );

            assertDoesNotThrow(
                    () -> strategy.analyze(List.of(recentExpense)),
                    "Should not throw exception when previous expenses are zero"
            );

            AnalysisMetric result = strategy.analyze(List.of(recentExpense));
            ScalarMetric scalarResult = (ScalarMetric) result;
            assertEquals(BigDecimal.ZERO, scalarResult.value(),
                    "Should return 0% when previous expenses are zero");
        }
    }

    // ========================================
    // analyze() - No Recent Expenses
    // ========================================

    @Nested
    @DisplayName("analyze() - No Recent Expenses")
    class AnalyzeNoRecentExpensesTests {

        /**
         * Verifies correct calculation when there are no recent expenses
         * but there were expenses in the previous period.
         * <p>
         * Expected: recentExpenses = 0, previousExpenses > 0
         * Trend = (0 - previousExpenses) / previousExpenses * 100 = -100%
         * (Complete reduction in spending)
         * Bug it could reveal: Incorrect calculation when recent is zero.
         */
        @Test
        @DisplayName("analyze with no recent expenses returns -100% trend")
        void analyze_noRecentExpenses_returnsNegative100Trend() {
            LocalDate today = LocalDate.now();
            // Only previous expenses (31-60 days ago)
            Transaction previousExpense1 = createMockExpenseTransaction(
                    new BigDecimal("100.00"),
                    today.minusDays(40),
                    TransactionCategoryEnum.ALIMENTATION
            );
            Transaction previousExpense2 = createMockExpenseTransaction(
                    new BigDecimal("50.00"),
                    today.minusDays(35),
                    TransactionCategoryEnum.TRANSPORTATION
            );

            AnalysisMetric result = strategy.analyze(List.of(previousExpense1, previousExpense2));

            assertNotNull(result);
            ScalarMetric scalarResult = (ScalarMetric) result;
            // previousExpenses = -100 + -50 = -150 (expenses are negative)
            // recentExpenses = 0
            // change = 0 - (-150) = 150
            // trend = (150 / -150) * 100 = -100%
            BigDecimal expectedTrend = new BigDecimal("-100.0000");
            assertEquals(expectedTrend, scalarResult.value(),
                    "Trend should be -100% when recent expenses dropped to zero");
        }
    }

    // ========================================
    // analyze() - Increasing Trend
    // ========================================

    @Nested
    @DisplayName("analyze() - Increasing Trend")
    class AnalyzeIncreasingTrendTests {

        /**
         * Verifies correct calculation when recent expenses are higher than previous.
         * <p>
         * Example:
         * - Previous period (31-60 days ago): -100 (expenses are negative)
         * - Recent period (0-30 days ago): -150
         * - Change = -150 - (-100) = -50
         * - Trend = (-50 / -100) * 100 = 50% (50% increase in spending)
         * <p>
         * Bug it could reveal: Incorrect sign handling or calculation.
         */
        @Test
        @DisplayName("analyze with increasing expenses returns positive trend")
        void analyze_increasingExpenses_returnsPositiveTrend() {
            LocalDate today = LocalDate.now();

            // Previous period expenses: 31-60 days ago
            Transaction previousExpense = createMockExpenseTransaction(
                    new BigDecimal("100.00"),
                    today.minusDays(45),
                    TransactionCategoryEnum.ALIMENTATION
            );

            // Recent period expenses: 0-30 days ago
            Transaction recentExpense = createMockExpenseTransaction(
                    new BigDecimal("150.00"),
                    today.minusDays(15),
                    TransactionCategoryEnum.ALIMENTATION
            );

            AnalysisMetric result = strategy.analyze(List.of(previousExpense, recentExpense));

            assertNotNull(result);
            ScalarMetric scalarResult = (ScalarMetric) result;
            // previousExpenses = -100 (absolute: 100)
            // recentExpenses = -150 (absolute: 150)
            // Formula: (|recent| - |previous|) / |previous| * 100
            // = (150 - 100) / 100 * 100 = 50%
            BigDecimal expectedTrend = new BigDecimal("50.0000");
            assertEquals(expectedTrend, scalarResult.value(),
                    "Trend should be 50% when recent expenses increased by 50%");
        }

        /**
         * Verifies calculation with multiple transactions in both periods.
         * <p>
         * Bug it could reveal: Incorrect aggregation across periods.
         */
        @Test
        @DisplayName("analyze with multiple transactions shows increasing trend")
        void analyze_multipleTransactions_increasingTrend() {
            LocalDate today = LocalDate.now();

            // Previous period: 200 total
            Transaction prev1 = createMockExpenseTransaction(
                    new BigDecimal("100.00"),
                    today.minusDays(40),
                    TransactionCategoryEnum.ALIMENTATION
            );
            Transaction prev2 = createMockExpenseTransaction(
                    new BigDecimal("100.00"),
                    today.minusDays(35),
                    TransactionCategoryEnum.TRANSPORTATION
            );

            // Recent period: 300 total
            Transaction recent1 = createMockExpenseTransaction(
                    new BigDecimal("150.00"),
                    today.minusDays(20),
                    TransactionCategoryEnum.ALIMENTATION
            );
            Transaction recent2 = createMockExpenseTransaction(
                    new BigDecimal("150.00"),
                    today.minusDays(10),
                    TransactionCategoryEnum.TRANSPORTATION
            );

            AnalysisMetric result = strategy.analyze(List.of(prev1, prev2, recent1, recent2));

            assertNotNull(result);
            ScalarMetric scalarResult = (ScalarMetric) result;
            // previousExpenses = -200 (absolute: 200)
            // recentExpenses = -300 (absolute: 300)
            // Formula: (300 - 200) / 200 * 100 = 50%
            BigDecimal expectedTrend = new BigDecimal("50.0000");
            assertEquals(expectedTrend, scalarResult.value(),
                    "Trend should show 50% increase (from 200 to 300)");
        }

        /**
         * Verifies calculation with a doubling of expenses (100% increase).
         * <p>
         * Bug it could reveal: Incorrect percentage calculation.
         */
        @Test
        @DisplayName("analyze with doubled expenses returns 100% trend")
        void analyze_doubledExpenses_returns100PercentTrend() {
            LocalDate today = LocalDate.now();

            Transaction previousExpense = createMockExpenseTransaction(
                    new BigDecimal("100.00"),
                    today.minusDays(45),
                    TransactionCategoryEnum.ALIMENTATION
            );

            Transaction recentExpense = createMockExpenseTransaction(
                    new BigDecimal("200.00"),
                    today.minusDays(15),
                    TransactionCategoryEnum.ALIMENTATION
            );

            AnalysisMetric result = strategy.analyze(List.of(previousExpense, recentExpense));

            assertNotNull(result);
            ScalarMetric scalarResult = (ScalarMetric) result;
            // previousExpenses = -100 (absolute: 100)
            // recentExpenses = -200 (absolute: 200)
            // Formula: (200 - 100) / 100 * 100 = 100%
            BigDecimal expectedTrend = new BigDecimal("100.0000");
            assertEquals(expectedTrend, scalarResult.value(),
                    "Trend should be 100% when expenses doubled");
        }
    }

    // ========================================
    // analyze() - Decreasing Trend
    // ========================================

    @Nested
    @DisplayName("analyze() - Decreasing Trend")
    class AnalyzeDecreasingTrendTests {

        /**
         * Verifies correct calculation when recent expenses are lower than previous.
         * <p>
         * Example:
         * - Previous period: -200
         * - Recent period: -150
         * - Change = -150 - (-200) = 50
         * - Trend = (50 / -200) * 100 = -25% (25% reduction in spending)
         * <p>
         * Bug it could reveal: Incorrect sign or calculation for decreasing trend.
         */
        @Test
        @DisplayName("analyze with decreasing expenses returns negative trend")
        void analyze_decreasingExpenses_returnsNegativeTrend() {
            LocalDate today = LocalDate.now();

            Transaction previousExpense = createMockExpenseTransaction(
                    new BigDecimal("200.00"),
                    today.minusDays(45),
                    TransactionCategoryEnum.ALIMENTATION
            );

            Transaction recentExpense = createMockExpenseTransaction(
                    new BigDecimal("150.00"),
                    today.minusDays(15),
                    TransactionCategoryEnum.ALIMENTATION
            );

            AnalysisMetric result = strategy.analyze(List.of(previousExpense, recentExpense));

            assertNotNull(result);
            ScalarMetric scalarResult = (ScalarMetric) result;
            // previousExpenses = -200 (absolute: 200)
            // recentExpenses = -150 (absolute: 150)
            // Formula: (150 - 200) / 200 * 100 = -25%
            BigDecimal expectedTrend = new BigDecimal("-25.0000");
            assertEquals(expectedTrend, scalarResult.value(),
                    "Trend should be -25% when expenses decreased by 25%");
        }

        /**
         * Verifies calculation when expenses are cut in half (50% reduction).
         * <p>
         * Bug it could reveal: Incorrect percentage calculation for reduction.
         */
        @Test
        @DisplayName("analyze with halved expenses returns -50% trend")
        void analyze_halvedExpenses_returnsNegative50Trend() {
            LocalDate today = LocalDate.now();

            Transaction previousExpense = createMockExpenseTransaction(
                    new BigDecimal("200.00"),
                    today.minusDays(45),
                    TransactionCategoryEnum.ALIMENTATION
            );

            Transaction recentExpense = createMockExpenseTransaction(
                    new BigDecimal("100.00"),
                    today.minusDays(15),
                    TransactionCategoryEnum.ALIMENTATION
            );

            AnalysisMetric result = strategy.analyze(List.of(previousExpense, recentExpense));

            assertNotNull(result);
            ScalarMetric scalarResult = (ScalarMetric) result;
            // previousExpenses = -200 (absolute: 200)
            // recentExpenses = -100 (absolute: 100)
            // Formula: (100 - 200) / 200 * 100 = -50%
            BigDecimal expectedTrend = new BigDecimal("-50.0000");
            assertEquals(expectedTrend, scalarResult.value(),
                    "Trend should be -50% when expenses were cut in half");
        }
    }

    // ========================================
    // analyze() - Stable Trend (No Change)
    // ========================================

    @Nested
    @DisplayName("analyze() - Stable Trend")
    class AnalyzeStableTrendTests {

        /**
         * Verifies that when expenses remain the same, trend is 0%.
         * <p>
         * Previous = -100, Recent = -100
         * Change = -100 - (-100) = 0
         * Trend = (0 / -100) * 100 = 0%
         * <p>
         * Bug it could reveal: Incorrect handling of no change scenario.
         */
        @Test
        @DisplayName("analyze with stable expenses returns zero trend")
        void analyze_stableExpenses_returnsZeroTrend() {
            LocalDate today = LocalDate.now();

            Transaction previousExpense = createMockExpenseTransaction(
                    new BigDecimal("100.00"),
                    today.minusDays(45),
                    TransactionCategoryEnum.ALIMENTATION
            );

            Transaction recentExpense = createMockExpenseTransaction(
                    new BigDecimal("100.00"),
                    today.minusDays(15),
                    TransactionCategoryEnum.ALIMENTATION
            );

            AnalysisMetric result = strategy.analyze(List.of(previousExpense, recentExpense));

            assertNotNull(result);
            ScalarMetric scalarResult = (ScalarMetric) result;
            // previousExpenses = -100 (absolute: 100)
            // recentExpenses = -100 (absolute: 100)
            // Formula: (100 - 100) / 100 * 100 = 0%
            BigDecimal expectedTrend = new BigDecimal("0.0000");
            assertEquals(expectedTrend, scalarResult.value(),
                    "Trend should be 0% when expenses remain the same");
        }
    }

    // ========================================
    // analyze() - Boundary Dates
    // ========================================

    @Nested
    @DisplayName("analyze() - Boundary Dates")
    class AnalyzeBoundaryDatesTests {

        /**
         * Verifies correct handling of transactions exactly at period boundaries.
         * <p>
         * Tests transactions at:
         * - Today (day 0) - should be in recent period
         * - Day 30 (boundary) - should be in recent period
         * - Day 31 (boundary) - should be in previous period
         * - Day 60 (boundary) - should be in previous period
         * <p>
         * Bug it could reveal: Off-by-one errors in date filtering.
         */
        @Test
        @DisplayName("analyze handles boundary dates correctly")
        void analyze_boundaryDates_handlesCorrectly() {
            LocalDate today = LocalDate.now();

            // Exactly at today (day 0) - should be in recent period
            Transaction atToday = createMockExpenseTransaction(
                    new BigDecimal("10.00"),
                    today,
                    TransactionCategoryEnum.ALIMENTATION
            );

            // Exactly 30 days ago (boundary) - should be in recent period
            Transaction at30Days = createMockExpenseTransaction(
                    new BigDecimal("20.00"),
                    today.minusDays(30),
                    TransactionCategoryEnum.ALIMENTATION
            );

            // Exactly 31 days ago (boundary) - should be in previous period
            Transaction at31Days = createMockExpenseTransaction(
                    new BigDecimal("30.00"),
                    today.minusDays(31),
                    TransactionCategoryEnum.ALIMENTATION
            );

            // Exactly 60 days ago (boundary) - should be in previous period
            Transaction at60Days = createMockExpenseTransaction(
                    new BigDecimal("40.00"),
                    today.minusDays(60),
                    TransactionCategoryEnum.ALIMENTATION
            );

            AnalysisMetric result = strategy.analyze(
                    List.of(atToday, at30Days, at31Days, at60Days)
            );

            assertNotNull(result);
            ScalarMetric scalarResult = (ScalarMetric) result;
            // Recent period (0-30 days): -10 + -20 = -30
            // Previous period (31-60 days): -30 + -40 = -70
            // change = -30 - (-70) = 40
            // trend = (40 / -70) * 100 ≈ -57.14%
            // This should show a decreasing trend
            assertTrue(scalarResult.value().compareTo(BigDecimal.ZERO) < 0,
                    "Trend should be negative (spending decreased)");
        }

        /**
         * Verifies that transactions outside the 60-day window are ignored.
         * <p>
         * Bug it could reveal: Incorrect date range filtering.
         */
        @Test
        @DisplayName("analyze ignores transactions outside 60-day window")
        void analyze_transactionsOutsideWindow_ignored() {
            LocalDate today = LocalDate.now();

            // Transaction outside window (61+ days ago)
            Transaction veryOld = createMockExpenseTransaction(
                    new BigDecimal("1000.00"),
                    today.minusDays(65),
                    TransactionCategoryEnum.ALIMENTATION
            );

            // Recent transaction
            Transaction recent = createMockExpenseTransaction(
                    new BigDecimal("100.00"),
                    today.minusDays(10),
                    TransactionCategoryEnum.ALIMENTATION
            );

            AnalysisMetric result = strategy.analyze(List.of(veryOld, recent));

            assertNotNull(result);
            ScalarMetric scalarResult = (ScalarMetric) result;
            // veryOld should be ignored
            // previousExpenses = 0
            // recentExpenses = -100
            // trend = 0% (no previous to compare)
            assertEquals(BigDecimal.ZERO, scalarResult.value(),
                    "Should ignore transactions older than 60 days");
        }
    }

    // ========================================
    // analyze() with Category Filter
    // ========================================

    @Nested
    @DisplayName("analyze() - Category Filtering")
    class AnalyzeCategoryFilterTests {

        /**
         * Verifies that category filtering works correctly.
         * <p>
         * When analyzing with a specific category, only transactions
         * of that category should be considered.
         * Bug it could reveal: Incorrect category filtering.
         */
        @Test
        @DisplayName("analyze with category filter only counts matching transactions")
        void analyze_withCategoryFilter_onlyCountsMatchingCategory() {
            LocalDate today = LocalDate.now();

            // Previous period - FOOD
            Transaction prevFood = createMockExpenseTransaction(
                    new BigDecimal("100.00"),
                    today.minusDays(45),
                    TransactionCategoryEnum.ALIMENTATION
            );

            // Previous period - TRANSPORT (should be ignored)
            Transaction prevTransport = createMockExpenseTransaction(
                    new BigDecimal("50.00"),
                    today.minusDays(40),
                    TransactionCategoryEnum.TRANSPORTATION
            );

            // Recent period - FOOD
            Transaction recentFood = createMockExpenseTransaction(
                    new BigDecimal("150.00"),
                    today.minusDays(15),
                    TransactionCategoryEnum.ALIMENTATION
            );

            // Recent period - TRANSPORT (should be ignored)
            Transaction recentTransport = createMockExpenseTransaction(
                    new BigDecimal("75.00"),
                    today.minusDays(10),
                    TransactionCategoryEnum.TRANSPORTATION
            );

            AnalysisMetric result = strategy.analyze(
                    List.of(prevFood, prevTransport, recentFood, recentTransport),
                    TransactionCategoryEnum.ALIMENTATION
            );

            assertNotNull(result);
            ScalarMetric scalarResult = (ScalarMetric) result;
            // Only FOOD transactions counted:
            // previousExpenses = -100 (absolute: 100)
            // recentExpenses = -150 (absolute: 150)
            // Formula: (150 - 100) / 100 * 100 = 50%
            BigDecimal expectedTrend = new BigDecimal("50.0000");
            assertEquals(expectedTrend, scalarResult.value(),
                    "Should only count FOOD category transactions");
        }

        /**
         * Verifies that null category throws NullPointerException.
         * <p>
         * Bug it could reveal: Missing null validation for category parameter.
         */
        @Test
        @DisplayName("analyze with null category throws NullPointerException")
        void analyze_withNullCategory_throwsNullPointer() {
            LocalDate today = LocalDate.now();
            Transaction transaction = createMockExpenseTransaction(
                    new BigDecimal("100.00"),
                    today.minusDays(10),
                    TransactionCategoryEnum.ALIMENTATION
            );

            assertThrows(
                    NullPointerException.class,
                    () -> strategy.analyze(List.of(transaction), null),
                    "Should throw NullPointerException when category is null"
            );
        }

        /**
         * Verifies behavior when no transactions match the category filter.
         * <p>
         * Bug it could reveal: Incorrect handling of empty filtered result.
         */
        @Test
        @DisplayName("analyze with no matching category returns zero trend")
        void analyze_noMatchingCategory_returnsZeroTrend() {
            LocalDate today = LocalDate.now();

            // Only FOOD transactions
            Transaction transaction = createMockExpenseTransaction(
                    new BigDecimal("100.00"),
                    today.minusDays(10),
                    TransactionCategoryEnum.ALIMENTATION
            );

            // Filter by TRANSPORT (no matches)
            AnalysisMetric result = strategy.analyze(
                    List.of(transaction),
                    TransactionCategoryEnum.TRANSPORTATION
            );

            assertNotNull(result);
            ScalarMetric scalarResult = (ScalarMetric) result;
            assertEquals(BigDecimal.ZERO, scalarResult.value(),
                    "Should return 0% trend when no transactions match category");
        }
    }

    // ========================================
    // analyze() - Income Transactions (Should Be Ignored)
    // ========================================

    @Nested
    @DisplayName("analyze() - Income Transactions")
    class AnalyzeIncomeTransactionsTests {

        /**
         * Verifies that income transactions are completely ignored.
         * <p>
         * The strategy only analyzes expense trends, not income trends.
         * Bug it could reveal: Income transactions incorrectly included.
         */
        @Test
        @DisplayName("analyze ignores income transactions")
        void analyze_ignoresIncomeTransactions() {
            LocalDate today = LocalDate.now();

            // Previous period - expense
            Transaction prevExpense = createMockExpenseTransaction(
                    new BigDecimal("100.00"),
                    today.minusDays(45),
                    TransactionCategoryEnum.ALIMENTATION
            );

            // Previous period - income (should be ignored)
            Transaction prevIncome = createMockIncomeTransaction(
                    new BigDecimal("500.00"),
                    today.minusDays(40)
            );

            // Recent period - expense
            Transaction recentExpense = createMockExpenseTransaction(
                    new BigDecimal("150.00"),
                    today.minusDays(15),
                    TransactionCategoryEnum.ALIMENTATION
            );

            // Recent period - income (should be ignored)
            Transaction recentIncome = createMockIncomeTransaction(
                    new BigDecimal("600.00"),
                    today.minusDays(10)
            );

            AnalysisMetric result = strategy.analyze(
                    List.of(prevExpense, prevIncome, recentExpense, recentIncome)
            );

            assertNotNull(result);
            ScalarMetric scalarResult = (ScalarMetric) result;
            // Only expenses should be counted:
            // previousExpenses = -100 (absolute: 100)
            // recentExpenses = -150 (absolute: 150)
            // Formula: (150 - 100) / 100 * 100 = 50%
            BigDecimal expectedTrend = new BigDecimal("50.0000");
            assertEquals(expectedTrend, scalarResult.value(),
                    "Should ignore income transactions and only count expenses");
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
            LocalDate today = LocalDate.now();
            Transaction transaction = createMockExpenseTransaction(
                    new BigDecimal("100.00"),
                    today.minusDays(10),
                    TransactionCategoryEnum.ALIMENTATION
            );

            AnalysisMetric result = strategy.analyze(List.of(transaction));

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
            LocalDate today = LocalDate.now();
            Transaction transaction = createMockExpenseTransaction(
                    new BigDecimal("100.00"),
                    today.minusDays(10),
                    TransactionCategoryEnum.ALIMENTATION
            );

            AnalysisMetric result = strategy.analyze(List.of(transaction));

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
            LocalDate today = LocalDate.now();
            Transaction transaction = createMockExpenseTransaction(
                    new BigDecimal("100.00"),
                    today.minusDays(10),
                    TransactionCategoryEnum.ALIMENTATION
            );

            AnalysisMetric result = strategy.analyze(List.of(transaction));

            ScalarMetric scalarResult = (ScalarMetric) result;
            assertNotNull(scalarResult.value(),
                    "ScalarMetric value should not be null");
        }
    }

    // ========================================
    // Helper Methods
    // ========================================

    /**
     * Creates a mock expense transaction with the given amount, date, and category.
     *
     * @param amount   the transaction amount (positive, will be stored as negative)
     * @param date     the transaction date
     * @param category the transaction category
     * @return a mocked expense Transaction
     */
    private Transaction createMockExpenseTransaction(BigDecimal amount, LocalDate date,
                                                    TransactionCategoryEnum category) {
        Transaction transaction = mock(Transaction.class);
        when(transaction.isExpense()).thenReturn(true);
        when(transaction.isIncome()).thenReturn(false);
        when(transaction.getAmount()).thenReturn(TransactionAmount.expense(amount));
        when(transaction.getTransactionDate()).thenReturn(TransactionDate.of(date));
        when(transaction.getCategory()).thenReturn(Category.of(category));
        return transaction;
    }

    /**
     * Creates a mock income transaction with the given amount and date.
     *
     * @param amount the transaction amount (positive)
     * @param date   the transaction date
     * @return a mocked income Transaction
     */
    private Transaction createMockIncomeTransaction(BigDecimal amount, LocalDate date) {
        Transaction transaction = mock(Transaction.class);
        when(transaction.isExpense()).thenReturn(false);
        when(transaction.isIncome()).thenReturn(true);
        when(transaction.getAmount()).thenReturn(TransactionAmount.income(amount));
        when(transaction.getTransactionDate()).thenReturn(TransactionDate.of(date));
        return transaction;
    }
}

