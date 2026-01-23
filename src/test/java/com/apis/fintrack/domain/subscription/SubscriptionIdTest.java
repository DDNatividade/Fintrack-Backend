package com.apis.fintrack.domain.subscription;

import com.apis.fintrack.domain.subscription.model.SubscriptionId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link SubscriptionId} value object.
 *
 * <p>Tests cover factory methods, validation rules, equality contract,
 * and edge cases following specification-based testing approaches.</p>
 */
@DisplayName("SubscriptionId Value Object Tests")
class SubscriptionIdTest {

    // ==================== FACTORY METHOD: of() - HIGH PRIORITY ====================

    @Nested
    @DisplayName("Factory Method: of()")
    class OfFactoryTests {

        /**
         * Verifies that the factory method creates a valid SubscriptionId
         * when provided with a positive Long value.
         */
        @Test
        @DisplayName("Should return SubscriptionId when given a valid positive value")
        void of_withValidPositiveValue_shouldReturnSubscriptionId() {
            Long validValue = 100L;

            SubscriptionId result = SubscriptionId.of(validValue);

            assertNotNull(result);
            assertEquals(validValue, result.getValue());
            assertFalse(result.isEmpty());
        }

        /**
         * Verifies that the factory method throws IllegalArgumentException
         * when provided with a null value.
         */
        @Test
        @DisplayName("Should throw IllegalArgumentException when value is null")
        void of_withNullValue_shouldThrowIllegalArgumentException() {
            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> SubscriptionId.of(null)
            );

            assertNotNull(exception.getMessage());
        }

        /**
         * Verifies that the factory method throws IllegalArgumentException
         * when provided with zero.
         */
        @Test
        @DisplayName("Should throw IllegalArgumentException when value is zero")
        void of_withZeroValue_shouldThrowIllegalArgumentException() {
            assertThrows(
                    IllegalArgumentException.class,
                    () -> SubscriptionId.of(0L)
            );
        }

        /**
         * Verifies that the factory method throws IllegalArgumentException
         * when provided with a negative value.
         */
        @Test
        @DisplayName("Should throw IllegalArgumentException when value is negative")
        void of_withNegativeValue_shouldThrowIllegalArgumentException() {
            assertThrows(
                    IllegalArgumentException.class,
                    () -> SubscriptionId.of(-5L)
            );
        }

        /**
         * Verifies that the minimum valid value (1) is accepted.
         */
        @Test
        @DisplayName("Should accept value of 1 (minimum valid)")
        void of_withValueOne_shouldReturnSubscriptionId() {
            SubscriptionId result = SubscriptionId.of(1L);

            assertEquals(1L, result.getValue());
            assertFalse(result.isEmpty());
        }

        /**
         * Verifies that Long.MAX_VALUE is accepted.
         */
        @Test
        @DisplayName("Should accept Long.MAX_VALUE")
        void of_withMaxLongValue_shouldReturnSubscriptionId() {
            SubscriptionId result = SubscriptionId.of(Long.MAX_VALUE);

            assertEquals(Long.MAX_VALUE, result.getValue());
        }

