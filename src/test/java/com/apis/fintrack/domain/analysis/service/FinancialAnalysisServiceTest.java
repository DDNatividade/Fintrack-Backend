package com.apis.fintrack.domain.analysis.service;

import com.apis.fintrack.domain.analysis.model.kpi.AnalysisResult;
import com.apis.fintrack.domain.analysis.model.kpi.AnalysisType;
import com.apis.fintrack.domain.analysis.model.metric.MetricUnit;
import com.apis.fintrack.domain.analysis.model.metric.ScalarMetric;
import com.apis.fintrack.domain.analysis.strategy.FinancialAnalysisStrategy;
import com.apis.fintrack.domain.transaction.model.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for FinancialAnalysisService.
 *
 * Tests follow specification-based testing approach, covering:
 * - Constructor validation (null, empty, duplicates, null supports)
 * - Defensive copying
 * - analyze() method with valid inputs
 * - analyze() method with invalid inputs (null, empty, missing strategy)
 * - Edge cases and error handling
 */
@DisplayName("FinancialAnalysisService Tests")
class FinancialAnalysisServiceTest {

    private FinancialAnalysisStrategy mockStrategy1;
    private FinancialAnalysisStrategy mockStrategy2;
    private Transaction mockTransaction;
    private List<Transaction> validTransactions;

    @BeforeEach
    void setUp() {
        mockStrategy1 = mock(FinancialAnalysisStrategy.class);
        mockStrategy2 = mock(FinancialAnalysisStrategy.class);
        mockTransaction = mock(Transaction.class);
        validTransactions = List.of(mockTransaction);

        // Default behavior for mockStrategy1
        when(mockStrategy1.supports()).thenReturn(AnalysisType.SAVINGS_RATE);
        when(mockStrategy1.analyze(any())).thenReturn(
                new ScalarMetric(new BigDecimal("50.00"), MetricUnit.PERCENT)
        );

        // Default behavior for mockStrategy2
        when(mockStrategy2.supports()).thenReturn(AnalysisType.MONTHLY_AVERAGE_EXPENSES);
        when(mockStrategy2.analyze(any())).thenReturn(
                new ScalarMetric(new BigDecimal("1000.00"), MetricUnit.CURRENCY)
        );
    }

    // ========================================
    // Constructor Tests - Valid Inputs
    // ========================================

    @Nested
    @DisplayName("Constructor - Valid Inputs")
    class ConstructorValidTests {

        @Test
        @DisplayName("Constructor with single strategy succeeds")
        void constructor_singleStrategy_succeeds() {
            assertDoesNotThrow(() -> new FinancialAnalysisService(List.of(mockStrategy1)));
        }

        @Test
        @DisplayName("Constructor with multiple strategies succeeds")
        void constructor_multipleStrategies_succeeds() {
            assertDoesNotThrow(() -> new FinancialAnalysisService(List.of(mockStrategy1, mockStrategy2)));
        }

        @Test
        @DisplayName("Constructor with multiple strategies makes all accessible")
        void constructor_multipleStrategiesDifferentTypes_allAccessible() {
            FinancialAnalysisService service = new FinancialAnalysisService(
                    List.of(mockStrategy1, mockStrategy2)
            );

            // Both strategies should be accessible
            assertDoesNotThrow(() -> service.analyze(AnalysisType.SAVINGS_RATE, validTransactions));
            assertDoesNotThrow(() -> service.analyze(AnalysisType.MONTHLY_AVERAGE_EXPENSES, validTransactions));
        }
    }

    // ========================================
    // Constructor Tests - Invalid Inputs
    // ========================================

    @Nested
    @DisplayName("Constructor - Invalid Inputs")
    class ConstructorInvalidTests {

        @Test
        @DisplayName("Constructor with null strategies throws NullPointerException")
        void constructor_nullStrategies_throwsNullPointer() {
            NullPointerException exception = assertThrows(
                    NullPointerException.class,
                    () -> new FinancialAnalysisService(null)
            );
            assertEquals("Strategies list must not be null", exception.getMessage());
        }

        @Test
        @DisplayName("Constructor with empty strategies throws IllegalArgumentException")
        void constructor_emptyStrategies_throwsIllegalArgument() {
            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> new FinancialAnalysisService(Collections.emptyList())
            );
            assertEquals("At least one strategy must be provided", exception.getMessage());
        }

        @Test
        @DisplayName("Constructor with duplicate supports throws IllegalStateException")
        void constructor_duplicateSupports_throwsIllegalState() {
            FinancialAnalysisStrategy mockStrategy3 = mock(FinancialAnalysisStrategy.class);
            when(mockStrategy3.supports()).thenReturn(AnalysisType.SAVINGS_RATE); // Same as mockStrategy1

            assertThrows(
                    IllegalStateException.class,
                    () -> new FinancialAnalysisService(List.of(mockStrategy1, mockStrategy3)),
                    "Should throw IllegalStateException when two strategies have the same supports"
            );
        }

