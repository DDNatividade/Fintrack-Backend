package com.apis.fintrack.domain.user;

import com.apis.fintrack.domain.user.model.UserId;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link UserId} value object.
 *
 * <p>Tests cover factory methods, validation rules, equality contract,
 * and edge cases following specification-based testing approaches.</p>
 */
@DisplayName("UserId Value Object Tests")
class UserIdTest {

    // ==================== FACTORY METHOD: of() - HIGH PRIORITY ====================

    @Nested
    @DisplayName("Factory Method: of()")
    class OfFactoryTests {

        /**
         * Verifies that the factory method creates a valid UserId
         * when provided with a positive Long value.
         */
        @Test
        @DisplayName("Should return UserId when given a valid positive value")
        void of_withValidPositiveValue_shouldReturnUserId() {
            Long validValue = 100L;

            UserId result = UserId.of(validValue);

            assertNotNull(result);
            assertEquals(validValue, result.getValue());
            assertFalse(result.isEmpty());
        }

        /**
         * Verifies that null value throws IllegalArgumentException.
         */
        @Test
        @DisplayName("Should throw IllegalArgumentException when value is null")
        void of_withNullValue_shouldThrowIllegalArgumentException() {
            assertThrows(
                    IllegalArgumentException.class,
                    () -> UserId.of(null)
            );
        }

        /**
         * Verifies that zero value throws IllegalArgumentException.
         */
        @Test
        @DisplayName("Should throw IllegalArgumentException when value is zero")
        void of_withZeroValue_shouldThrowIllegalArgumentException() {
            assertThrows(
                    IllegalArgumentException.class,
                    () -> UserId.of(0L)
            );
        }

        /**
         * Verifies that negative value throws IllegalArgumentException.
         */
        @Test
        @DisplayName("Should throw IllegalArgumentException when value is negative")
        void of_withNegativeValue_shouldThrowIllegalArgumentException() {
            assertThrows(
                    IllegalArgumentException.class,
                    () -> UserId.of(-5L)
            );
        }

        /**
         * Verifies that minimum valid value (1) is accepted.
         */
        @Test
        @DisplayName("Should accept value of 1 (minimum valid)")
        void of_withValueOne_shouldReturnUserId() {
            UserId result = UserId.of(1L);

            assertEquals(1L, result.getValue());
        }

        /**
         * Verifies that Long.MAX_VALUE is accepted.
         */
        @Test
        @DisplayName("Should accept Long.MAX_VALUE")
        void of_withMaxLongValue_shouldReturnUserId() {
            UserId result = UserId.of(Long.MAX_VALUE);

            assertEquals(Long.MAX_VALUE, result.getValue());
        }
    }

    // ==================== FACTORY METHOD: empty() - HIGH PRIORITY ====================

    @Nested
    @DisplayName("Factory Method: empty()")
    class EmptyFactoryTests {

        /**
         * Verifies that empty() creates a UserId with null internal value.
         */
        @Test
        @DisplayName("Should return UserId with null value")
        void empty_shouldReturnUserIdWithNullValue() {
            UserId result = UserId.empty();

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
            UserId emptyId = UserId.empty();

            assertTrue(emptyId.isEmpty());
        }

        /**
         * Verifies that isEmpty() returns false for a valid instance.
         */
        @Test
        @DisplayName("isEmpty() should return false for valid instance")
        void isEmpty_whenNotEmpty_shouldReturnFalse() {
            UserId validId = UserId.of(42L);

            assertFalse(validId.isEmpty());
        }
    }

    // ==================== EQUALITY CONTRACT - MEDIUM PRIORITY ====================

    @Nested
    @DisplayName("Equality Contract: equals() and hashCode()")
    class EqualityTests {

        /**
         * Verifies that two UserId instances with the same value are equal.
         */
        @Test
        @DisplayName("equals() should return true for same value")
        void equals_withSameValue_shouldReturnTrue() {
            UserId id1 = UserId.of(50L);
            UserId id2 = UserId.of(50L);

            assertEquals(id1, id2);
        }

        /**
         * Verifies that two UserId instances with different values are not equal.
         */
        @Test
        @DisplayName("equals() should return false for different values")
        void equals_withDifferentValue_shouldReturnFalse() {
            UserId id1 = UserId.of(1L);
            UserId id2 = UserId.of(2L);

            assertNotEquals(id1, id2);
        }

        /**
         * Verifies that equals() returns false when compared with null.
         */
        @Test
        @DisplayName("equals() should return false when compared with null")
        void equals_withNull_shouldReturnFalse() {
            UserId userId = UserId.of(10L);

            assertNotEquals(null, userId);
        }

        /**
         * Verifies reflexivity.
         */
        @Test
        @DisplayName("equals() should be reflexive")
        void equals_reflexive_shouldReturnTrue() {
            UserId userId = UserId.of(77L);

            assertEquals(userId, userId);
        }

        /**
         * Verifies that two empty instances are equal.
         */
        @Test
        @DisplayName("Two empty instances should be equal")
        void equals_twoEmptyInstances_shouldReturnTrue() {
            UserId empty1 = UserId.empty();
            UserId empty2 = UserId.empty();

            assertEquals(empty1, empty2);
        }

        /**
         * Verifies that equal objects have the same hashCode.
         */
        @Test
        @DisplayName("hashCode() should be same for equal objects")
        void hashCode_withSameValue_shouldReturnSameHash() {
            UserId id1 = UserId.of(123L);
            UserId id2 = UserId.of(123L);

            assertEquals(id1.hashCode(), id2.hashCode());
        }

        /**
         * Verifies hashCode consistency.
         */
        @Test
        @DisplayName("hashCode() should be consistent across multiple calls")
        void hashCode_multipleCalls_shouldBeConsistent() {
            UserId userId = UserId.of(456L);

            int hash1 = userId.hashCode();
            int hash2 = userId.hashCode();

            assertEquals(hash1, hash2);
        }
    }

    // ==================== toString() - MEDIUM PRIORITY ====================

    @Nested
    @DisplayName("Method: toString()")
    class ToStringTests {

        /**
         * Verifies that toString() returns the value as string.
         */
        @Test
        @DisplayName("toString() should return value as string")
        void toString_shouldReturnValueAsString() {
            UserId userId = UserId.of(42L);

            assertEquals("42", userId.toString());
        }

        /**
         * Verifies that toString() returns "empty" for empty instance.
         */
        @Test
        @DisplayName("toString() should return 'empty' for empty instance")
        void toString_empty_shouldReturnEmpty() {
            UserId emptyId = UserId.empty();

            assertEquals("empty", emptyId.toString());
        }
    }
}

