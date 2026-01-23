package com.apis.fintrack.domain.shared;

import com.apis.fintrack.domain.shared.model.Money;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Currency;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link Money} value object.
 *
 * <p>Tests cover factory methods, arithmetic operations, comparison methods,
 * and equality contract following specification-based testing approaches.</p>
 */
@DisplayName("Money Value Object Tests")
class MoneyTest {

    private static final Currency EUR = Currency.getInstance("EUR");
    private static final Currency USD = Currency.getInstance("USD");

    // ==================== FACTORY METHODS - HIGH PRIORITY ====================

    @Nested
    @DisplayName("Factory Method: of(BigDecimal)")
    class OfBigDecimalTests {

        /**
         * Verifies that the factory method creates a valid Money instance
         * with the default currency (EUR).
         */
        @Test
        @DisplayName("Should create Money with default currency EUR")
        void of_withValidAmount_shouldCreateMoneyWithEUR() {
            Money money = Money.of(new BigDecimal("100.00"));

            assertNotNull(money);
            assertEquals(new BigDecimal("100.00"), money.getAmount());
            assertEquals(EUR, money.getCurrency());
        }

        /**
         * Verifies that amounts are rounded to 2 decimal places.
         */
        @Test
        @DisplayName("Should round to 2 decimal places")
        void of_withManyDecimals_shouldRoundTo2Decimals() {
            Money money = Money.of(new BigDecimal("100.456"));

            assertEquals(new BigDecimal("100.46"), money.getAmount());
        }

        /**
         * Verifies that null amount throws IllegalArgumentException.
         */
        @Test
        @DisplayName("Should throw IllegalArgumentException when amount is null")
        void of_withNullAmount_shouldThrowIllegalArgumentException() {
            assertThrows(
                    IllegalArgumentException.class,
                    () -> Money.of((BigDecimal) null)
            );
        }

        /**
         * Verifies that negative amounts are accepted.
         */
        @Test
        @DisplayName("Should accept negative amounts")
        void of_withNegativeAmount_shouldCreateMoney() {
            Money money = Money.of(new BigDecimal("-50.00"));

            assertEquals(new BigDecimal("-50.00"), money.getAmount());
            assertTrue(money.isNegative());
        }

        /**
         * Verifies that zero amount is accepted.
         */
        @Test
        @DisplayName("Should accept zero amount")
        void of_withZeroAmount_shouldCreateMoney() {
            Money money = Money.of(BigDecimal.ZERO);

            assertTrue(money.isZero());
        }
    }

    @Nested
    @DisplayName("Factory Method: of(BigDecimal, Currency)")
    class OfBigDecimalCurrencyTests {

        /**
         * Verifies that the factory method creates Money with specific currency.
         */
        @Test
        @DisplayName("Should create Money with specified currency")
        void of_withCurrency_shouldCreateMoneyWithCurrency() {
            Money money = Money.of(new BigDecimal("100.00"), USD);

            assertEquals(USD, money.getCurrency());
        }

        /**
         * Verifies that null currency throws IllegalArgumentException.
         */
        @Test
        @DisplayName("Should throw IllegalArgumentException when currency is null")
        void of_withNullCurrency_shouldThrowIllegalArgumentException() {
            assertThrows(
                    IllegalArgumentException.class,
                    () -> Money.of(new BigDecimal("100.00"), null)
            );
        }
    }

    @Nested
    @DisplayName("Factory Method: of(double)")
    class OfDoubleTests {

        /**
         * Verifies that the factory method creates Money from double.
         */
        @Test
        @DisplayName("Should create Money from double")
        void of_withDouble_shouldCreateMoney() {
            Money money = Money.of(99.99);

            assertEquals(new BigDecimal("99.99"), money.getAmount());
        }

        /**
         * Verifies that double precision is handled correctly.
         */
        @Test
        @DisplayName("Should handle double precision correctly")
        void of_withDoublePrecision_shouldRoundCorrectly() {
            Money money = Money.of(10.005);

            // Double.valueOf rounds differently, check scale
            assertEquals(2, money.getAmount().scale());
        }
    }

    @Nested
    @DisplayName("Factory Method: zero()")
    class ZeroTests {