        /**
         * Verifies that -1 throws exception.
         */
        @Test
        @DisplayName("Should throw IllegalArgumentException when value is -1")
        void of_withMinusOne_shouldThrowIllegalArgumentException() {
            assertThrows(
                    IllegalArgumentException.class,
                    () -> SubscriptionId.of(-1L)
            );
        }
    }

    // ==================== FACTORY METHOD: empty() - HIGH PRIORITY ====================

    @Nested
    @DisplayName("Factory Method: empty()")
    class EmptyFactoryTests {

        /**
         * Verifies that empty() creates a SubscriptionId with null internal value.
         */
        @Test
        @DisplayName("Should return SubscriptionId with null value")
        void empty_shouldReturnSubscriptionIdWithNullValue() {
            SubscriptionId result = SubscriptionId.empty();

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
            SubscriptionId emptyId = SubscriptionId.empty();

            assertTrue(emptyId.isEmpty());
        }

        /**
         * Verifies that isEmpty() returns false for a valid instance.
         */
        @Test
        @DisplayName("isEmpty() should return false for valid instance")
        void isEmpty_whenNotEmpty_shouldReturnFalse() {
            SubscriptionId validId = SubscriptionId.of(42L);

            assertFalse(validId.isEmpty());
        }
    }

    // ==================== GETTER METHOD - MEDIUM PRIORITY ====================

    @Nested
    @DisplayName("Getter: getValue()")
    class GetterTests {

        /**
         * Verifies that getValue() returns the exact Long value
         * that was used to create the SubscriptionId.
         */
        @Test
        @DisplayName("Should return the internal Long value")
        void getValue_shouldReturnInternalValue() {
            Long expected = 999L;
            SubscriptionId subscriptionId = SubscriptionId.of(expected);

            Long result = subscriptionId.getValue();

            assertEquals(expected, result);
        }

        /**
         * Verifies that getValue() returns null for an empty instance.
         */
        @Test
        @DisplayName("Should return null for empty instance")
        void getValue_whenEmpty_shouldReturnNull() {
            SubscriptionId emptyId = SubscriptionId.empty();

            assertNull(emptyId.getValue());
        }
    }

    // ==================== EQUALITY CONTRACT - MEDIUM PRIORITY ====================

    @Nested
    @DisplayName("Equality Contract: equals() and hashCode()")
    class EqualityTests {

        /**
         * Verifies that two SubscriptionId instances with the same value are equal.
         */
        @Test
        @DisplayName("equals() should return true for same value")
        void equals_withSameValue_shouldReturnTrue() {
            SubscriptionId id1 = SubscriptionId.of(50L);
            SubscriptionId id2 = SubscriptionId.of(50L);

            assertEquals(id1, id2);
        }

        /**
         * Verifies that two SubscriptionId instances with different values are not equal.
         */
        @Test
        @DisplayName("equals() should return false for different values")
        void equals_withDifferentValue_shouldReturnFalse() {
            SubscriptionId id1 = SubscriptionId.of(1L);
            SubscriptionId id2 = SubscriptionId.of(2L);

            assertNotEquals(id1, id2);
        }

        /**
         * Verifies that equals() returns false when compared with null.
         */
        @Test
        @DisplayName("equals() should return false when compared with null")
        void equals_withNull_shouldReturnFalse() {
            SubscriptionId subscriptionId = SubscriptionId.of(10L);

            assertNotEquals(null, subscriptionId);
        }

        /**
         * Verifies that equals() returns false when compared with a different type.
         */
        @Test
        @DisplayName("equals() should return false when compared with different type")
        void equals_withDifferentType_shouldReturnFalse() {
            SubscriptionId subscriptionId = SubscriptionId.of(10L);
            Long differentType = 10L;

            assertNotEquals(differentType, subscriptionId);
        }

        /**
         * Verifies reflexivity: an object must equal itself.
         */
        @Test
        @DisplayName("equals() should be reflexive")
        void equals_reflexive_shouldReturnTrue() {
            SubscriptionId subscriptionId = SubscriptionId.of(77L);

            assertEquals(subscriptionId, subscriptionId);
        }

        /**
         * Verifies that two empty instances are equal.
         */
        @Test
        @DisplayName("Two empty instances should be equal")
        void equals_twoEmptyInstances_shouldReturnTrue() {
            SubscriptionId empty1 = SubscriptionId.empty();
            SubscriptionId empty2 = SubscriptionId.empty();

            assertEquals(empty1, empty2);
        }

        /**
         * Verifies that empty instance is not equal to valid instance.
         */
        @Test
        @DisplayName("Empty instance should not equal valid instance")
        void equals_emptyAndValid_shouldReturnFalse() {
            SubscriptionId empty = SubscriptionId.empty();
            SubscriptionId valid = SubscriptionId.of(1L);

            assertNotEquals(empty, valid);
        }

        /**
         * Verifies that equal objects have the same hashCode.
         */
        @Test
        @DisplayName("hashCode() should be same for equal objects")
        void hashCode_withSameValue_shouldReturnSameHash() {
            SubscriptionId id1 = SubscriptionId.of(123L);
            SubscriptionId id2 = SubscriptionId.of(123L);

            assertEquals(id1.hashCode(), id2.hashCode());
        }

        /**
         * Verifies hashCode consistency.
         */
        @Test
        @DisplayName("hashCode() should be consistent across multiple calls")
        void hashCode_multipleCalls_shouldBeConsistent() {
            SubscriptionId subscriptionId = SubscriptionId.of(456L);

            int hash1 = subscriptionId.hashCode();
            int hash2 = subscriptionId.hashCode();

            assertEquals(hash1, hash2);
        }

        /**
         * Verifies that hashCode does not throw exception for empty instance.
         */
        @Test
        @DisplayName("hashCode() should not throw for empty instance")
        void hashCode_emptyInstance_shouldNotThrow() {
            SubscriptionId emptyId = SubscriptionId.empty();

            assertDoesNotThrow(emptyId::hashCode);
        }

        /**
         * Verifies that two empty instances have the same hashCode.
         */
        @Test
        @DisplayName("Two empty instances should have same hashCode")
        void hashCode_twoEmptyInstances_shouldReturnSameHash() {
            SubscriptionId empty1 = SubscriptionId.empty();
            SubscriptionId empty2 = SubscriptionId.empty();

            assertEquals(empty1.hashCode(), empty2.hashCode());
        }
    }
}

