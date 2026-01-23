package com.apis.fintrack.domain.user;

import com.apis.fintrack.domain.user.model.Email;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link Email} value object.
 *
 * <p>Tests cover factory method, format validation, case insensitivity,
 * and equality contract following specification-based testing approaches.</p>
 */
@DisplayName("Email Value Object Tests")
class EmailTest {

    // ==================== FACTORY METHOD: of() - HIGH PRIORITY ====================

    @Nested
    @DisplayName("Factory Method: of()")
    class OfFactoryTests {

        /**
         * Verifies that the factory method creates a valid Email.
         */
        @Test
        @DisplayName("Should return Email when given a valid email address")
        void of_withValidEmail_shouldReturnEmail() {
            String validEmail = "user@example.com";

            Email result = Email.of(validEmail);

            assertNotNull(result);
            assertEquals("user@example.com", result.getValue());
        }

        /**
         * Verifies that null value throws IllegalArgumentException.
         */
        @Test
        @DisplayName("Should throw IllegalArgumentException when value is null")
        void of_withNullValue_shouldThrowIllegalArgumentException() {
            assertThrows(
                    IllegalArgumentException.class,
                    () -> Email.of(null)
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
                    () -> Email.of("")
            );
        }

        /**
         * Verifies that blank value throws IllegalArgumentException.
         */
        @Test
        @DisplayName("Should throw IllegalArgumentException when value is blank")
        void of_withBlankValue_shouldThrowIllegalArgumentException() {
            assertThrows(
                    IllegalArgumentException.class,
                    () -> Email.of("   ")
            );
        }

        /**
         * Verifies that invalid format throws IllegalArgumentException.
         */
        @Test
        @DisplayName("Should throw IllegalArgumentException for invalid format")
        void of_withInvalidFormat_shouldThrowIllegalArgumentException() {
            assertThrows(IllegalArgumentException.class, () -> Email.of("invalid"));
            assertThrows(IllegalArgumentException.class, () -> Email.of("invalid@"));
            assertThrows(IllegalArgumentException.class, () -> Email.of("@domain.com"));
            assertThrows(IllegalArgumentException.class, () -> Email.of("user@domain"));
            assertThrows(IllegalArgumentException.class, () -> Email.of("user domain.com"));
        }

        /**
         * Verifies that email is converted to lowercase.
         */
        @Test
        @DisplayName("Should convert email to lowercase")
        void of_withUppercase_shouldConvertToLowercase() {
            Email result = Email.of("USER@EXAMPLE.COM");

            assertEquals("user@example.com", result.getValue());
        }

        /**
         * Verifies that email is trimmed.
         */
        @Test
        @DisplayName("Should trim whitespace")
        void of_withWhitespace_shouldTrim() {
            Email result = Email.of("  user@example.com  ");

            assertEquals("user@example.com", result.getValue());
        }

        /**
         * Verifies various valid email formats are accepted.
         */
        @Test
        @DisplayName("Should accept various valid email formats")
        void of_variousValidFormats_shouldReturnEmail() {
            assertDoesNotThrow(() -> Email.of("user@example.com"));
            assertDoesNotThrow(() -> Email.of("user.name@example.com"));
            assertDoesNotThrow(() -> Email.of("user+tag@example.com"));
            assertDoesNotThrow(() -> Email.of("user123@example.co.uk"));
            assertDoesNotThrow(() -> Email.of("user_name@example.org"));
        }
    }

    // ==================== EQUALITY CONTRACT - MEDIUM PRIORITY ====================

    @Nested
    @DisplayName("Equality Contract: equals() and hashCode()")
    class EqualityTests {

        /**
         * Verifies that two Email with same value are equal.
         */
        @Test
        @DisplayName("equals() should return true for same value")
        void equals_withSameValue_shouldReturnTrue() {
            Email email1 = Email.of("user@example.com");
            Email email2 = Email.of("user@example.com");

            assertEquals(email1, email2);
        }

        /**
         * Verifies that emails with different case are equal (case insensitive).
         */
        @Test
        @DisplayName("equals() should return true for different case (case insensitive)")
        void equals_differentCase_shouldReturnTrue() {
            Email email1 = Email.of("USER@EXAMPLE.COM");
            Email email2 = Email.of("user@example.com");

            assertEquals(email1, email2);
        }

        /**
         * Verifies that two Email with different values are not equal.
         */
        @Test
        @DisplayName("equals() should return false for different values")
        void equals_withDifferentValue_shouldReturnFalse() {
            Email email1 = Email.of("user1@example.com");
            Email email2 = Email.of("user2@example.com");

            assertNotEquals(email1, email2);
        }

        /**
         * Verifies that equals() returns false when compared with null.
         */
        @Test
        @DisplayName("equals() should return false when compared with null")
        void equals_withNull_shouldReturnFalse() {
            Email email = Email.of("user@example.com");

            assertNotEquals(null, email);
        }

        /**
         * Verifies reflexivity.
         */
        @Test
        @DisplayName("equals() should be reflexive")
        void equals_reflexive_shouldReturnTrue() {
            Email email = Email.of("user@example.com");

            assertEquals(email, email);
        }

        /**
         * Verifies that equal objects have the same hashCode.
         */
        @Test
        @DisplayName("hashCode() should be same for equal objects")
        void hashCode_equalObjects_shouldReturnSameHash() {
            Email email1 = Email.of("user@example.com");
            Email email2 = Email.of("user@example.com");

            assertEquals(email1.hashCode(), email2.hashCode());
        }

        /**
         * Verifies hashCode consistency.
         */
        @Test
        @DisplayName("hashCode() should be consistent across multiple calls")
        void hashCode_multipleCalls_shouldBeConsistent() {
            Email email = Email.of("user@example.com");

            int hash1 = email.hashCode();
            int hash2 = email.hashCode();

            assertEquals(hash1, hash2);
        }
    }

    // ==================== toString() - MEDIUM PRIORITY ====================

    @Nested
    @DisplayName("Method: toString()")
    class ToStringTests {

        /**
         * Verifies that toString() returns the email value.
         */
        @Test
        @DisplayName("toString() should return the email value")
        void toString_shouldReturnEmailValue() {
            Email email = Email.of("user@example.com");

            assertEquals("user@example.com", email.toString());
        }
    }
}

