package com.apis.fintrack.domain.user;

import com.apis.fintrack.domain.user.model.FullName;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link FullName} value object.
 *
 * <p>Tests cover factory method, validation rules (empty, length, characters),
 * immutability, and equality contract following specification-based testing approaches.</p>
 */
@DisplayName("FullName Value Object Tests")
class FullNameTest {

    // ==================== FACTORY METHOD: of() - HIGH PRIORITY ====================

    @Nested
    @DisplayName("Factory Method: of()")
    class OfFactoryTests {

        /**
         * Verifies that the factory method creates a valid FullName.
         */
        @Test
        @DisplayName("Should return FullName when given valid name and surname")
        void of_withValidValues_shouldReturnFullName() {
            FullName result = FullName.of("John", "Doe");

            assertNotNull(result);
            assertEquals("John", result.getName());
            assertEquals("Doe", result.getSurname());
        }

        /**
         * Verifies that name is capitalized automatically.
         */
        @Test
        @DisplayName("Should capitalize name automatically")
        void of_shouldCapitalizeName() {
            FullName result = FullName.of("john", "doe");

            assertEquals("John", result.getName());
            assertEquals("Doe", result.getSurname());
        }

        /**
         * Verifies that name is trimmed.
         */
        @Test
        @DisplayName("Should trim whitespace from name and surname")
        void of_shouldTrimWhitespace() {
            FullName result = FullName.of("  John  ", "  Doe  ");

            assertEquals("John", result.getName());
            assertEquals("Doe", result.getSurname());
        }

        /**
         * Verifies that null name throws IllegalArgumentException.
         */
        @Test
        @DisplayName("Should throw IllegalArgumentException when name is null")
        void of_withNullName_shouldThrowIllegalArgumentException() {
            assertThrows(
                    IllegalArgumentException.class,
                    () -> FullName.of(null, "Doe")
            );
        }

        /**
         * Verifies that null surname throws IllegalArgumentException.
         */
        @Test
        @DisplayName("Should throw IllegalArgumentException when surname is null")
        void of_withNullSurname_shouldThrowIllegalArgumentException() {
            assertThrows(
                    IllegalArgumentException.class,
                    () -> FullName.of("John", null)
            );
        }

        /**
         * Verifies that empty name throws IllegalArgumentException.
         */
        @Test
        @DisplayName("Should throw IllegalArgumentException when name is empty")
        void of_withEmptyName_shouldThrowIllegalArgumentException() {
            assertThrows(
                    IllegalArgumentException.class,
                    () -> FullName.of("", "Doe")
            );
        }

        /**
         * Verifies that blank name throws IllegalArgumentException.
         */
        @Test
        @DisplayName("Should throw IllegalArgumentException when name is blank")
        void of_withBlankName_shouldThrowIllegalArgumentException() {
            assertThrows(
                    IllegalArgumentException.class,
                    () -> FullName.of("   ", "Doe")
            );
        }

        /**
         * Verifies that name exceeding 50 characters throws exception.
         */
        @Test
        @DisplayName("Should throw IllegalArgumentException when name exceeds 50 characters")
        void of_nameTooLong_shouldThrowIllegalArgumentException() {
            String longName = "A".repeat(51);

            assertThrows(
                    IllegalArgumentException.class,
                    () -> FullName.of(longName, "Doe")
            );
        }

        /**
         * Verifies that surname exceeding 50 characters throws exception.
         */
        @Test
        @DisplayName("Should throw IllegalArgumentException when surname exceeds 50 characters")
        void of_surnameTooLong_shouldThrowIllegalArgumentException() {
            String longSurname = "A".repeat(51);

            assertThrows(
                    IllegalArgumentException.class,
                    () -> FullName.of("John", longSurname)
            );
        }

        /**
         * Verifies that names with special characters (accents) are accepted.
         */
        @Test
        @DisplayName("Should accept names with accented characters")
        void of_withAccentedCharacters_shouldReturnFullName() {
            FullName result = FullName.of("José", "García");

            assertNotNull(result);
        }

        /**
         * Verifies that names with hyphens are accepted.
         */
        @Test
        @DisplayName("Should accept names with hyphens")
        void of_withHyphen_shouldReturnFullName() {
            FullName result = FullName.of("Mary-Jane", "Smith-Jones");

            assertNotNull(result);
        }