        /**
         * Verifies that zero() creates Money with zero amount.
         */
        @Test
        @DisplayName("Should create Money with zero amount")
        void zero_shouldCreateMoneyWithZeroAmount() {
            Money money = Money.zero();

            assertTrue(money.isZero());
            assertEquals(new BigDecimal("0.00"), money.getAmount());
            assertEquals(EUR, money.getCurrency());
        }

        /**
         * Verifies that zero(Currency) creates zero Money with specific currency.
         */
        @Test
        @DisplayName("Should create zero Money with specified currency")
        void zero_withCurrency_shouldCreateZeroMoneyWithCurrency() {
            Money money = Money.zero(USD);

            assertTrue(money.isZero());
            assertEquals(USD, money.getCurrency());
        }
    }

    // ==================== ARITHMETIC OPERATIONS - HIGH PRIORITY ====================

    @Nested
    @DisplayName("Arithmetic: add()")
    class AddTests {

        /**
         * Verifies that add() returns correct sum.
         */
        @Test
        @DisplayName("Should return correct sum of two Money values")
        void add_twoMoneyValues_shouldReturnSum() {
            Money m1 = Money.of(new BigDecimal("100.00"));
            Money m2 = Money.of(new BigDecimal("50.00"));

            Money result = m1.add(m2);

            assertEquals(new BigDecimal("150.00"), result.getAmount());
        }

        /**
         * Verifies that adding zero returns equivalent value.
         */
        @Test
        @DisplayName("Should return same value when adding zero")
        void add_zero_shouldReturnSameValue() {
            Money m1 = Money.of(new BigDecimal("100.00"));
            Money zero = Money.zero();

            Money result = m1.add(zero);

            assertEquals(m1.getAmount(), result.getAmount());
        }

        /**
         * Verifies that adding negative value works correctly.
         */
        @Test
        @DisplayName("Should handle adding negative values")
        void add_negativeValue_shouldSubtract() {
            Money m1 = Money.of(new BigDecimal("100.00"));
            Money m2 = Money.of(new BigDecimal("-30.00"));

            Money result = m1.add(m2);

            assertEquals(new BigDecimal("70.00"), result.getAmount());
        }

        /**
         * Verifies that adding different currencies throws exception.
         */
        @Test
        @DisplayName("Should throw exception when adding different currencies")
        void add_differentCurrencies_shouldThrowException() {
            Money eur = Money.of(new BigDecimal("100.00"), EUR);
            Money usd = Money.of(new BigDecimal("50.00"), USD);

            assertThrows(IllegalArgumentException.class, () -> eur.add(usd));
        }

        /**
         * Verifies immutability: original values unchanged after add.
         */
        @Test
        @DisplayName("Should not modify original Money instances")
        void add_shouldBeImmutable() {
            Money m1 = Money.of(new BigDecimal("100.00"));
            Money m2 = Money.of(new BigDecimal("50.00"));

            m1.add(m2);

            assertEquals(new BigDecimal("100.00"), m1.getAmount());
            assertEquals(new BigDecimal("50.00"), m2.getAmount());
        }
    }

    @Nested
    @DisplayName("Arithmetic: subtract()")
    class SubtractTests {

        /**
         * Verifies that subtract() returns correct difference.
         */
        @Test
        @DisplayName("Should return correct difference of two Money values")
        void subtract_twoMoneyValues_shouldReturnDifference() {
            Money m1 = Money.of(new BigDecimal("100.00"));
            Money m2 = Money.of(new BigDecimal("30.00"));

            Money result = m1.subtract(m2);

            assertEquals(new BigDecimal("70.00"), result.getAmount());
        }

        /**
         * Verifies that subtracting larger value results in negative.
         */
        @Test
        @DisplayName("Should return negative when subtracting larger value")
        void subtract_largerValue_shouldReturnNegative() {
            Money m1 = Money.of(new BigDecimal("50.00"));
            Money m2 = Money.of(new BigDecimal("100.00"));

            Money result = m1.subtract(m2);

            assertTrue(result.isNegative());
            assertEquals(new BigDecimal("-50.00"), result.getAmount());
        }

