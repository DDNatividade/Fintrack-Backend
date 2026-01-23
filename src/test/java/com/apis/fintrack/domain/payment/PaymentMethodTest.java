package com.apis.fintrack.domain.payment;

import com.apis.fintrack.domain.payment.model.PaymentMethod;
import com.apis.fintrack.domain.payment.model.PaymentMethodType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link PaymentMethod} value object.
 *
 * <p>Tests cover factory methods, validation rules, query methods,
 * and equality contract following specification-based testing approaches.</p>
 */
@DisplayName("PaymentMethod Value Object Tests")
class PaymentMethodTest {

    private static final String VALID_TOKEN = "tok_visa_1234567890";
    private static final String VALID_LAST_FOUR = "4242";

    // ==================== FACTORY METHOD: of() - HIGH PRIORITY ====================

    @Nested
    @DisplayName("Factory Method: of()")
    class OfFactoryTests {

        /**
         * Verifies that the factory method creates a valid PaymentMethod
         * when provided with valid CREDIT_CARD parameters.
         */
        @Test
        @DisplayName("Should return PaymentMethod for valid CREDIT_CARD")
        void of_withValidCreditCard_shouldReturnPaymentMethod() {
            PaymentMethod result = PaymentMethod.of(
                    PaymentMethodType.CREDIT_CARD,
                    VALID_TOKEN,
                    VALID_LAST_FOUR
            );

            assertNotNull(result);
            assertEquals(PaymentMethodType.CREDIT_CARD, result.getType());
            assertEquals(VALID_TOKEN, result.getExternalToken());
            assertEquals(VALID_LAST_FOUR, result.getLastFourDigits());
            assertFalse(result.isEmpty());
        }

        /**
         * Verifies that the factory method creates a valid PaymentMethod
         * when provided with valid DEBIT_CARD parameters.
         */
        @Test
        @DisplayName("Should return PaymentMethod for valid DEBIT_CARD")
        void of_withValidDebitCard_shouldReturnPaymentMethod() {
            PaymentMethod result = PaymentMethod.of(
                    PaymentMethodType.DEBIT_CARD,
                    VALID_TOKEN,
                    "1234"
            );

            assertNotNull(result);
            assertEquals(PaymentMethodType.DEBIT_CARD, result.getType());
            assertFalse(result.isEmpty());
        }

        /**
         * Verifies that null type throws NullPointerException.
         */
        @Test
        @DisplayName("Should throw NullPointerException when type is null")
        void of_withNullType_shouldThrowNullPointerException() {
            NullPointerException exception = assertThrows(
                    NullPointerException.class,
                    () -> PaymentMethod.of(null, VALID_TOKEN, VALID_LAST_FOUR)
            );

            assertTrue(exception.getMessage().contains("type"));
        }

        /**
         * Verifies that null externalToken throws NullPointerException.
         */
        @Test
        @DisplayName("Should throw NullPointerException when externalToken is null")
        void of_withNullToken_shouldThrowNullPointerException() {
            NullPointerException exception = assertThrows(
                    NullPointerException.class,
                    () -> PaymentMethod.of(PaymentMethodType.CREDIT_CARD, null, VALID_LAST_FOUR)
            );

            assertTrue(exception.getMessage().contains("token"));
        }

        /**
         * Verifies that empty externalToken throws IllegalArgumentException.
         */
        @Test
        @DisplayName("Should throw IllegalArgumentException when externalToken is empty")
        void of_withEmptyToken_shouldThrowIllegalArgumentException() {
            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> PaymentMethod.of(PaymentMethodType.CREDIT_CARD, "", VALID_LAST_FOUR)
            );

            assertTrue(exception.getMessage().toLowerCase().contains("empty"));
        }

