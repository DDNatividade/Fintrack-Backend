package com.apis.fintrack.domain.payment;

import com.apis.fintrack.domain.payment.model.PaymentId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link PaymentId} value object.
 *
 * <p>Tests cover factory methods, validation rules, equality contract,
 * and edge cases following specification-based testing approaches.</p>
 */
@DisplayName("PaymentId Value Object Tests")
class PaymentIdTest {

    // ==================== FACTORY METHOD: of() (HIGH PRIORITY) ====================

    @Nested
    @DisplayName("Factory Method: of()")
    class OfFactoryTests {

        /**
         * Verifies that the factory method creates a valid PaymentId
         * when provided with a positive Long value.
         */
        @Test
        @DisplayName("Should return PaymentId when given a valid positive value")
        void of_withValidPositiveValue_shouldReturnPaymentId() {
            Long validValue = 100L;

            PaymentId result = PaymentId.of(validValue);

            assertNotNull(result);
            assertEquals(validValue, result.getValue());
            assertFalse(result.isEmpty());
        }

        /**
         * Verifies that the factory method throws IllegalArgumentException
         * when provided with a null value. Ensures fail-fast behavior.
         */
        @Test
        @DisplayName("Should throw IllegalArgumentException when value is null")
        void of_withNullValue_shouldThrowIllegalArgumentException() {
            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> PaymentId.of(null)
            );

            assertNotNull(exception.getMessage());
            assertFalse(exception.getMessage().isEmpty());
        }

        /**
         * Verifies that the factory method throws IllegalArgumentException
         * when provided with zero. Zero is not a valid identifier.
         */
        @Test
        @DisplayName("Should throw IllegalArgumentException when value is zero")
        void of_withZeroValue_shouldThrowIllegalArgumentException() {
            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> PaymentId.of(0L)
            );

