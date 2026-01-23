package com.apis.fintrack.domain.transaction;

import com.apis.fintrack.domain.transaction.model.TransactionAmount;
import com.apis.fintrack.domain.transaction.model.TransactionType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link TransactionAmount} value object.
 *
 * <p>Tests cover factory methods for income/expense, validation rules,
 * and sign handling following specification-based testing approaches.</p>
 */
@DisplayName("TransactionAmount Value Object Tests")
class TransactionAmountTest {

    // ==================== FACTORY METHOD: income() - HIGH PRIORITY ====================

    @Nested
    @DisplayName("Factory Method: income()")
    class IncomeFactoryTests {

        /**
         * Verifies that income() creates a TransactionAmount with positive value.
         */
        @Test
        @DisplayName("Should create TransactionAmount with positive value")
        void income_withPositiveAmount_shouldReturnPositiveValue() {
            BigDecimal amount = new BigDecimal("100.00");

            TransactionAmount result = TransactionAmount.income(amount);

            assertNotNull(result);
            assertEquals(new BigDecimal("100.00"), result.getValue());
            assertTrue(result.getValue().compareTo(BigDecimal.ZERO) > 0);
        }

        /**
         * Verifies that income() rounds to 2 decimal places.
         */
        @Test
        @DisplayName("Should round to 2 decimal places")
        void income_withManyDecimals_shouldRoundTo2Decimals() {
            BigDecimal amount = new BigDecimal("100.456");

            TransactionAmount result = TransactionAmount.income(amount);

            assertEquals(new BigDecimal("100.46"), result.getValue());
        }

        /**
         * Verifies that null amount throws IllegalArgumentException.
         */
        @Test
        @DisplayName("Should throw IllegalArgumentException when amount is null")
        void income_withNullAmount_shouldThrowIllegalArgumentException() {
            assertThrows(
                    IllegalArgumentException.class,
                    () -> TransactionAmount.income(null)
            );
        }

        /**
         * Verifies that zero amount throws IllegalArgumentException.
         */
        @Test
        @DisplayName("Should throw IllegalArgumentException when amount is zero")
        void income_withZeroAmount_shouldThrowIllegalArgumentException() {
            assertThrows(
                    IllegalArgumentException.class,
                    () -> TransactionAmount.income(BigDecimal.ZERO)
            );
        }

        /**
         * Verifies that negative amount throws IllegalArgumentException.
         */
        @Test
        @DisplayName("Should throw IllegalArgumentException when amount is negative")
        void income_withNegativeAmount_shouldThrowIllegalArgumentException() {
            assertThrows(
                    IllegalArgumentException.class,
                    () -> TransactionAmount.income(new BigDecimal("-100.00"))
            );
        }

        /**
         * Verifies that small positive amount is accepted.
         */
        @Test
        @DisplayName("Should accept small positive amount (0.01)")
        void income_withSmallAmount_shouldReturnTransactionAmount() {
            TransactionAmount result = TransactionAmount.income(new BigDecimal("0.01"));

            assertEquals(new BigDecimal("0.01"), result.getValue());
        }
    }

    // ==================== FACTORY METHOD: expense() - HIGH PRIORITY ====================

    @Nested
    @DisplayName("Factory Method: expense()")
    class ExpenseFactoryTests {

        /**
         * Verifies that expense() creates a TransactionAmount with negative value.
         */
        @Test
        @DisplayName("Should create TransactionAmount with negative value")
        void expense_withPositiveAmount_shouldReturnNegativeValue() {
            BigDecimal amount = new BigDecimal("50.00");

            TransactionAmount result = TransactionAmount.expense(amount);

            assertNotNull(result);
            assertEquals(new BigDecimal("-50.00"), result.getValue());
            assertTrue(result.getValue().compareTo(BigDecimal.ZERO) < 0);
        }

        /**
         * Verifies that expense() converts already negative input to negative.
         */
        @Test
        @DisplayName("Should convert negative input to negative")
        void expense_withNegativeInput_shouldReturnNegativeValue() {
            BigDecimal amount = new BigDecimal("-50.00");

            TransactionAmount result = TransactionAmount.expense(amount);

            assertEquals(new BigDecimal("-50.00"), result.getValue());
        }

        /**
         * Verifies that expense() rounds to 2 decimal places.
         */
        @Test
        @DisplayName("Should round to 2 decimal places")
        void expense_withManyDecimals_shouldRoundTo2Decimals() {
            BigDecimal amount = new BigDecimal("75.999");

            TransactionAmount result = TransactionAmount.expense(amount);

            assertEquals(new BigDecimal("-76.00"), result.getValue());
        }