        @Test
        @DisplayName("Constructor with strategy returning null supports throws NullPointerException")
        void constructor_strategyReturnsNullSupports_throwsNullPointer() {
            FinancialAnalysisStrategy mockStrategyWithNullSupports = mock(FinancialAnalysisStrategy.class);
            when(mockStrategyWithNullSupports.supports()).thenReturn(null);

            assertThrows(
                    NullPointerException.class,
                    () -> new FinancialAnalysisService(List.of(mockStrategyWithNullSupports)),
                    "Should throw NullPointerException when strategy returns null supports"
            );
        }
    }

    // ========================================
    // Constructor - Defensive Copy Tests
    // ========================================

    @Nested
    @DisplayName("Constructor - Defensive Copy")
    class ConstructorDefensiveCopyTests {

        @Test
        @DisplayName("Modifying input list after construction does not affect service")
        void constructor_defensiveCopy_inputListModificationDoesNotAffectService() {
            List<FinancialAnalysisStrategy> mutableList = new ArrayList<>();
            mutableList.add(mockStrategy1);

            FinancialAnalysisService service = new FinancialAnalysisService(mutableList);

            // Modify original list
            mutableList.add(mockStrategy2);
            mutableList.clear();

            // Service should still work with the original strategy
            assertDoesNotThrow(() -> service.analyze(AnalysisType.SAVINGS_RATE, validTransactions));
        }
    }

    // ========================================
    // analyze() - Valid Inputs
    // ========================================

    @Nested
    @DisplayName("analyze() - Valid Inputs")
    class AnalyzeValidTests {

        private FinancialAnalysisService service;

        @BeforeEach
        void setUp() {
            service = new FinancialAnalysisService(List.of(mockStrategy1, mockStrategy2));
        }

        @Test
        @DisplayName("analyze delegates to correct strategy and returns expected result")
        void analyze_delegatesToCorrectStrategy_and_returnsExpectedResult() {
            AnalysisResult result = service.analyze(AnalysisType.SAVINGS_RATE, validTransactions);

            // Verify delegation
            verify(mockStrategy1, times(1)).analyze(validTransactions);
            verify(mockStrategy2, never()).analyze(any());

            // Verify result
            assertNotNull(result);
            assertEquals(AnalysisType.SAVINGS_RATE, result.getType());
            assertNotNull(result.getValue());
        }

        @Test
        @DisplayName("analyze with single transaction succeeds")
        void analyze_singleTransaction_succeeds() {
            List<Transaction> singleTransaction = List.of(mockTransaction);

            AnalysisResult result = service.analyze(AnalysisType.SAVINGS_RATE, singleTransaction);

            assertNotNull(result);
            verify(mockStrategy1, times(1)).analyze(singleTransaction);
        }

        @Test
        @DisplayName("analyze with multiple transactions succeeds")
        void analyze_multipleTransactions_succeeds() {
            List<Transaction> multipleTransactions = Arrays.asList(
                    mockTransaction,
                    mock(Transaction.class),
                    mock(Transaction.class)
            );

            AnalysisResult result = service.analyze(AnalysisType.SAVINGS_RATE, multipleTransactions);

            assertNotNull(result);
            verify(mockStrategy1, times(1)).analyze(multipleTransactions);
        }

        @Test
        @DisplayName("analyze with different analysis type uses different strategy")
        void analyze_differentAnalysisType_usesDifferentStrategy() {
            service.analyze(AnalysisType.MONTHLY_AVERAGE_EXPENSES, validTransactions);

            verify(mockStrategy1, never()).analyze(any());
            verify(mockStrategy2, times(1)).analyze(validTransactions);
        }
    }

    // ========================================
    // analyze() - Invalid Inputs
    // ========================================

    @Nested
    @DisplayName("analyze() - Invalid Inputs")
    class AnalyzeInvalidTests {

        private FinancialAnalysisService service;

        @BeforeEach
        void setUp() {
            service = new FinancialAnalysisService(List.of(mockStrategy1));
        }

        @Test
        @DisplayName("analyze with null type throws NullPointerException")
        void analyze_nullType_throwsNullPointer() {
            NullPointerException exception = assertThrows(
                    NullPointerException.class,
                    () -> service.analyze(null, validTransactions)
            );
            assertEquals("AnalysisType must not be null", exception.getMessage());
        }

        @Test
        @DisplayName("analyze with null transactions throws NullPointerException")
        void analyze_nullTransactions_throwsNullPointer() {
            NullPointerException exception = assertThrows(
                    NullPointerException.class,
                    () -> service.analyze(AnalysisType.SAVINGS_RATE, null)
            );
            assertEquals("Transactions must not be null", exception.getMessage());
        }

        @Test
        @DisplayName("analyze with empty transactions throws IllegalArgumentException")
        void analyze_emptyTransactions_throwsIllegalArgument() {
            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> service.analyze(AnalysisType.SAVINGS_RATE, Collections.emptyList())
            );
            assertEquals("Transactions list must not be empty", exception.getMessage());
        }

        @Test
        @DisplayName("analyze with missing strategy leads to NullPointerException")
        void analyze_missingStrategy_leadsToNullPointer() {
            // Service only has mockStrategy1 (SAVINGS_RATE)
            // Try to analyze with MONTHLY_AVERAGE_EXPENSES which is not registered
            assertThrows(
                    NullPointerException.class,
                    () -> service.analyze(AnalysisType.MONTHLY_AVERAGE_EXPENSES, validTransactions),
                    "Should throw NullPointerException when strategy for the given type is not found"
            );
        }
    }

    // ========================================
    // analyze() - Edge Cases
    // ========================================

    @Nested
    @DisplayName("analyze() - Edge Cases")
    class AnalyzeEdgeCasesTests {

        private FinancialAnalysisService service;

        @BeforeEach
        void setUp() {
            service = new FinancialAnalysisService(List.of(mockStrategy1));
        }

        @Test
        @DisplayName("analyze when strategy returns null metric leads to NullPointerException")
        void analyze_strategyReturnsNullMetric_handlesOrFailsExplicitly() {
            when(mockStrategy1.analyze(any())).thenReturn(null);

            assertThrows(
                    NullPointerException.class,
                    () -> service.analyze(AnalysisType.SAVINGS_RATE, validTransactions),
                    "Should fail when strategy returns null metric"
            );
        }

        @Test
        @DisplayName("analyze with transactions containing null elements propagates or validates")
        void analyze_transactionsContainingNullElements_propagatesOrValidates() {
            List<Transaction> listWithNull = Arrays.asList(mockTransaction, null, mockTransaction);

            // This test verifies that the service doesn't crash immediately
            // The actual behavior depends on the strategy implementation
            // Here we just verify that the list is passed to the strategy
            service.analyze(AnalysisType.SAVINGS_RATE, listWithNull);

            verify(mockStrategy1, times(1)).analyze(listWithNull);
        }

        @Test
        @DisplayName("analyze with strategy throwing exception propagates exception")
        void analyze_strategyThrowsException_propagatesException() {
            when(mockStrategy1.analyze(any())).thenThrow(new RuntimeException("Strategy failure"));

            assertThrows(
                    RuntimeException.class,
                    () -> service.analyze(AnalysisType.SAVINGS_RATE, validTransactions)
            );
        }
    }

    // ========================================
    // Concurrency Tests (Optional)
    // ========================================

    @Nested
    @DisplayName("Concurrency - Edge Cases")
    class ConcurrencyTests {

        @Test
        @DisplayName("Concurrent modification of input list does not corrupt internal state")
        void constructor_concurrentModificationOfInputList_doesNotCorruptInternalState() {
            List<FinancialAnalysisStrategy> mutableList = new ArrayList<>();
            mutableList.add(mockStrategy1);

            // Create service while list is potentially being modified
            FinancialAnalysisService service = new FinancialAnalysisService(mutableList);

            // Modify list (simulating concurrent access)
            mutableList.add(mockStrategy2);

            // Service should still function correctly with original strategies
            assertDoesNotThrow(() -> service.analyze(AnalysisType.SAVINGS_RATE, validTransactions));
        }
    }

    // ========================================
    // Integration-like Tests
    // ========================================

    @Nested
    @DisplayName("Integration-like Tests")
    class IntegrationTests {

        @Test
        @DisplayName("Complete flow: construct service, analyze, verify delegation")
        void completeFlow_constructAnalyzeVerify() {
            // Arrange
            FinancialAnalysisService service = new FinancialAnalysisService(
                    List.of(mockStrategy1, mockStrategy2)
            );

            // Act
            AnalysisResult result = service.analyze(AnalysisType.SAVINGS_RATE, validTransactions);

            // Assert
            assertNotNull(result);
            assertEquals(AnalysisType.SAVINGS_RATE, result.getType());
            verify(mockStrategy1, times(1)).analyze(validTransactions);
            verify(mockStrategy2, never()).analyze(any());
        }
    }
}