            assertNotNull(exception.getMessage());
        }

        /**
         * Verifies that the factory method throws IllegalArgumentException
         * when provided with a negative value.
         */
        @Test
        @DisplayName("Should throw IllegalArgumentException when value is negative")
        void of_withNegativeValue_shouldThrowIllegalArgumentException() {
            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> PaymentId.of(-5L)
            );

            assertNotNull(exception.getMessage());
        }

        /**
         * Verifies that the minimum valid value (1) is accepted.
         * This tests the lower boundary of valid values.
         */
        @Test
        @DisplayName("Should accept value of 1 (minimum valid)")
        void of_withValueOne_shouldReturnPaymentId() {
            PaymentId result = PaymentId.of(1L);

            assertEquals(1L, result.getValue());
            assertFalse(result.isEmpty());
        }

        /**
         * Verifies that Long.MAX_VALUE is accepted.
         * This tests the upper boundary of valid values.
         */
        @Test
        @DisplayName("Should accept Long.MAX_VALUE")
        void of_withMaxLongValue_shouldReturnPaymentId() {
            PaymentId result = PaymentId.of(Long.MAX_VALUE);

            assertEquals(Long.MAX_VALUE, result.getValue());
        }

        /**
         * Verifies that -1 (boundary just below zero) throws exception.
         */
        @Test
        @DisplayName("Should throw IllegalArgumentException when value is -1")
        void of_withMinusOne_shouldThrowIllegalArgumentException() {
            assertThrows(
                    IllegalArgumentException.class,
                    () -> PaymentId.of(-1L)
            );
        }
    }

    // ==================== FACTORY METHOD: empty() (HIGH PRIORITY) ====================

    @Nested
    @DisplayName("Factory Method: empty()")
    class EmptyFactoryTests {

        /**
         * Verifies that empty() creates a PaymentId with null internal value.
         * This represents an unpersisted entity identifier.
         */
        @Test
        @DisplayName("Should return PaymentId with null value")
        void empty_shouldReturnPaymentIdWithNullValue() {
            PaymentId result = PaymentId.empty();

            assertNotNull(result);
            assertNull(result.getValue());
        }

        /**
         * Verifies that isEmpty() returns true for an empty instance.
         */
        @Test
        @DisplayName("isEmpty() should return true for empty instance")
        void isEmpty_whenEmpty_shouldReturnTrue() {
            PaymentId emptyId = PaymentId.empty();

            assertTrue(emptyId.isEmpty());
        }

        /**
         * Verifies that isEmpty() returns false for a valid instance.
         */
        @Test
        @DisplayName("isEmpty() should return false for valid instance")
        void isEmpty_whenNotEmpty_shouldReturnFalse() {
            PaymentId validId = PaymentId.of(42L);

            assertFalse(validId.isEmpty());
        }
    }

    // ==================== GETTER METHOD (MEDIUM PRIORITY) ====================

    @Nested
    @DisplayName("Getter: getValue()")
    class GetterTests {

        /**
         * Verifies that getValue() returns the exact Long value
         * that was used to create the PaymentId.
         */
        @Test
        @DisplayName("Should return the internal Long value")
        void getValue_shouldReturnInternalValue() {
            Long expected = 999L;
            PaymentId paymentId = PaymentId.of(expected);

            Long result = paymentId.getValue();

            assertEquals(expected, result);
        }

        /**
         * Verifies that getValue() returns null for an empty instance.
         */
        @Test
        @DisplayName("Should return null for empty instance")
        void getValue_whenEmpty_shouldReturnNull() {
            PaymentId emptyId = PaymentId.empty();

            assertNull(emptyId.getValue());
        }
    }

    // ==================== EQUALITY CONTRACT (MEDIUM PRIORITY) ====================

    @Nested
    @DisplayName("Equality Contract: equals() and hashCode()")
    class EqualityTests {

        /**
         * Verifies that two PaymentId instances with the same value are equal.
         */
        @Test
        @DisplayName("equals() should return true for same value")
        void equals_withSameValue_shouldReturnTrue() {
            PaymentId id1 = PaymentId.of(50L);
            PaymentId id2 = PaymentId.of(50L);

            assertEquals(id1, id2);
        }

        /**
         * Verifies that two PaymentId instances with different values are not equal.
         */
        @Test
        @DisplayName("equals() should return false for different values")
        void equals_withDifferentValue_shouldReturnFalse() {
            PaymentId id1 = PaymentId.of(1L);
            PaymentId id2 = PaymentId.of(2L);

            assertNotEquals(id1, id2);
        }

        /**
         * Verifies that equals() returns false when compared with null.
         */
        @Test
        @DisplayName("equals() should return false when compared with null")
        void equals_withNull_shouldReturnFalse() {
            PaymentId paymentId = PaymentId.of(10L);

            assertNotEquals(null, paymentId);
        }

        /**
         * Verifies that equals() returns false when compared with a different type.
         */
        @Test
        @DisplayName("equals() should return false when compared with different type")
        void equals_withDifferentType_shouldReturnFalse() {
            PaymentId paymentId = PaymentId.of(10L);
            Long differentType = 10L;

            assertNotEquals(differentType, paymentId);
        }

        /**
         * Verifies reflexivity: an object must equal itself.
         */
        @Test
        @DisplayName("equals() should be reflexive")
        void equals_reflexive_shouldReturnTrue() {
            PaymentId paymentId = PaymentId.of(77L);

            assertEquals(paymentId, paymentId);
        }

        /**
         * Verifies that two empty instances are equal to each other.
         */
        @Test
        @DisplayName("Two empty instances should be equal")
        void equals_twoEmptyInstances_shouldReturnTrue() {
            PaymentId empty1 = PaymentId.empty();
            PaymentId empty2 = PaymentId.empty();

            assertEquals(empty1, empty2);
        }

        /**
         * Verifies that an empty instance is not equal to a valid instance.
         */
        @Test
        @DisplayName("Empty instance should not equal valid instance")
        void equals_emptyAndValid_shouldReturnFalse() {
            PaymentId empty = PaymentId.empty();
            PaymentId valid = PaymentId.of(1L);

            assertNotEquals(empty, valid);
        }

        /**
         * Verifies that equal objects have the same hashCode.
         */
        @Test
        @DisplayName("hashCode() should be same for equal objects")
        void hashCode_withSameValue_shouldReturnSameHash() {
            PaymentId id1 = PaymentId.of(123L);
            PaymentId id2 = PaymentId.of(123L);

            assertEquals(id1.hashCode(), id2.hashCode());
        }

        /**
         * Verifies hashCode consistency: multiple calls return the same value.
         */
        @Test
        @DisplayName("hashCode() should be consistent across multiple calls")
        void hashCode_multipleCalls_shouldBeConsistent() {
            PaymentId paymentId = PaymentId.of(456L);

            int hash1 = paymentId.hashCode();
            int hash2 = paymentId.hashCode();
            int hash3 = paymentId.hashCode();

            assertEquals(hash1, hash2);
            assertEquals(hash2, hash3);
        }

        /**
         * Verifies that hashCode does not throw exception for empty instance.
         */
        @Test
        @DisplayName("hashCode() should not throw for empty instance")
        void hashCode_emptyInstance_shouldNotThrow() {
            PaymentId emptyId = PaymentId.empty();

            assertDoesNotThrow(emptyId::hashCode);
        }

        /**
         * Verifies that two empty instances have the same hashCode.
         */
        @Test
        @DisplayName("Two empty instances should have same hashCode")
        void hashCode_twoEmptyInstances_shouldReturnSameHash() {
            PaymentId empty1 = PaymentId.empty();
            PaymentId empty2 = PaymentId.empty();

            assertEquals(empty1.hashCode(), empty2.hashCode());
        }
    }
}