        /**
         * Verifies that null amount throws IllegalArgumentException.
         */
        @Test
        @DisplayName("Should throw IllegalArgumentException when amount is null")
        void expense_withNullAmount_shouldThrowIllegalArgumentException() {
            assertThrows(
                    IllegalArgumentException.class,
                    () -> TransactionAmount.expense(null)
            );
        }

        /**
         * Verifies that zero amount throws IllegalArgumentException.
         */
        @Test
        @DisplayName("Should throw IllegalArgumentException when amount is zero")
        void expense_withZeroAmount_shouldThrowIllegalArgumentException() {
            assertThrows(
                    IllegalArgumentException.class,
                    () -> TransactionAmount.expense(BigDecimal.ZERO)
            );
        }
    }

    // ==================== FACTORY METHOD: of(BigDecimal, TransactionType) - HIGH PRIORITY ====================

    @Nested
    @DisplayName("Factory Method: of(BigDecimal, TransactionType)")
    class OfWithTypeTests {

        /**
         * Verifies that of() with INCOME type returns positive value.
         */
        @Test
        @DisplayName("Should return positive value for INCOME type")
        void of_withIncomeType_shouldReturnPositiveValue() {
            BigDecimal amount = new BigDecimal("100.00");

            TransactionAmount result = TransactionAmount.of(amount, TransactionType.INCOME);

            assertEquals(new BigDecimal("100.00"), result.getValue());
        }

        /**
         * Verifies that of() with EXPENSE type returns negative value.
         */
        @Test
        @DisplayName("Should return negative value for EXPENSE type")
        void of_withExpenseType_shouldReturnNegativeValue() {
            BigDecimal amount = new BigDecimal("100.00");

            TransactionAmount result = TransactionAmount.of(amount, TransactionType.EXPENSE);

            assertEquals(new BigDecimal("-100.00"), result.getValue());
        }
    }

    // ==================== FACTORY METHOD: of(BigDecimal, boolean) - MEDIUM PRIORITY ====================

    @Nested
    @DisplayName("Factory Method: of(BigDecimal, boolean)")
    class OfWithBooleanTests {

        /**
         * Verifies that of() with isIncome=true returns positive value.
         */
        @Test
        @DisplayName("Should return positive value when isIncome is true")
        void of_withIsIncomeTrue_shouldReturnPositiveValue() {
            BigDecimal amount = new BigDecimal("100.00");

            TransactionAmount result = TransactionAmount.of(amount, true);

            assertEquals(new BigDecimal("100.00"), result.getValue());
        }

        /**
         * Verifies that of() with isIncome=false returns negative value.
         */
        @Test
        @DisplayName("Should return negative value when isIncome is false")
        void of_withIsIncomeFalse_shouldReturnNegativeValue() {
            BigDecimal amount = new BigDecimal("100.00");

            TransactionAmount result = TransactionAmount.of(amount, false);

            assertEquals(new BigDecimal("-100.00"), result.getValue());
        }
    }

    // ==================== FACTORY METHOD: fromStorage() - HIGH PRIORITY ====================

    @Nested
    @DisplayName("Factory Method: fromStorage()")
    class FromStorageTests {

        /**
         * Verifies that fromStorage() creates TransactionAmount preserving sign.
         */
        @Test
        @DisplayName("Should preserve stored value with sign")
        void fromStorage_withStoredValue_shouldPreserveSign() {
            BigDecimal storedPositive = new BigDecimal("100.00");
            BigDecimal storedNegative = new BigDecimal("-50.00");

            TransactionAmount positiveResult = TransactionAmount.fromStorage(storedPositive);
            TransactionAmount negativeResult = TransactionAmount.fromStorage(storedNegative);

            assertEquals(new BigDecimal("100.00"), positiveResult.getValue());
            assertEquals(new BigDecimal("-50.00"), negativeResult.getValue());
        }

        /**
         * Verifies that fromStorage() throws for null.
         */
        @Test
        @DisplayName("Should throw IllegalArgumentException when stored value is null")
        void fromStorage_withNullValue_shouldThrowIllegalArgumentException() {
            assertThrows(
                    IllegalArgumentException.class,
                    () -> TransactionAmount.fromStorage(null)
            );
        }
    }

    // ==================== QUERY METHODS - MEDIUM PRIORITY ====================

    @Nested
    @DisplayName("Query Methods")
    class QueryMethodsTests {

        /**
         * Verifies isIncome() returns true for positive amounts.
         */
        @Test
        @DisplayName("isIncome() should return true for positive amounts")
        void isIncome_positiveAmount_shouldReturnTrue() {
            TransactionAmount income = TransactionAmount.income(new BigDecimal("100.00"));

            assertTrue(income.isIncome());
        }

