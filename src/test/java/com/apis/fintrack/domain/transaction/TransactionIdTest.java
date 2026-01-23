package com.apis.fintrack.domain.transaction;

import com.apis.fintrack.domain.transaction.model.TransactionId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link TransactionId} value object.
 *
 * <p>Tests cover factory methods, validation rules, equality contract,
 * and edge cases following specification-based testing approaches.</p>
 */
@DisplayName("TransactionId Value Object Tests")
class TransactionIdTest {

    // ==================== FACTORY METHOD: of() - HIGH PRIORITY ====================

    @Nested
    @DisplayName("Factory Method: of()")
    class OfFactoryTests {

        /**
         * Verifies that the factory method creates a valid TransactionId
         * when provided with a positive Long value.
         */
        @Test
        @DisplayName("Should return TransactionId when given a valid positive value")
        void of_withValidPositiveValue_shouldReturnTransactionId() {
            Long validValue = 100L;

            TransactionId result = TransactionId.of(validValue);

            assertNotNull(result);
            assertEquals(validValue, result.getValue());
            assertFalse(result.isEmpty());
        }

        /**
         * Verifies that the factory method accepts null (for new transactions).
         */
        @Test
        @DisplayName("Should accept null value")
        void of_withNullValue_shouldReturnTransactionId() {
            TransactionId result = TransactionId.of(null);

            assertNotNull(result);
            assertNull(result.getValue());
            assertTrue(result.isEmpty());
        }

        /**
         * Verifies that the factory method accepts zero.
         */
        @Test
        @DisplayName("Should accept zero value")
        void of_withZeroValue_shouldReturnTransactionId() {
            TransactionId result = TransactionId.of(0L);

            assertEquals(0L, result.getValue());
        }

        /**
         * Verifies that negative values throw IllegalArgumentException.
         */
        @Test
        @DisplayName("Should throw IllegalArgumentException when value is negative")
        void of_withNegativeValue_shouldThrowIllegalArgumentException() {
            assertThrows(
                    IllegalArgumentException.class,
                    () -> TransactionId.of(-5L)
            );
        }

        /**
         * Verifies that the minimum valid positive value (1) is accepted.
         */
        @Test
        @DisplayName("Should accept value of 1")
        void of_withValueOne_shouldReturnTransactionId() {
            TransactionId result = TransactionId.of(1L);

            assertEquals(1L, result.getValue());
        }

        /**
         * Verifies that Long.MAX_VALUE is accepted.
         */
        @Test
        @DisplayName("Should accept Long.MAX_VALUE")
        void of_withMaxLongValue_shouldReturnTransactionId() {
            TransactionId result = TransactionId.of(Long.MAX_VALUE);

            assertEquals(Long.MAX_VALUE, result.getValue());
        }
    }

    // ==================== FACTORY METHOD: empty() - HIGH PRIORITY ====================

    @Nested
    @DisplayName("Factory Method: empty()")
    class EmptyFactoryTests {

        /**
         * Verifies that empty() creates a TransactionId with null internal value.
         */
        @Test
        @DisplayName("Should return TransactionId with null value")
        void empty_shouldReturnTransactionIdWithNullValue() {
            TransactionId result = TransactionId.empty();

            assertNotNull(result);
            assertNull(result.getValue());
            assertTrue(result.isEmpty());
        }

        /**
         * Verifies that isEmpty() returns true for an empty instance.
         */
        @Test
        @DisplayName("isEmpty() should return true for empty instance")
        void isEmpty_whenEmpty_shouldReturnTrue() {
            TransactionId emptyId = TransactionId.empty();

            assertTrue(emptyId.isEmpty());
        }

        /**
         * Verifies that isEmpty() returns false for a valid instance.
         */
        @Test
        @DisplayName("isEmpty() should return false for valid instance")
        void isEmpty_whenNotEmpty_shouldReturnFalse() {
            TransactionId validId = TransactionId.of(42L);

            assertFalse(validId.isEmpty());
        }
    }

    // ==================== EQUALITY CONTRACT - MEDIUM PRIORITY ====================

    @Nested
    @DisplayName("Equality Contract: equals() and hashCode()")
    class EqualityTests {

        /**
         * Verifies that two TransactionId instances with the same value are equal.
         */
        @Test
        @DisplayName("equals() should return true for same value")
        void equals_withSameValue_shouldReturnTrue() {
            TransactionId id1 = TransactionId.of(50L);
            TransactionId id2 = TransactionId.of(50L);

            assertEquals(id1, id2);
        }

        /**
         * Verifies that two TransactionId instances with different values are not equal.
         */
        @Test
        @DisplayName("equals() should return false for different values")
        void equals_withDifferentValue_shouldReturnFalse() {
            TransactionId id1 = TransactionId.of(1L);
            TransactionId id2 = TransactionId.of(2L);

            assertNotEquals(id1, id2);
        }

        /**
         * Verifies that equals() returns false when compared with null.
         */
        @Test
        @DisplayName("equals() should return false when compared with null")
        void equals_withNull_shouldReturnFalse() {
            TransactionId transactionId = TransactionId.of(10L);

            assertNotEquals(null, transactionId);
        }

        /**
         * Verifies reflexivity.
         */
        @Test
        @DisplayName("equals() should be reflexive")
        void equals_reflexive_shouldReturnTrue() {
            TransactionId transactionId = TransactionId.of(77L);

            assertEquals(transactionId, transactionId);
        }

        /**
         * Verifies that two empty instances are equal.
         */
        @Test
        @DisplayName("Two empty instances should be equal")
        void equals_twoEmptyInstances_shouldReturnTrue() {
            TransactionId empty1 = TransactionId.empty();
            TransactionId empty2 = TransactionId.empty();

            assertEquals(empty1, empty2);
        }

        /**
         * Verifies that equal objects have the same hashCode.
         */
        @Test
        @DisplayName("hashCode() should be same for equal objects")
        void hashCode_withSameValue_shouldReturnSameHash() {
            TransactionId id1 = TransactionId.of(123L);
            TransactionId id2 = TransactionId.of(123L);

            assertEquals(id1.hashCode(), id2.hashCode());
        }

        /**
         * Verifies hashCode consistency.
         */
        @Test
        @DisplayName("hashCode() should be consistent across multiple calls")
        void hashCode_multipleCalls_shouldBeConsistent() {
            TransactionId transactionId = TransactionId.of(456L);

            int hash1 = transactionId.hashCode();
            int hash2 = transactionId.hashCode();

            assertEquals(hash1, hash2);
        }
    }
}

