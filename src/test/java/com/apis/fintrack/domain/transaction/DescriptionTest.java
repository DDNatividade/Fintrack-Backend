package com.apis.fintrack.domain.transaction;

import com.apis.fintrack.domain.transaction.model.Description;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link Description} value object.
 *
 * <p>Tests cover factory method, validation rules (length limits),
 * and equality contract following specification-based testing approaches.</p>
 */
@DisplayName("Description Value Object Tests")
class DescriptionTest {

    // ==================== FACTORY METHOD: of() - HIGH PRIORITY ====================

    @Nested
    @DisplayName("Factory Method: of()")
    class OfFactoryTests {

        /**
         * Verifies that the factory method creates a valid Description.
         */
        @Test
        @DisplayName("Should return Description when given a valid value")
        void of_withValidValue_shouldReturnDescription() {
            String validValue = "Monthly salary";

            Description result = Description.of(validValue);

            assertNotNull(result);
            assertEquals(validValue, result.getValue());
        }

        /**
         * Verifies that null value throws IllegalArgumentException.
         */
        @Test
        @DisplayName("Should throw IllegalArgumentException when value is null")
        void of_withNullValue_shouldThrowIllegalArgumentException() {
            assertThrows(
                    IllegalArgumentException.class,
                    () -> Description.of(null)
            );
        }

        /**
         * Verifies that empty value throws IllegalArgumentException.
         */
        @Test
        @DisplayName("Should throw IllegalArgumentException when value is empty")
        void of_withEmptyValue_shouldThrowIllegalArgumentException() {
            assertThrows(
                    IllegalArgumentException.class,
                    () -> Description.of("")
            );
        }

        /**
         * Verifies that whitespace-only value throws IllegalArgumentException.
         */
        @Test
        @DisplayName("Should throw IllegalArgumentException when value is only whitespace")
        void of_withWhitespaceValue_shouldThrowIllegalArgumentException() {
            assertThrows(
                    IllegalArgumentException.class,
                    () -> Description.of("   ")
            );
        }

        /**
         * Verifies that value exceeding 100 characters throws exception.
         */
        @Test
        @DisplayName("Should throw IllegalArgumentException when value exceeds 100 characters")
        void of_withTooLongValue_shouldThrowIllegalArgumentException() {
            String longValue = "A".repeat(101);

            assertThrows(
                    IllegalArgumentException.class,
                    () -> Description.of(longValue)
            );
        }

        /**
         * Verifies that exactly 100 characters is accepted.
         */
        @Test
        @DisplayName("Should accept exactly 100 characters")
        void of_withExactly100Chars_shouldReturnDescription() {
            String maxValue = "A".repeat(100);

            Description result = Description.of(maxValue);

            assertEquals(100, result.getValue().length());
        }

        /**
         * Verifies that minimum length (1 character) is accepted.
         */
        @Test
        @DisplayName("Should accept 1 character description")
        void of_withOneChar_shouldReturnDescription() {
            Description result = Description.of("A");

            assertEquals("A", result.getValue());
        }

        /**
         * Verifies that leading and trailing whitespace is trimmed.
         */
        @Test
        @DisplayName("Should trim leading and trailing whitespace")
        void of_withWhitespace_shouldTrimValue() {
            Description result = Description.of("  test description  ");

            assertEquals("test description", result.getValue());
        }
    }

    // ==================== VALUE LENGTH - MEDIUM PRIORITY ====================

    @Nested
    @DisplayName("Value Length")
    class LengthTests {

        /**
         * Verifies that getValue().length() returns correct length.
         */
        @Test
        @DisplayName("Should return correct length")
        void getValue_length_shouldReturnCorrectLength() {
            Description description = Description.of("Hello World");

            assertEquals(11, description.getValue().length());
        }

        /**
         * Verifies that getValue().length() returns trimmed length.
         */
        @Test
        @DisplayName("Should return length after trimming")
        void getValue_length_withTrimmedValue_shouldReturnCorrectLength() {
            Description description = Description.of("  test  ");

            assertEquals(4, description.getValue().length());
        }
    }

    // ==================== EQUALITY CONTRACT - MEDIUM PRIORITY ====================

    @Nested
    @DisplayName("Equality Contract: equals() and hashCode()")
    class EqualityTests {

        /**
         * Verifies that two Description with same value are equal.
         */
        @Test
        @DisplayName("equals() should return true for same value")
        void equals_withSameValue_shouldReturnTrue() {
            Description desc1 = Description.of("Test");
            Description desc2 = Description.of("Test");

            assertEquals(desc1, desc2);
        }

        /**
         * Verifies that two Description with different values are not equal.
         */
        @Test
        @DisplayName("equals() should return false for different values")
        void equals_withDifferentValue_shouldReturnFalse() {
            Description desc1 = Description.of("Test1");
            Description desc2 = Description.of("Test2");

            assertNotEquals(desc1, desc2);
        }

        /**
         * Verifies that equals() returns false when compared with null.
         */
        @Test
        @DisplayName("equals() should return false when compared with null")
        void equals_withNull_shouldReturnFalse() {
            Description description = Description.of("Test");

            assertNotEquals(null, description);
        }

        /**
         * Verifies reflexivity.
         */
        @Test
        @DisplayName("equals() should be reflexive")
        void equals_reflexive_shouldReturnTrue() {
            Description description = Description.of("Test");

            assertEquals(description, description);
        }

        /**
         * Verifies that trimmed values are considered equal.
         */
        @Test
        @DisplayName("Trimmed values should be equal")
        void equals_trimmedValues_shouldReturnTrue() {
            Description desc1 = Description.of("Test");
            Description desc2 = Description.of("  Test  ");

            assertEquals(desc1, desc2);
        }

        /**
         * Verifies that equal objects have the same hashCode.
         */
        @Test
        @DisplayName("hashCode() should be same for equal objects")
        void hashCode_equalObjects_shouldReturnSameHash() {
            Description desc1 = Description.of("Test");
            Description desc2 = Description.of("Test");

            assertEquals(desc1.hashCode(), desc2.hashCode());
        }

        /**
         * Verifies hashCode consistency.
         */
        @Test
        @DisplayName("hashCode() should be consistent across multiple calls")
        void hashCode_multipleCalls_shouldBeConsistent() {
            Description description = Description.of("Test");

            int hash1 = description.hashCode();
            int hash2 = description.hashCode();

            assertEquals(hash1, hash2);
        }
    }
}

