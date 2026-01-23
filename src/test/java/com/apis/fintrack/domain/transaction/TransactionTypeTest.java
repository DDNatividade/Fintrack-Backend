package com.apis.fintrack.domain.transaction;

import com.apis.fintrack.domain.transaction.model.TransactionType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link TransactionType} enum.
 *
 * <p>Tests cover enum values, query methods (isPositive, isIncome, isExpense),
 * and descriptions following specification-based testing approaches.</p>
 */
@DisplayName("TransactionType Enum Tests")
class TransactionTypeTest {

    // ==================== ENUM VALUES - HIGH PRIORITY ====================

    @Nested
    @DisplayName("Enum Values")
    class EnumValuesTests {

        /**
         * Verifies that INCOME and EXPENSE exist.
         */
        @Test
        @DisplayName("Should have INCOME and EXPENSE values")
        void enumValues_shouldContainIncomeAndExpense() {
            assertNotNull(TransactionType.valueOf("INCOME"));
            assertNotNull(TransactionType.valueOf("EXPENSE"));
            assertEquals(2, TransactionType.values().length);
        }

        /**
         * Verifies that each type has a description.
         */
        @Test
        @DisplayName("Each type should have a description")
        void enumValues_shouldHaveDescriptions() {
            for (TransactionType type : TransactionType.values()) {
                assertNotNull(type.getDescription());
                assertFalse(type.getDescription().isEmpty());
            }
        }
    }

    // ==================== QUERY METHODS - HIGH PRIORITY ====================

    @Nested
    @DisplayName("Query Methods")
    class QueryMethodsTests {

        /**
         * Verifies isPositive() returns true for INCOME.
         */
        @Test
        @DisplayName("INCOME.isPositive() should return true")
        void isPositive_income_shouldReturnTrue() {
            assertTrue(TransactionType.INCOME.isPositive());
        }

        /**
         * Verifies isPositive() returns false for EXPENSE.
         */
        @Test
        @DisplayName("EXPENSE.isPositive() should return false")
        void isPositive_expense_shouldReturnFalse() {
            assertFalse(TransactionType.EXPENSE.isPositive());
        }

        /**
         * Verifies isIncome() returns true for INCOME.
         */
        @Test
        @DisplayName("INCOME.isIncome() should return true")
        void isIncome_income_shouldReturnTrue() {
            assertTrue(TransactionType.INCOME.isIncome());
        }

        /**
         * Verifies isIncome() returns false for EXPENSE.
         */
        @Test
        @DisplayName("EXPENSE.isIncome() should return false")
        void isIncome_expense_shouldReturnFalse() {
            assertFalse(TransactionType.EXPENSE.isIncome());
        }

        /**
         * Verifies isExpense() returns true for EXPENSE.
         */
        @Test
        @DisplayName("EXPENSE.isExpense() should return true")
        void isExpense_expense_shouldReturnTrue() {
            assertTrue(TransactionType.EXPENSE.isExpense());
        }

        /**
         * Verifies isExpense() returns false for INCOME.
         */
        @Test
        @DisplayName("INCOME.isExpense() should return false")
        void isExpense_income_shouldReturnFalse() {
            assertFalse(TransactionType.INCOME.isExpense());
        }
    }

    // ==================== CONSISTENCY - MEDIUM PRIORITY ====================

    @Nested
    @DisplayName("Consistency Tests")
    class ConsistencyTests {

        /**
         * Verifies that isPositive and isIncome are consistent for INCOME.
         */
        @Test
        @DisplayName("INCOME: isPositive and isIncome should be consistent")
        void income_isPositiveAndIsIncome_shouldBeConsistent() {
            assertEquals(TransactionType.INCOME.isPositive(), TransactionType.INCOME.isIncome());
        }

        /**
         * Verifies that isPositive is opposite of isExpense for EXPENSE.
         */
        @Test
        @DisplayName("EXPENSE: isPositive and isExpense should be opposite")
        void expense_isPositiveAndIsExpense_shouldBeOpposite() {
            assertNotEquals(TransactionType.EXPENSE.isPositive(), TransactionType.EXPENSE.isExpense());
        }

        /**
         * Verifies that isIncome and isExpense are mutually exclusive.
         */
        @Test
        @DisplayName("isIncome and isExpense should be mutually exclusive")
        void types_isIncomeAndIsExpense_shouldBeMutuallyExclusive() {
            for (TransactionType type : TransactionType.values()) {
                assertNotEquals(type.isIncome(), type.isExpense());
            }
        }
    }
}