        /**
         * Verifies that subtracting different currencies throws exception.
         */
        @Test
        @DisplayName("Should throw exception when subtracting different currencies")
        void subtract_differentCurrencies_shouldThrowException() {
            Money eur = Money.of(new BigDecimal("100.00"), EUR);
            Money usd = Money.of(new BigDecimal("50.00"), USD);

            assertThrows(IllegalArgumentException.class, () -> eur.subtract(usd));
        }
    }

    @Nested
    @DisplayName("Arithmetic: multiply()")
    class MultiplyTests {

        /**
         * Verifies that multiply() returns correct product.
         */
        @Test
        @DisplayName("Should return correct product")
        void multiply_byFactor_shouldReturnProduct() {
            Money money = Money.of(new BigDecimal("100.00"));

            Money result = money.multiply(new BigDecimal("2.5"));

            assertEquals(new BigDecimal("250.00"), result.getAmount());
        }

        /**
         * Verifies that multiplying by zero returns zero.
         */
        @Test
        @DisplayName("Should return zero when multiplying by zero")
        void multiply_byZero_shouldReturnZero() {
            Money money = Money.of(new BigDecimal("100.00"));

            Money result = money.multiply(BigDecimal.ZERO);

            assertTrue(result.isZero());
        }

        /**
         * Verifies that multiplying by negative factor negates value.
         */
        @Test
        @DisplayName("Should negate when multiplying by negative factor")
        void multiply_byNegative_shouldNegate() {
            Money money = Money.of(new BigDecimal("100.00"));

            Money result = money.multiply(new BigDecimal("-1"));

            assertEquals(new BigDecimal("-100.00"), result.getAmount());
        }

        /**
         * Verifies that multiply(int) works correctly.
         */
        @Test
        @DisplayName("Should multiply by integer factor")
        void multiply_byInt_shouldReturnProduct() {
            Money money = Money.of(new BigDecimal("50.00"));

            Money result = money.multiply(3);

            assertEquals(new BigDecimal("150.00"), result.getAmount());
        }
    }

    @Nested
    @DisplayName("Arithmetic: divide()")
    class DivideTests {

        /**
         * Verifies that divide() returns correct quotient.
         */
        @Test
        @DisplayName("Should return correct quotient")
        void divide_byDivisor_shouldReturnQuotient() {
            Money money = Money.of(new BigDecimal("100.00"));

            Money result = money.divide(new BigDecimal("4"));

            assertEquals(new BigDecimal("25.00"), result.getAmount());
        }

        /**
         * Verifies that dividing by zero throws ArithmeticException.
         */
        @Test
        @DisplayName("Should throw ArithmeticException when dividing by zero")
        void divide_byZero_shouldThrowArithmeticException() {
            Money money = Money.of(new BigDecimal("100.00"));

            assertThrows(ArithmeticException.class, () -> money.divide(BigDecimal.ZERO));
        }

        /**
         * Verifies that division rounds to 2 decimal places.
         */
        @Test
        @DisplayName("Should round to 2 decimal places")
        void divide_withRemainder_shouldRound() {
            Money money = Money.of(new BigDecimal("100.00"));

            Money result = money.divide(new BigDecimal("3"));

            assertEquals(new BigDecimal("33.33"), result.getAmount());
        }
    }

    // ==================== COMPARISON METHODS - MEDIUM PRIORITY ====================

    @Nested
    @DisplayName("Comparison Methods")
    class ComparisonTests {

        /**
         * Verifies isPositive() returns true for positive amounts.
         */
        @Test
        @DisplayName("isPositive() should return true for positive amounts")
        void isPositive_positiveAmount_shouldReturnTrue() {
            Money money = Money.of(new BigDecimal("100.00"));

            assertTrue(money.isPositive());
        }

        /**
         * Verifies isPositive() returns false for zero.
         */
        @Test
        @DisplayName("isPositive() should return false for zero")
        void isPositive_zero_shouldReturnFalse() {
            Money money = Money.zero();

            assertFalse(money.isPositive());
        }

        /**
         * Verifies isNegative() returns true for negative amounts.
         */
        @Test
        @DisplayName("isNegative() should return true for negative amounts")
        void isNegative_negativeAmount_shouldReturnTrue() {
            Money money = Money.of(new BigDecimal("-50.00"));

            assertTrue(money.isNegative());
        }