        /**
         * Verifies that blank externalToken throws IllegalArgumentException.
         */
        @Test
        @DisplayName("Should throw IllegalArgumentException when externalToken is blank")
        void of_withBlankToken_shouldThrowIllegalArgumentException() {
            assertThrows(
                    IllegalArgumentException.class,
                    () -> PaymentMethod.of(PaymentMethodType.CREDIT_CARD, "   ", VALID_LAST_FOUR)
            );
        }
    }

    // ==================== LAST FOUR DIGITS VALIDATION - HIGH PRIORITY ====================

    @Nested
    @DisplayName("Last Four Digits Validation")
    class LastFourDigitsValidationTests {

        /**
         * Verifies that null lastFourDigits throws IllegalArgumentException.
         */
        @Test
        @DisplayName("Should throw IllegalArgumentException when lastFourDigits is null")
        void of_withNullLastFour_shouldThrowIllegalArgumentException() {
            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> PaymentMethod.of(PaymentMethodType.CREDIT_CARD, VALID_TOKEN, null)
            );

            assertTrue(exception.getMessage().contains("4"));
        }

        /**
         * Verifies that lastFourDigits with less than 4 characters throws exception.
         */
        @Test
        @DisplayName("Should throw IllegalArgumentException when lastFourDigits has less than 4 chars")
        void of_withTooFewDigits_shouldThrowIllegalArgumentException() {
            assertThrows(
                    IllegalArgumentException.class,
                    () -> PaymentMethod.of(PaymentMethodType.CREDIT_CARD, VALID_TOKEN, "123")
            );
        }

        /**
         * Verifies that lastFourDigits with more than 4 characters throws exception.
         */
        @Test
        @DisplayName("Should throw IllegalArgumentException when lastFourDigits has more than 4 chars")
        void of_withTooManyDigits_shouldThrowIllegalArgumentException() {
            assertThrows(
                    IllegalArgumentException.class,
                    () -> PaymentMethod.of(PaymentMethodType.CREDIT_CARD, VALID_TOKEN, "12345")
            );
        }

        /**
         * Verifies that non-numeric lastFourDigits throws exception.
         */
        @Test
        @DisplayName("Should throw IllegalArgumentException when lastFourDigits contains letters")
        void of_withLettersInLastFour_shouldThrowIllegalArgumentException() {
            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> PaymentMethod.of(PaymentMethodType.CREDIT_CARD, VALID_TOKEN, "12ab")
            );

            assertTrue(exception.getMessage().toLowerCase().contains("numeric"));
        }

        /**
         * Verifies that lastFourDigits with special characters throws exception.
         */
        @Test
        @DisplayName("Should throw IllegalArgumentException when lastFourDigits contains special chars")
        void of_withSpecialCharsInLastFour_shouldThrowIllegalArgumentException() {
            assertThrows(
                    IllegalArgumentException.class,
                    () -> PaymentMethod.of(PaymentMethodType.CREDIT_CARD, VALID_TOKEN, "12-4")
            );
        }

        /**
         * Verifies that empty lastFourDigits throws exception.
         */
        @Test
        @DisplayName("Should throw IllegalArgumentException when lastFourDigits is empty")
        void of_withEmptyLastFour_shouldThrowIllegalArgumentException() {
            assertThrows(
                    IllegalArgumentException.class,
                    () -> PaymentMethod.of(PaymentMethodType.CREDIT_CARD, VALID_TOKEN, "")
            );
        }

        /**
         * Verifies that "0000" is accepted as valid lastFourDigits.
         */
        @Test
        @DisplayName("Should accept '0000' as valid lastFourDigits")
        void of_withZerosLastFour_shouldReturnPaymentMethod() {
            PaymentMethod result = PaymentMethod.of(
                    PaymentMethodType.CREDIT_CARD,
                    VALID_TOKEN,
                    "0000"
            );

            assertEquals("0000", result.getLastFourDigits());
        }

        /**
         * Verifies that "9999" is accepted as valid lastFourDigits.
         */
        @Test
        @DisplayName("Should accept '9999' as valid lastFourDigits")
        void of_withNinesLastFour_shouldReturnPaymentMethod() {
            PaymentMethod result = PaymentMethod.of(
                    PaymentMethodType.CREDIT_CARD,
                    VALID_TOKEN,
                    "9999"
            );

            assertEquals("9999", result.getLastFourDigits());
        }
    }

    // ==================== FACTORY METHOD: empty() - HIGH PRIORITY ====================

    @Nested
    @DisplayName("Factory Method: empty()")
    class EmptyFactoryTests {

        /**
         * Verifies that empty() creates a PaymentMethod with null values.
         */
        @Test
        @DisplayName("Should return PaymentMethod with null values")
        void empty_shouldReturnPaymentMethodWithNullValues() {
            PaymentMethod result = PaymentMethod.empty();

            assertNotNull(result);
            assertNull(result.getType());
            assertNull(result.getExternalToken());
            assertNull(result.getLastFourDigits());
        }

        /**
         * Verifies that isEmpty() returns true for empty instance.
         */
        @Test
        @DisplayName("isEmpty() should return true for empty instance")
        void isEmpty_whenEmpty_shouldReturnTrue() {
            PaymentMethod empty = PaymentMethod.empty();

            assertTrue(empty.isEmpty());
        }

        /**
         * Verifies that isEmpty() returns false for valid instance.
         */
        @Test
        @DisplayName("isEmpty() should return false for valid instance")
        void isEmpty_whenValid_shouldReturnFalse() {
            PaymentMethod valid = PaymentMethod.of(
                    PaymentMethodType.CREDIT_CARD,
                    VALID_TOKEN,
                    VALID_LAST_FOUR
            );

            assertFalse(valid.isEmpty());
        }
    }

    // ==================== QUERY METHODS - MEDIUM PRIORITY ====================

    @Nested
    @DisplayName("Query Methods: isCreditCard() and isDebitCard()")
    class CardTypeQueryTests {

        /**
         * Verifies that isCreditCard() returns true for CREDIT_CARD type.
         */
        @Test
        @DisplayName("isCreditCard() should return true for CREDIT_CARD")
        void isCreditCard_whenCreditCard_shouldReturnTrue() {
            PaymentMethod creditCard = PaymentMethod.of(
                    PaymentMethodType.CREDIT_CARD,
                    VALID_TOKEN,
                    VALID_LAST_FOUR
            );

            assertTrue(creditCard.isCreditCard());
        }

        /**
         * Verifies that isCreditCard() returns false for DEBIT_CARD type.
         */
        @Test
        @DisplayName("isCreditCard() should return false for DEBIT_CARD")
        void isCreditCard_whenDebitCard_shouldReturnFalse() {
            PaymentMethod debitCard = PaymentMethod.of(
                    PaymentMethodType.DEBIT_CARD,
                    VALID_TOKEN,
                    VALID_LAST_FOUR
            );

            assertFalse(debitCard.isCreditCard());
        }

        /**
         * Verifies that isCreditCard() returns false for empty instance.
         */
        @Test
        @DisplayName("isCreditCard() should return false for empty instance")
        void isCreditCard_whenEmpty_shouldReturnFalse() {
            PaymentMethod empty = PaymentMethod.empty();

            assertFalse(empty.isCreditCard());
        }

        /**
         * Verifies that isDebitCard() returns true for DEBIT_CARD type.
         */
        @Test
        @DisplayName("isDebitCard() should return true for DEBIT_CARD")
        void isDebitCard_whenDebitCard_shouldReturnTrue() {
            PaymentMethod debitCard = PaymentMethod.of(
                    PaymentMethodType.DEBIT_CARD,
                    VALID_TOKEN,
                    VALID_LAST_FOUR
            );

            assertTrue(debitCard.isDebitCard());
        }

        /**
         * Verifies that isDebitCard() returns false for CREDIT_CARD type.
         */
        @Test
        @DisplayName("isDebitCard() should return false for CREDIT_CARD")
        void isDebitCard_whenCreditCard_shouldReturnFalse() {
            PaymentMethod creditCard = PaymentMethod.of(
                    PaymentMethodType.CREDIT_CARD,
                    VALID_TOKEN,
                    VALID_LAST_FOUR
            );

            assertFalse(creditCard.isDebitCard());
        }

        /**
         * Verifies that isDebitCard() returns false for empty instance.
         */
        @Test
        @DisplayName("isDebitCard() should return false for empty instance")
        void isDebitCard_whenEmpty_shouldReturnFalse() {
            PaymentMethod empty = PaymentMethod.empty();

            assertFalse(empty.isDebitCard());
        }
    }

    // ==================== getMaskedDisplay() - MEDIUM PRIORITY ====================

    @Nested
    @DisplayName("Query Method: getMaskedDisplay()")
    class MaskedDisplayTests {

        /**
         * Verifies that getMaskedDisplay() returns correct format for CREDIT_CARD.
         */
        @Test
        @DisplayName("Should return 'CREDIT_CARD ****XXXX' format")
        void getMaskedDisplay_forCreditCard_shouldReturnCorrectFormat() {
            PaymentMethod creditCard = PaymentMethod.of(
                    PaymentMethodType.CREDIT_CARD,
                    VALID_TOKEN,
                    "4242"
            );

            assertEquals("CREDIT_CARD ****4242", creditCard.getMaskedDisplay());
        }

        /**
         * Verifies that getMaskedDisplay() returns correct format for DEBIT_CARD.
         */
        @Test
        @DisplayName("Should return 'DEBIT_CARD ****XXXX' format")
        void getMaskedDisplay_forDebitCard_shouldReturnCorrectFormat() {
            PaymentMethod debitCard = PaymentMethod.of(
                    PaymentMethodType.DEBIT_CARD,
                    VALID_TOKEN,
                    "1234"
            );

            assertEquals("DEBIT_CARD ****1234", debitCard.getMaskedDisplay());
        }

        /**
         * Verifies that getMaskedDisplay() returns 'No payment method' for empty.
         */
        @Test
        @DisplayName("Should return 'No payment method' for empty instance")
        void getMaskedDisplay_forEmpty_shouldReturnNoPaymentMethod() {
            PaymentMethod empty = PaymentMethod.empty();

            assertEquals("No payment method", empty.getMaskedDisplay());
        }

        /**
         * Verifies that toString() delegates to getMaskedDisplay().
         */
        @Test
        @DisplayName("toString() should return same as getMaskedDisplay()")
        void toString_shouldReturnMaskedDisplay() {
            PaymentMethod creditCard = PaymentMethod.of(
                    PaymentMethodType.CREDIT_CARD,
                    VALID_TOKEN,
                    "5678"
            );

            assertEquals(creditCard.getMaskedDisplay(), creditCard.toString());
        }
    }

    // ==================== EQUALITY CONTRACT - MEDIUM PRIORITY ====================

    @Nested
    @DisplayName("Equality Contract: equals() and hashCode()")
    class EqualityTests {

        /**
         * Verifies that two PaymentMethods with same type and token are equal.
         */
        @Test
        @DisplayName("equals() should return true for same type and token")
        void equals_withSameTypeAndToken_shouldReturnTrue() {
            PaymentMethod pm1 = PaymentMethod.of(
                    PaymentMethodType.CREDIT_CARD,
                    "tok_123",
                    "4242"
            );
            PaymentMethod pm2 = PaymentMethod.of(
                    PaymentMethodType.CREDIT_CARD,
                    "tok_123",
                    "4242"
            );

            assertEquals(pm1, pm2);
        }

        /**
         * Verifies that PaymentMethods with different types are not equal.
         */
        @Test
        @DisplayName("equals() should return false for different types")
        void equals_withDifferentType_shouldReturnFalse() {
            PaymentMethod creditCard = PaymentMethod.of(
                    PaymentMethodType.CREDIT_CARD,
                    "tok_123",
                    "4242"
            );
            PaymentMethod debitCard = PaymentMethod.of(
                    PaymentMethodType.DEBIT_CARD,
                    "tok_123",
                    "4242"
            );

            assertNotEquals(creditCard, debitCard);
        }

        /**
         * Verifies that PaymentMethods with different tokens are not equal.
         */
        @Test
        @DisplayName("equals() should return false for different tokens")
        void equals_withDifferentToken_shouldReturnFalse() {
            PaymentMethod pm1 = PaymentMethod.of(
                    PaymentMethodType.CREDIT_CARD,
                    "tok_123",
                    "4242"
            );
            PaymentMethod pm2 = PaymentMethod.of(
                    PaymentMethodType.CREDIT_CARD,
                    "tok_456",
                    "4242"
            );

            assertNotEquals(pm1, pm2);
        }

        /**
         * Verifies that equals() returns false when compared with null.
         */
        @Test
        @DisplayName("equals() should return false when compared with null")
        void equals_withNull_shouldReturnFalse() {
            PaymentMethod pm = PaymentMethod.of(
                    PaymentMethodType.CREDIT_CARD,
                    VALID_TOKEN,
                    VALID_LAST_FOUR
            );

            assertNotEquals(null, pm);
        }

        /**
         * Verifies that equals() returns false when compared with different type.
         */
        @Test
        @DisplayName("equals() should return false when compared with different class")
        void equals_withDifferentClass_shouldReturnFalse() {
            PaymentMethod pm = PaymentMethod.of(
                    PaymentMethodType.CREDIT_CARD,
                    VALID_TOKEN,
                    VALID_LAST_FOUR
            );

            assertNotEquals("not a payment method", pm);
        }

        /**
         * Verifies reflexivity: an object must equal itself.
         */
        @Test
        @DisplayName("equals() should be reflexive")
        void equals_reflexive_shouldReturnTrue() {
            PaymentMethod pm = PaymentMethod.of(
                    PaymentMethodType.CREDIT_CARD,
                    VALID_TOKEN,
                    VALID_LAST_FOUR
            );

            assertEquals(pm, pm);
        }

        /**
         * Verifies that two empty instances are equal.
         */
        @Test
        @DisplayName("Two empty instances should be equal")
        void equals_twoEmptyInstances_shouldReturnTrue() {
            PaymentMethod empty1 = PaymentMethod.empty();
            PaymentMethod empty2 = PaymentMethod.empty();

            assertEquals(empty1, empty2);
        }

        /**
         * Verifies that equal objects have the same hashCode.
         */
        @Test
        @DisplayName("hashCode() should be same for equal objects")
        void hashCode_equalObjects_shouldReturnSameHash() {
            PaymentMethod pm1 = PaymentMethod.of(
                    PaymentMethodType.CREDIT_CARD,
                    "tok_123",
                    "4242"
            );
            PaymentMethod pm2 = PaymentMethod.of(
                    PaymentMethodType.CREDIT_CARD,
                    "tok_123",
                    "4242"
            );

            assertEquals(pm1.hashCode(), pm2.hashCode());
        }

        /**
         * Verifies hashCode consistency across multiple calls.
         */
        @Test
        @DisplayName("hashCode() should be consistent across multiple calls")
        void hashCode_multipleCalls_shouldBeConsistent() {
            PaymentMethod pm = PaymentMethod.of(
                    PaymentMethodType.CREDIT_CARD,
                    VALID_TOKEN,
                    VALID_LAST_FOUR
            );

            int hash1 = pm.hashCode();
            int hash2 = pm.hashCode();

            assertEquals(hash1, hash2);
        }

        /**
         * Verifies that empty instances have consistent hashCode.
         */
        @Test
        @DisplayName("hashCode() should be same for two empty instances")
        void hashCode_twoEmptyInstances_shouldReturnSameHash() {
            PaymentMethod empty1 = PaymentMethod.empty();
            PaymentMethod empty2 = PaymentMethod.empty();

            assertEquals(empty1.hashCode(), empty2.hashCode());
        }
    }
}