        /**
         * Verifies isIncome() returns false for negative amounts.
         */
        @Test
        @DisplayName("isIncome() should return false for negative amounts")
        void isIncome_negativeAmount_shouldReturnFalse() {
            TransactionAmount expense = TransactionAmount.expense(new BigDecimal("100.00"));

            assertFalse(expense.isIncome());
        }

        /**
         * Verifies isExpense() returns true for negative amounts.
         */
        @Test
        @DisplayName("isExpense() should return true for negative amounts")
        void isExpense_negativeAmount_shouldReturnTrue() {
            TransactionAmount expense = TransactionAmount.expense(new BigDecimal("100.00"));

            assertTrue(expense.isExpense());
        }

        /**
         * Verifies isExpense() returns false for positive amounts.
         */
        @Test
        @DisplayName("isExpense() should return false for positive amounts")
        void isExpense_positiveAmount_shouldReturnFalse() {
            TransactionAmount income = TransactionAmount.income(new BigDecimal("100.00"));

            assertFalse(income.isExpense());
        }

        /**
         * Verifies getAbsoluteValue() returns positive value.
         */
        @Test
        @DisplayName("getAbsoluteValue() should return positive value")
        void getAbsoluteValue_negativeAmount_shouldReturnPositive() {
            TransactionAmount expense = TransactionAmount.expense(new BigDecimal("100.00"));

            assertEquals(new BigDecimal("100.00"), expense.getAbsoluteValue());
        }
    }

    // ==================== EQUALITY CONTRACT - MEDIUM PRIORITY ====================

    @Nested
    @DisplayName("Equality Contract: equals() and hashCode()")
    class EqualityTests {

        /**
         * Verifies that two TransactionAmount with same value are equal.
         */
        @Test
        @DisplayName("equals() should return true for same value")
        void equals_withSameValue_shouldReturnTrue() {
            TransactionAmount amount1 = TransactionAmount.income(new BigDecimal("100.00"));
            TransactionAmount amount2 = TransactionAmount.income(new BigDecimal("100.00"));

            assertEquals(amount1, amount2);
        }

        /**
         * Verifies that TransactionAmount with different values are not equal.
         */
        @Test
        @DisplayName("equals() should return false for different values")
        void equals_withDifferentValue_shouldReturnFalse() {
            TransactionAmount amount1 = TransactionAmount.income(new BigDecimal("100.00"));
            TransactionAmount amount2 = TransactionAmount.income(new BigDecimal("50.00"));

            assertNotEquals(amount1, amount2);
        }

        /**
         * Verifies that income and expense with same absolute value are not equal.
         */
        @Test
        @DisplayName("Income and expense with same absolute value should not be equal")
        void equals_incomeAndExpense_shouldReturnFalse() {
            TransactionAmount income = TransactionAmount.income(new BigDecimal("100.00"));
            TransactionAmount expense = TransactionAmount.expense(new BigDecimal("100.00"));

            assertNotEquals(income, expense);
        }

        /**
         * Verifies reflexivity.
         */
        @Test
        @DisplayName("equals() should be reflexive")
        void equals_reflexive_shouldReturnTrue() {
            TransactionAmount amount = TransactionAmount.income(new BigDecimal("100.00"));

            assertEquals(amount, amount);
        }

        /**
         * Verifies that equal objects have the same hashCode.
         */
        @Test
        @DisplayName("hashCode() should be same for equal objects")
        void hashCode_equalObjects_shouldReturnSameHash() {
            TransactionAmount amount1 = TransactionAmount.income(new BigDecimal("100.00"));
            TransactionAmount amount2 = TransactionAmount.income(new BigDecimal("100.00"));

            assertEquals(amount1.hashCode(), amount2.hashCode());
        }
    }

    // ==================== EDGE CASES - LOW PRIORITY ====================

    @Nested
    @DisplayName("Edge Cases")
    class EdgeCaseTests {

        /**
         * Verifies handling of very large amounts.
         */
        @Test
        @DisplayName("Should handle very large amounts")
        void income_veryLargeAmount_shouldCreateTransactionAmount() {
            BigDecimal largeAmount = new BigDecimal("999999999999.99");

            TransactionAmount result = TransactionAmount.income(largeAmount);

            assertEquals(largeAmount, result.getValue());
        }

        /**
         * Verifies handling of very small amounts.
         */
        @Test
        @DisplayName("Should handle very small amounts (0.01)")
        void expense_verySmallAmount_shouldCreateTransactionAmount() {
            TransactionAmount result = TransactionAmount.expense(new BigDecimal("0.01"));

            assertEquals(new BigDecimal("-0.01"), result.getValue());
        }
    }
}