        /**
         * Verifies isZero() returns true for zero amount.
         */
        @Test
        @DisplayName("isZero() should return true for zero amount")
        void isZero_zeroAmount_shouldReturnTrue() {
            Money money = Money.zero();

            assertTrue(money.isZero());
        }

        /**
         * Verifies isGreaterThan() comparison.
         */
        @Test
        @DisplayName("isGreaterThan() should return true when amount is greater")
        void isGreaterThan_greaterAmount_shouldReturnTrue() {
            Money m1 = Money.of(new BigDecimal("100.00"));
            Money m2 = Money.of(new BigDecimal("50.00"));

            assertTrue(m1.isGreaterThan(m2));
            assertFalse(m2.isGreaterThan(m1));
        }

        /**
         * Verifies isLessThan() comparison.
         */
        @Test
        @DisplayName("isLessThan() should return true when amount is less")
        void isLessThan_lessAmount_shouldReturnTrue() {
            Money m1 = Money.of(new BigDecimal("50.00"));
            Money m2 = Money.of(new BigDecimal("100.00"));

            assertTrue(m1.isLessThan(m2));
            assertFalse(m2.isLessThan(m1));
        }

        /**
         * Verifies isGreaterThanOrEqual() comparison.
         */
        @Test
        @DisplayName("isGreaterThanOrEqual() should return true when amount is greater or equal")
        void isGreaterThanOrEqual_greaterOrEqualAmount_shouldReturnTrue() {
            Money m1 = Money.of(new BigDecimal("100.00"));
            Money m2 = Money.of(new BigDecimal("100.00"));
            Money m3 = Money.of(new BigDecimal("50.00"));

            assertTrue(m1.isGreaterThanOrEqual(m2));
            assertTrue(m1.isGreaterThanOrEqual(m3));
            assertFalse(m3.isGreaterThanOrEqual(m1));
        }

        /**
         * Verifies comparison with different currencies throws exception.
         */
        @Test
        @DisplayName("Comparison with different currencies should throw exception")
        void comparison_differentCurrencies_shouldThrowException() {
            Money eur = Money.of(new BigDecimal("100.00"), EUR);
            Money usd = Money.of(new BigDecimal("100.00"), USD);

            assertThrows(IllegalArgumentException.class, () -> eur.isGreaterThan(usd));
            assertThrows(IllegalArgumentException.class, () -> eur.isLessThan(usd));
        }
    }

    // ==================== UTILITY METHODS - MEDIUM PRIORITY ====================

    @Nested
    @DisplayName("Utility Methods")
    class UtilityTests {

        /**
         * Verifies abs() returns absolute value.
         */
        @Test
        @DisplayName("abs() should return absolute value")
        void abs_negativeAmount_shouldReturnPositive() {
            Money negative = Money.of(new BigDecimal("-100.00"));

            Money result = negative.abs();

            assertEquals(new BigDecimal("100.00"), result.getAmount());
            assertTrue(result.isPositive());
        }

        /**
         * Verifies abs() on positive returns same value.
         */
        @Test
        @DisplayName("abs() on positive should return same value")
        void abs_positiveAmount_shouldReturnSameValue() {
            Money positive = Money.of(new BigDecimal("100.00"));

            Money result = positive.abs();

            assertEquals(positive.getAmount(), result.getAmount());
        }

        /**
         * Verifies negate() changes sign.
         */
        @Test
        @DisplayName("negate() should change sign")
        void negate_shouldChangeSign() {
            Money positive = Money.of(new BigDecimal("100.00"));
            Money negative = Money.of(new BigDecimal("-50.00"));

            assertEquals(new BigDecimal("-100.00"), positive.negate().getAmount());
            assertEquals(new BigDecimal("50.00"), negative.negate().getAmount());
        }
    }

    // ==================== EQUALITY CONTRACT - MEDIUM PRIORITY ====================

    @Nested
    @DisplayName("Equality Contract: equals() and hashCode()")
    class EqualityTests {

        /**
         * Verifies that two Money with same amount and currency are equal.
         */
        @Test
        @DisplayName("equals() should return true for same amount and currency")
        void equals_sameAmountAndCurrency_shouldReturnTrue() {
            Money m1 = Money.of(new BigDecimal("100.00"), EUR);
            Money m2 = Money.of(new BigDecimal("100.00"), EUR);

            assertEquals(m1, m2);
        }