        /**
         * Verifies that names with apostrophes are accepted.
         */
        @Test
        @DisplayName("Should accept names with apostrophes")
        void of_withApostrophe_shouldReturnFullName() {
            FullName result = FullName.of("O'Connor", "D'Angelo");

            assertNotNull(result);
        }
    }

    // ==================== IMMUTABILITY: withName(), withSurname() - HIGH PRIORITY ====================

    @Nested
    @DisplayName("Immutability: withName() and withSurname()")
    class ImmutabilityTests {

        /**
         * Verifies that withName() returns a new instance.
         */
        @Test
        @DisplayName("withName() should return a new instance with updated name")
        void withName_shouldReturnNewInstance() {
            FullName original = FullName.of("John", "Doe");

            FullName updated = original.withName("Jane");

            assertNotSame(original, updated);
            assertEquals("John", original.getName()); // Original unchanged
            assertEquals("Jane", updated.getName());
            assertEquals("Doe", updated.getSurname()); // Surname preserved
        }

        /**
         * Verifies that withSurname() returns a new instance.
         */
        @Test
        @DisplayName("withSurname() should return a new instance with updated surname")
        void withSurname_shouldReturnNewInstance() {
            FullName original = FullName.of("John", "Doe");

            FullName updated = original.withSurname("Smith");

            assertNotSame(original, updated);
            assertEquals("Doe", original.getSurname()); // Original unchanged
            assertEquals("Smith", updated.getSurname());
            assertEquals("John", updated.getName()); // Name preserved
        }

        /**
         * Verifies that withName() with null throws exception.
         */
        @Test
        @DisplayName("withName() should throw exception for null")
        void withName_null_shouldThrowException() {
            FullName fullName = FullName.of("John", "Doe");

            assertThrows(
                    IllegalArgumentException.class,
                    () -> fullName.withName(null)
            );
        }

        /**
         * Verifies that withSurname() with null throws exception.
         */
        @Test
        @DisplayName("withSurname() should throw exception for null")
        void withSurname_null_shouldThrowException() {
            FullName fullName = FullName.of("John", "Doe");

            assertThrows(
                    IllegalArgumentException.class,
                    () -> fullName.withSurname(null)
            );
        }
    }

    // ==================== EQUALITY CONTRACT - MEDIUM PRIORITY ====================

    @Nested
    @DisplayName("Equality Contract: equals() and hashCode()")
    class EqualityTests {

        /**
         * Verifies that two FullName with same values are equal.
         */
        @Test
        @DisplayName("equals() should return true for same values")
        void equals_withSameValues_shouldReturnTrue() {
            FullName name1 = FullName.of("John", "Doe");
            FullName name2 = FullName.of("John", "Doe");

            assertEquals(name1, name2);
        }

        /**
         * Verifies that FullName with different names are not equal.
         */
        @Test
        @DisplayName("equals() should return false for different names")
        void equals_withDifferentName_shouldReturnFalse() {
            FullName name1 = FullName.of("John", "Doe");
            FullName name2 = FullName.of("Jane", "Doe");

            assertNotEquals(name1, name2);
        }

        /**
         * Verifies that FullName with different surnames are not equal.
         */
        @Test
        @DisplayName("equals() should return false for different surnames")
        void equals_withDifferentSurname_shouldReturnFalse() {
            FullName name1 = FullName.of("John", "Doe");
            FullName name2 = FullName.of("John", "Smith");

            assertNotEquals(name1, name2);
        }

        /**
         * Verifies that equals() returns false when compared with null.
         */
        @Test
        @DisplayName("equals() should return false when compared with null")
        void equals_withNull_shouldReturnFalse() {
            FullName fullName = FullName.of("John", "Doe");

            assertNotEquals(null, fullName);
        }

        /**
         * Verifies reflexivity.
         */
        @Test
        @DisplayName("equals() should be reflexive")
        void equals_reflexive_shouldReturnTrue() {
            FullName fullName = FullName.of("John", "Doe");

            assertEquals(fullName, fullName);
        }

        /**
         * Verifies that equal objects have the same hashCode.
         */
        @Test
        @DisplayName("hashCode() should be same for equal objects")
        void hashCode_equalObjects_shouldReturnSameHash() {
            FullName name1 = FullName.of("John", "Doe");
            FullName name2 = FullName.of("John", "Doe");

            assertEquals(name1.hashCode(), name2.hashCode());
        }
    }
}

