package com.apis.fintrack.domain.user;

import com.apis.fintrack.domain.user.model.Password;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link Password} value object.
 *
 * <p>Tests cover factory methods, validation rules (length, uppercase,
 * lowercase, digit), and security features following specification-based
 * testing approaches.</p>
 */
@DisplayName("Password Value Object Tests")
class PasswordTest {

    private static final String VALID_PASSWORD = "ValidPass1";

    // ==================== FACTORY METHOD: of() - HIGH PRIORITY ====================

    @Nested
    @DisplayName("Factory Method: of()")
    class OfFactoryTests {

        /**
         * Verifies that the factory method creates a valid Password.
         */
        @Test
        @DisplayName("Should return Password when given valid password")
        void of_withValidPassword_shouldReturnPassword() {
            Password result = Password.of(VALID_PASSWORD);

            assertNotNull(result);
            assertEquals(VALID_PASSWORD, result.getValue());
        }

        /**
         * Verifies that null value throws IllegalArgumentException.
         */
        @Test
        @DisplayName("Should throw IllegalArgumentException when value is null")
        void of_withNullValue_shouldThrowIllegalArgumentException() {
            assertThrows(
                    IllegalArgumentException.class,
                    () -> Password.of(null)
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
                    () -> Password.of("")
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
                    () -> Password.of("   ")
            );
        }

        /**
         * Verifies that password shorter than 8 characters throws exception.
         */
        @Test
        @DisplayName("Should throw IllegalArgumentException when less than 8 characters")
        void of_lessThan8Chars_shouldThrowIllegalArgumentException() {
            assertThrows(
                    IllegalArgumentException.class,
                    () -> Password.of("Short1A")
            );
        }

        /**
         * Verifies that password with exactly 8 characters is accepted.
         */
        @Test
        @DisplayName("Should accept password with exactly 8 characters")
        void of_exactly8Chars_shouldReturnPassword() {
            Password result = Password.of("Pass123A");

            assertNotNull(result);
        }

        /**
         * Verifies that password without uppercase throws exception.
         */
        @Test
        @DisplayName("Should throw IllegalArgumentException when no uppercase letter")
        void of_noUppercase_shouldThrowIllegalArgumentException() {
            assertThrows(
                    IllegalArgumentException.class,
                    () -> Password.of("alllowercase1")
            );
        }

        /**
         * Verifies that password without lowercase throws exception.
         */
        @Test
        @DisplayName("Should throw IllegalArgumentException when no lowercase letter")
        void of_noLowercase_shouldThrowIllegalArgumentException() {
            assertThrows(
                    IllegalArgumentException.class,
                    () -> Password.of("ALLUPPERCASE1")
            );
        }

        /**
         * Verifies that password without digit throws exception.
         */
        @Test
        @DisplayName("Should throw IllegalArgumentException when no digit")
        void of_noDigit_shouldThrowIllegalArgumentException() {
            assertThrows(
                    IllegalArgumentException.class,
                    () -> Password.of("NoDigitsHere")
            );
        }
    }

    // ==================== FACTORY METHOD: fromStorage() - HIGH PRIORITY ====================

    @Nested
    @DisplayName("Factory Method: fromStorage()")
    class FromStorageFactoryTests {

        /**
         * Verifies that fromStorage() creates Password without validation.
         */
        @Test
        @DisplayName("Should create Password from stored value without validation")
        void fromStorage_withStoredValue_shouldReturnPassword() {
            String hashedPassword = "$2a$10$hashedPasswordValue";

            Password result = Password.fromStorage(hashedPassword);

            assertNotNull(result);
            assertEquals(hashedPassword, result.getValue());
        }

        /**
         * Verifies that fromStorage() throws for null.
         */
        @Test
        @DisplayName("Should throw IllegalArgumentException when stored value is null")
        void fromStorage_withNullValue_shouldThrowIllegalArgumentException() {
            assertThrows(
                    IllegalArgumentException.class,
                    () -> Password.fromStorage(null)
            );
        }

        /**
         * Verifies that fromStorage() throws for blank.
         */
        @Test
        @DisplayName("Should throw IllegalArgumentException when stored value is blank")
        void fromStorage_withBlankValue_shouldThrowIllegalArgumentException() {
            assertThrows(
                    IllegalArgumentException.class,
                    () -> Password.fromStorage("   ")
            );
        }
    }

    // ==================== EQUALITY CONTRACT - MEDIUM PRIORITY ====================

    @Nested
    @DisplayName("Equality Contract: equals() and hashCode()")
    class EqualityTests {

        /**
         * Verifies that two Password with same value are equal.
         */
        @Test
        @DisplayName("equals() should return true for same value")
        void equals_withSameValue_shouldReturnTrue() {
            Password pass1 = Password.of(VALID_PASSWORD);
            Password pass2 = Password.of(VALID_PASSWORD);

            assertEquals(pass1, pass2);
        }

        /**
         * Verifies that two Password with different values are not equal.
         */
        @Test
        @DisplayName("equals() should return false for different values")
        void equals_withDifferentValue_shouldReturnFalse() {
            Password pass1 = Password.of("Password1");
            Password pass2 = Password.of("Password2");

            assertNotEquals(pass1, pass2);
        }

        /**
         * Verifies that equals() returns false when compared with null.
         */
        @Test
        @DisplayName("equals() should return false when compared with null")
        void equals_withNull_shouldReturnFalse() {
            Password password = Password.of(VALID_PASSWORD);

            assertNotEquals(null, password);
        }

        /**
         * Verifies reflexivity.
         */
        @Test
        @DisplayName("equals() should be reflexive")
        void equals_reflexive_shouldReturnTrue() {
            Password password = Password.of(VALID_PASSWORD);

            assertEquals(password, password);
        }

        /**
         * Verifies that equal objects have the same hashCode.
         */
        @Test
        @DisplayName("hashCode() should be same for equal objects")
        void hashCode_equalObjects_shouldReturnSameHash() {
            Password pass1 = Password.of(VALID_PASSWORD);
            Password pass2 = Password.of(VALID_PASSWORD);

            assertEquals(pass1.hashCode(), pass2.hashCode());
        }
    }

    // ==================== SECURITY - MEDIUM PRIORITY ====================

    @Nested
    @DisplayName("Security Features")
    class SecurityTests {

        /**
         * Verifies that toString() does not expose password value.
         */
        @Test
        @DisplayName("toString() should not expose password value")
        void toString_shouldNotExposeValue() {
            Password password = Password.of(VALID_PASSWORD);

            String result = password.toString();

            assertFalse(result.contains(VALID_PASSWORD));
            assertEquals("[PROTECTED]", result);
        }
    }
}