        /**
         * Verifies that Money with different amounts are not equal.
         */
        @Test
        @DisplayName("equals() should return false for different amounts")
        void equals_differentAmount_shouldReturnFalse() {
            Money m1 = Money.of(new BigDecimal("100.00"));
            Money m2 = Money.of(new BigDecimal("50.00"));

            assertNotEquals(m1, m2);
        }

        /**
         * Verifies that Money with different currencies are not equal.
         */
        @Test
        @DisplayName("equals() should return false for different currencies")
        void equals_differentCurrency_shouldReturnFalse() {
            Money eur = Money.of(new BigDecimal("100.00"), EUR);
            Money usd = Money.of(new BigDecimal("100.00"), USD);

            assertNotEquals(eur, usd);
        }

        /**
         * Verifies equals with null returns false.
         */
        @Test
        @DisplayName("equals() should return false when compared with null")
        void equals_withNull_shouldReturnFalse() {
            Money money = Money.of(new BigDecimal("100.00"));

            assertNotEquals(null, money);
        }

        /**
         * Verifies reflexivity.
         */
        @Test
        @DisplayName("equals() should be reflexive")
        void equals_reflexive_shouldReturnTrue() {
            Money money = Money.of(new BigDecimal("100.00"));

            assertEquals(money, money);
        }

        /**
         * Verifies that equal objects have same hashCode.
         */
        @Test
        @DisplayName("hashCode() should be same for equal objects")
        void hashCode_equalObjects_shouldReturnSameHash() {
            Money m1 = Money.of(new BigDecimal("100.00"), EUR);
            Money m2 = Money.of(new BigDecimal("100.00"), EUR);

            assertEquals(m1.hashCode(), m2.hashCode());
        }

        /**
         * Verifies hashCode consistency.
         */
        @Test
        @DisplayName("hashCode() should be consistent")
        void hashCode_multipleCalls_shouldBeConsistent() {
            Money money = Money.of(new BigDecimal("100.00"));

            int hash1 = money.hashCode();
            int hash2 = money.hashCode();

            assertEquals(hash1, hash2);
        }
    }

    // ==================== toString() - MEDIUM PRIORITY ====================

    @Nested
    @DisplayName("Method: toString() and toFormattedString()")
    class ToStringTests {

        /**
         * Verifies that toString() returns amount with currency code.
         */
        @Test
        @DisplayName("toString() should return amount with currency code")
        void toString_shouldReturnAmountWithCurrencyCode() {
            Money money = Money.of(new BigDecimal("100.00"), EUR);

            String result = money.toString();

            assertTrue(result.contains("100.00"));
            assertTrue(result.contains("EUR"));
        }

        /**
         * Verifies that toFormattedString() returns amount with currency symbol.
         */
        @Test
        @DisplayName("toFormattedString() should return amount with currency symbol")
        void toFormattedString_shouldReturnAmountWithSymbol() {
            Money money = Money.of(new BigDecimal("100.00"), EUR);

            String result = money.toFormattedString();

            assertTrue(result.contains("100.00"));
            // EUR symbol may vary by locale, just check it's not empty
            assertFalse(result.isEmpty());
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
        void of_veryLargeAmount_shouldCreateMoney() {
            BigDecimal largeAmount = new BigDecimal("999999999999999.99");

            Money money = Money.of(largeAmount);

            assertEquals(largeAmount, money.getAmount());
        }

        /**
         * Verifies handling of very small amounts.
         */
        @Test
        @DisplayName("Should handle very small amounts")
        void of_verySmallAmount_shouldCreateMoney() {
            BigDecimal smallAmount = new BigDecimal("0.01");

            Money money = Money.of(smallAmount);

            assertEquals(smallAmount, money.getAmount());
        }

        /**
         * Verifies that amounts with different scales but same value are equal.
         */
        @Test
        @DisplayName("Amounts with different scales but same value should be equal")
        void equals_differentScalesSameValue_shouldBeEqual() {
            Money m1 = Money.of(new BigDecimal("100.00"));
            Money m2 = Money.of(new BigDecimal("100"));

            assertEquals(m1, m2);
        }
    }
}

