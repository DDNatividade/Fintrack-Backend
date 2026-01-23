package com.apis.fintrack.domain.budget.model;

import com.apis.fintrack.domain.shared.model.Money;
import com.apis.fintrack.domain.transaction.model.TransactionCategoryEnum;
import com.apis.fintrack.domain.user.model.UserId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for Budget Aggregate Root.
 *
 * Tests follow specification-based testing approach, covering:
 * - Constructor invariant validation
 * - Domain behaviors (changeLimit, changeCategory, isExceeded, etc.)
 * - Lifecycle methods (renewBudget, deactivateBudget)
 * - Equality and hashCode contracts
 *
 * Budget is an Aggregate Root that represents a spending limit configuration
 * for a user, optionally scoped to a specific category.
 *
 * Key design decisions verified by tests:
 * - amountSpent is NOT stored in Budget (provided externally)
 * - equals() includes budgetID, userId, limit, period, category but NOT state
 * - isExceeded() returns true when amountSpent >= limit (boundary inclusive)
 * - getUsagePercentage() is capped at 100%
 */
@DisplayName("Budget Aggregate Root Tests")
class BudgetTest {

    private BudgetID validBudgetID;
    private UserId validUserId;
    private Money validLimit;
    private TransactionCategoryEnum validCategory;

    @BeforeEach
    void setUp() {
        validBudgetID = BudgetID.of(1L);
        validUserId = UserId.of(100L);
        validLimit = Money.of(new BigDecimal("500.00"));
        validCategory = TransactionCategoryEnum.ALIMENTATION;
    }

    // ========================================
    // PRIORITY CRITICAL - Constructor Invariants
    // ========================================

    @Nested
    @DisplayName("Constructor - Invariant Validation")
    class ConstructorInvariantTests {

        /**
         * Verifies that creating a Budget with null userId throws NullPointerException.
         * <p>
         * Business Rule: Every budget must belong to a user.
         * <p>
         * Bug it could reveal: Missing userId validation.
         */
        @Test
        @DisplayName("constructor with null userId throws NullPointerException")
        void constructor_nullUserId_throwsNPE() {
            NullPointerException exception = assertThrows(
                    NullPointerException.class,
                    () -> new Budget(validBudgetID, null, validLimit, validCategory)
            );

            assertEquals("userId cannot be null", exception.getMessage(),
                    "Exception message should indicate null userId");
        }

        /**
         * Verifies that creating a Budget with null limit throws NullPointerException.
         * <p>
         * Business Rule: Every budget must have a defined spending limit.
         * <p>
         * Bug it could reveal: Missing limit validation.
         */
        @Test
        @DisplayName("constructor with null limit throws NullPointerException")
        void constructor_nullLimit_throwsNPE() {
            NullPointerException exception = assertThrows(
                    NullPointerException.class,
                    () -> new Budget(validBudgetID, validUserId, null, validCategory)
            );

            assertEquals("limit cannot be null", exception.getMessage(),
                    "Exception message should indicate null limit");
        }

        /**
         * Verifies that creating a Budget with zero limit throws IllegalArgumentException.
         * <p>
         * Business Rule: A budget limit must be positive (greater than zero).
         * Zero is not a meaningful limit.
         * <p>
         * Bug it could reveal: Zero accepted as valid limit.
         */
        @Test
        @DisplayName("constructor with zero limit throws IllegalArgumentException")
        void constructor_zeroLimit_throwsIllegalArgument() {
            Money zeroLimit = Money.of(BigDecimal.ZERO);

            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> new Budget(validBudgetID, validUserId, zeroLimit, validCategory)
            );

            assertEquals("limit must be greater than zero", exception.getMessage(),
                    "Exception message should indicate limit must be positive");
        }

        /**
         * Verifies that creating a Budget with negative limit throws IllegalArgumentException.
         * <p>
         * Business Rule: A budget limit cannot be negative.
         * <p>
         * Bug it could reveal: Negative values accepted as limit.
         */
        @Test
        @DisplayName("constructor with negative limit throws IllegalArgumentException")
        void constructor_negativeLimit_throwsIllegalArgument() {
            Money negativeLimit = Money.of(new BigDecimal("-100.00"));

            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> new Budget(validBudgetID, validUserId, negativeLimit, validCategory)
            );

            assertEquals("limit must be greater than zero", exception.getMessage(),
                    "Exception message should indicate limit must be positive");
        }

        /**
         * Verifies that creating a Budget with category OTHER throws IllegalArgumentException.
         * <p>
         * Business Rule: OTHER is a catch-all category for transactions but is not
         * meaningful as a budget category. Budgets should track specific categories.
         * <p>
         * Bug it could reveal: OTHER category accepted for budgets.
         */
        @Test
        @DisplayName("constructor with category OTHER throws IllegalArgumentException")
        void constructor_categoryOTHER_throwsIllegalArgument() {
            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> new Budget(validBudgetID, validUserId, validLimit, TransactionCategoryEnum.OTHER)
            );

            assertEquals("Category OTHER is not allowed for budgets", exception.getMessage(),
                    "Exception message should indicate OTHER is not allowed");
        }

        /**
         * Verifies that a Budget is created successfully with all valid parameters.
         * <p>
         * This is the primary happy path for Budget creation.
         * <p>
         * Bug it could reveal: Valid parameters incorrectly rejected.
         */
        @Test
        @DisplayName("constructor with valid params creates Budget")
        void constructor_validParams_createsBudget() {
            Budget budget = new Budget(validBudgetID, validUserId, validLimit, validCategory);

            assertNotNull(budget, "Budget should not be null");
            assertEquals(validBudgetID, budget.getBudgetID(), "BudgetID should match");
            assertEquals(validUserId, budget.getUserId(), "UserId should match");
            assertEquals(validLimit, budget.getLimit(), "Limit should match");
            assertEquals(validCategory, budget.getCategory(), "Category should match");
        }

        /**
         * Verifies that null category is allowed (budget applies to all categories).
         * <p>
         * Business Rule: A budget with null category applies globally to all spending.
         * <p>
         * Bug it could reveal: Null category incorrectly rejected.
         */
        @Test
        @DisplayName("constructor with null category is allowed")
        void constructor_nullCategory_allowed() {
            Budget budget = new Budget(validBudgetID, validUserId, validLimit, null);

            assertNotNull(budget, "Budget should be created");
            assertNull(budget.getCategory(), "Category should be null");
        }

        /**
         * Verifies that a new Budget starts with ACTIVE state.
         * <p>
         * Business Rule: All new budgets start as active.
         * <p>
         * Bug it could reveal: Incorrect initial state.
         */
        @Test
        @DisplayName("constructor sets initial state to ACTIVE")
        void constructor_setsInitialStateActive() {
            Budget budget = new Budget(validBudgetID, validUserId, validLimit, validCategory);

            assertEquals(BudgetState.ACTIVE, budget.getState(),
                    "New budget should be in ACTIVE state");
        }
    }

    // ========================================
    // PRIORITY CRITICAL - changeLimit()
    // ========================================

    @Nested
    @DisplayName("changeLimit() - Limit Modification")
    class ChangeLimitTests {

        /**
         * Verifies that changeLimit with null throws NullPointerException.
         * <p>
         * Bug it could reveal: Missing null validation in changeLimit.
         */
        @Test
        @DisplayName("changeLimit with null throws NullPointerException")
        void changeLimit_nullLimit_throwsNPE() {
            Budget budget = new Budget(validBudgetID, validUserId, validLimit, validCategory);

            NullPointerException exception = assertThrows(
                    NullPointerException.class,
                    () -> budget.changeLimit(null)
            );

            assertEquals("newLimit cannot be null", exception.getMessage(),
                    "Exception message should indicate null newLimit");
        }

        /**
         * Verifies that changeLimit with zero throws IllegalArgumentException.
         * <p>
         * Bug it could reveal: Zero accepted when changing limit.
         */
        @Test
        @DisplayName("changeLimit with zero throws IllegalArgumentException")
        void changeLimit_zeroLimit_throwsIllegalArgument() {
            Budget budget = new Budget(validBudgetID, validUserId, validLimit, validCategory);
            Money zeroLimit = Money.of(BigDecimal.ZERO);

            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> budget.changeLimit(zeroLimit)
            );

            assertEquals("limit must be greater than zero", exception.getMessage(),
                    "Exception message should indicate limit must be positive");
        }

        /**
         * Verifies that changeLimit with negative throws IllegalArgumentException.
         * <p>
         * Bug it could reveal: Negative values accepted when changing limit.
         */
        @Test
        @DisplayName("changeLimit with negative throws IllegalArgumentException")
        void changeLimit_negativeLimit_throwsIllegalArgument() {
            Budget budget = new Budget(validBudgetID, validUserId, validLimit, validCategory);
            Money negativeLimit = Money.of(new BigDecimal("-50.00"));

            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> budget.changeLimit(negativeLimit)
            );

            assertEquals("limit must be greater than zero", exception.getMessage(),
                    "Exception message should indicate limit must be positive");
        }

        /**
         * Verifies that changeLimit with valid value updates the limit.
         * <p>
         * Bug it could reveal: Limit not updated after valid change.
         */
        @Test
        @DisplayName("changeLimit with valid limit updates the limit")
        void changeLimit_validLimit_updatesLimit() {
            Budget budget = new Budget(validBudgetID, validUserId, validLimit, validCategory);
            Money newLimit = Money.of(new BigDecimal("1000.00"));

            budget.changeLimit(newLimit);

            assertEquals(newLimit, budget.getLimit(),
                    "Limit should be updated to new value");
        }
    }

    // ========================================
    // PRIORITY CRITICAL - changeCategory()
    // ========================================

    @Nested
    @DisplayName("changeCategory() - Category Modification")
    class ChangeCategoryTests {

        /**
         * Verifies that changeCategory with null throws NullPointerException.
         * <p>
         * Note: Unlike constructor, changeCategory does not allow null
         * because changing to "no category" should be an explicit action.
         * <p>
         * Bug it could reveal: Missing null validation in changeCategory.
         */
        @Test
        @DisplayName("changeCategory with null throws NullPointerException")
        void changeCategory_nullCategory_throwsNPE() {
            Budget budget = new Budget(validBudgetID, validUserId, validLimit, validCategory);

            NullPointerException exception = assertThrows(
                    NullPointerException.class,
                    () -> budget.changeCategory(null)
            );

            assertEquals("newCategory cannot be null", exception.getMessage(),
                    "Exception message should indicate null newCategory");
        }

        /**
         * Verifies that changeCategory to OTHER throws IllegalArgumentException.
         * <p>
         * Bug it could reveal: OTHER category accepted when changing.
         */
        @Test
        @DisplayName("changeCategory to OTHER throws IllegalArgumentException")
        void changeCategory_categoryOTHER_throwsIllegalArgument() {
            Budget budget = new Budget(validBudgetID, validUserId, validLimit, validCategory);

            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    () -> budget.changeCategory(TransactionCategoryEnum.OTHER)
            );

            assertEquals("Category OTHER is not allowed for budgets", exception.getMessage(),
                    "Exception message should indicate OTHER is not allowed");
        }

        /**
         * Verifies that changeCategory with valid category updates the category.
         * <p>
         * Bug it could reveal: Category not updated after valid change.
         */
        @Test
        @DisplayName("changeCategory with valid category updates the category")
        void changeCategory_validCategory_updatesCategory() {
            Budget budget = new Budget(validBudgetID, validUserId, validLimit, validCategory);
            TransactionCategoryEnum newCategory = TransactionCategoryEnum.TRANSPORTATION;

            budget.changeCategory(newCategory);

            assertEquals(newCategory, budget.getCategory(),
                    "Category should be updated to new value");
        }
    }

    // ========================================
    // PRIORITY HIGH - isExceeded()
    // ========================================

    @Nested
    @DisplayName("isExceeded() - Budget Limit Check")
    class IsExceededTests {

        /**
         * Verifies that isExceeded with null throws NullPointerException.
         * <p>
         * Bug it could reveal: Missing null validation.
         */
        @Test
        @DisplayName("isExceeded with null amountSpent throws NullPointerException")
        void isExceeded_nullAmountSpent_throwsNPE() {
            Budget budget = new Budget(validBudgetID, validUserId, validLimit, validCategory);

            NullPointerException exception = assertThrows(
                    NullPointerException.class,
                    () -> budget.isExceeded(null)
            );

            assertEquals("amountSpent cannot be null", exception.getMessage(),
                    "Exception message should indicate null amountSpent");
        }

        /**
         * Verifies that isExceeded returns false when amount < limit.
         * <p>
         * Example: limit = 500, spent = 300 → not exceeded
         * <p>
         * Bug it could reveal: Incorrectly reports exceeded when under limit.
         */
        @Test
        @DisplayName("isExceeded with amount less than limit returns false")
        void isExceeded_amountLessThanLimit_returnsFalse() {
            Budget budget = new Budget(validBudgetID, validUserId, validLimit, validCategory);
            Money amountSpent = Money.of(new BigDecimal("300.00"));

            assertFalse(budget.isExceeded(amountSpent),
                    "Should not be exceeded when spent < limit");
        }

        /**
         * Verifies that isExceeded returns true when amount = limit.
         * <p>
         * Business Rule: Reaching exactly the limit counts as exceeded.
         * <p>
         * Bug it could reveal: Boundary condition incorrect.
         */
        @Test
        @DisplayName("isExceeded with amount equals limit returns true")
        void isExceeded_amountEqualsLimit_returnsTrue() {
            Budget budget = new Budget(validBudgetID, validUserId, validLimit, validCategory);
            Money amountSpent = Money.of(new BigDecimal("500.00")); // equals limit

            assertTrue(budget.isExceeded(amountSpent),
                    "Should be exceeded when spent = limit");
        }

        /**
         * Verifies that isExceeded returns true when amount > limit.
         * <p>
         * Bug it could reveal: Over limit not detected.
         */
        @Test
        @DisplayName("isExceeded with amount greater than limit returns true")
        void isExceeded_amountGreaterThanLimit_returnsTrue() {
            Budget budget = new Budget(validBudgetID, validUserId, validLimit, validCategory);
            Money amountSpent = Money.of(new BigDecimal("750.00"));

            assertTrue(budget.isExceeded(amountSpent),
                    "Should be exceeded when spent > limit");
        }
    }

    // ========================================
    // PRIORITY HIGH - getRemainingAmount()
    // ========================================

    @Nested
    @DisplayName("getRemainingAmount() - Remaining Budget")
    class GetRemainingAmountTests {

        /**
         * Verifies that getRemainingAmount with null throws NullPointerException.
         * <p>
         * Bug it could reveal: Missing null validation.
         */
        @Test
        @DisplayName("getRemainingAmount with null amountSpent throws NullPointerException")
        void getRemainingAmount_nullAmountSpent_throwsNPE() {
            Budget budget = new Budget(validBudgetID, validUserId, validLimit, validCategory);

            NullPointerException exception = assertThrows(
                    NullPointerException.class,
                    () -> budget.getRemainingAmount(null)
            );

            assertEquals("amountSpent cannot be null", exception.getMessage(),
                    "Exception message should indicate null amountSpent");
        }

        /**
         * Verifies remaining amount when spent < limit.
         * <p>
         * Example: limit = 500, spent = 300 → remaining = 200
         * <p>
         * Bug it could reveal: Incorrect subtraction.
         */
        @Test
        @DisplayName("getRemainingAmount with amount less than limit returns positive")
        void getRemainingAmount_amountLessThanLimit_returnsPositive() {
            Budget budget = new Budget(validBudgetID, validUserId, validLimit, validCategory);
            Money amountSpent = Money.of(new BigDecimal("300.00"));

            Money remaining = budget.getRemainingAmount(amountSpent);

            assertEquals(Money.of(new BigDecimal("200.00")), remaining,
                    "Remaining should be limit - amountSpent");
        }

        /**
         * Verifies remaining amount is zero when spent = limit.
         * <p>
         * Bug it could reveal: Non-zero result at boundary.
         */
        @Test
        @DisplayName("getRemainingAmount with amount equals limit returns zero")
        void getRemainingAmount_amountEqualsLimit_returnsZero() {
            Budget budget = new Budget(validBudgetID, validUserId, validLimit, validCategory);
            Money amountSpent = Money.of(new BigDecimal("500.00"));

            Money remaining = budget.getRemainingAmount(amountSpent);

            assertEquals(Money.zero(), remaining,
                    "Remaining should be zero when spent = limit");
        }

        /**
         * Verifies remaining amount is zero (not negative) when spent > limit.
         * <p>
         * Business Rule: Remaining can never be negative.
         * <p>
         * Bug it could reveal: Returns negative remaining.
         */
        @Test
        @DisplayName("getRemainingAmount with amount greater than limit returns zero")
        void getRemainingAmount_amountGreaterThanLimit_returnsZero() {
            Budget budget = new Budget(validBudgetID, validUserId, validLimit, validCategory);
            Money amountSpent = Money.of(new BigDecimal("750.00"));

            Money remaining = budget.getRemainingAmount(amountSpent);

            assertEquals(Money.zero(), remaining,
                    "Remaining should be zero when spent > limit (never negative)");
        }
    }

    // ========================================
    // PRIORITY HIGH - getUsagePercentage()
    // ========================================

    @Nested
    @DisplayName("getUsagePercentage() - Budget Usage")
    class GetUsagePercentageTests {

        /**
         * Verifies that getUsagePercentage with null throws NullPointerException.
         * <p>
         * Bug it could reveal: Missing null validation.
         */
        @Test
        @DisplayName("getUsagePercentage with null amountSpent throws NullPointerException")
        void getUsagePercentage_nullAmountSpent_throwsNPE() {
            Budget budget = new Budget(validBudgetID, validUserId, validLimit, validCategory);

            NullPointerException exception = assertThrows(
                    NullPointerException.class,
                    () -> budget.getUsagePercentage(null)
            );

            assertEquals("amountSpent cannot be null", exception.getMessage(),
                    "Exception message should indicate null amountSpent");
        }

        /**
         * Verifies usage percentage is 0 when nothing spent.
         * <p>
         * Bug it could reveal: Non-zero result for zero spending.
         */
        @Test
        @DisplayName("getUsagePercentage with zero spent returns zero")
        void getUsagePercentage_zeroSpent_returnsZero() {
            Budget budget = new Budget(validBudgetID, validUserId, validLimit, validCategory);
            Money amountSpent = Money.of(BigDecimal.ZERO);

            BigDecimal usage = budget.getUsagePercentage(amountSpent);

            assertEquals(0, usage.compareTo(BigDecimal.ZERO),
                    "Usage should be 0% when nothing spent");
        }

        /**
         * Verifies usage percentage is 50% when half the limit is spent.
         * <p>
         * Example: limit = 500, spent = 250 → 50%
         * <p>
         * Bug it could reveal: Incorrect percentage calculation.
         */
        @Test
        @DisplayName("getUsagePercentage with half spent returns 50")
        void getUsagePercentage_halfSpent_returns50() {
            Budget budget = new Budget(validBudgetID, validUserId, validLimit, validCategory);
            Money amountSpent = Money.of(new BigDecimal("250.00"));

            BigDecimal usage = budget.getUsagePercentage(amountSpent);

            assertEquals(0, usage.compareTo(new BigDecimal("50")),
                    "Usage should be 50% when half the limit is spent");
        }

        /**
         * Verifies usage percentage is 100% when entire limit is spent.
         * <p>
         * Bug it could reveal: Incorrect calculation at boundary.
         */
        @Test
        @DisplayName("getUsagePercentage with full spent returns 100")
        void getUsagePercentage_fullSpent_returns100() {
            Budget budget = new Budget(validBudgetID, validUserId, validLimit, validCategory);
            Money amountSpent = Money.of(new BigDecimal("500.00"));

            BigDecimal usage = budget.getUsagePercentage(amountSpent);

            assertEquals(0, usage.compareTo(new BigDecimal("100")),
                    "Usage should be 100% when entire limit is spent");
        }

        /**
         * Verifies usage percentage is capped at 100% even when overspent.
         * <p>
         * Business Rule: Usage percentage should not exceed 100%.
         * <p>
         * Bug it could reveal: Returns > 100% when overspent.
         */
        @Test
        @DisplayName("getUsagePercentage with overspent returns 100 max")
        void getUsagePercentage_overSpent_returns100Max() {
            Budget budget = new Budget(validBudgetID, validUserId, validLimit, validCategory);
            Money amountSpent = Money.of(new BigDecimal("750.00")); // 150% of limit

            BigDecimal usage = budget.getUsagePercentage(amountSpent);

            assertEquals(0, usage.compareTo(new BigDecimal("100")),
                    "Usage should be capped at 100% even when overspent");
        }
    }

    // ========================================
    // PRIORITY MEDIUM - renewBudget()
    // ========================================

    @Nested
    @DisplayName("renewBudget() - Budget Renewal")
    class RenewBudgetTests {

        /**
         * Verifies that renewBudget advances the period on active budget.
         * <p>
         * Bug it could reveal: Period not updated on renewal.
         */
        @Test
        @DisplayName("renewBudget on active state advances period")
        void renewBudget_activeState_advancesPeriod() {
            Budget budget = new Budget(validBudgetID, validUserId, validLimit, validCategory);
            BudgetPeriod originalPeriod = budget.getPeriod();

            budget.renewBudget();

            assertNotEquals(originalPeriod, budget.getPeriod(),
                    "Period should change after renewal");
        }

        /**
         * Verifies that renewBudget on inactive budget throws IllegalStateException.
         * <p>
         * Business Rule: Cannot renew a deactivated budget.
         * <p>
         * Bug it could reveal: Allows renewal of inactive budgets.
         */
        @Test
        @DisplayName("renewBudget on inactive state throws IllegalStateException")
        void renewBudget_inactiveState_throwsIllegalState() {
            Budget budget = new Budget(validBudgetID, validUserId, validLimit, validCategory);
            budget.deactivateBudget();

            IllegalStateException exception = assertThrows(
                    IllegalStateException.class,
                    () -> budget.renewBudget()
            );

            assertEquals("Cannot renew an inactive budget", exception.getMessage(),
                    "Exception message should indicate cannot renew inactive");
        }

        /**
         * Verifies that multiple renewals advance the period multiple times.
         * <p>
         * Bug it could reveal: Renewal only works once.
         */
        @Test
        @DisplayName("renewBudget multiple times advances multiple periods")
        void renewBudget_multipleTimes_advancesMultiplePeriods() {
            Budget budget = new Budget(validBudgetID, validUserId, validLimit, validCategory);
            BudgetPeriod period0 = budget.getPeriod();

            budget.renewBudget();
            BudgetPeriod period1 = budget.getPeriod();

            budget.renewBudget();
            BudgetPeriod period2 = budget.getPeriod();

            assertNotEquals(period0, period1, "First renewal should change period");
            assertNotEquals(period1, period2, "Second renewal should change period");
            assertNotEquals(period0, period2, "Periods should all be different");
        }
    }

    // ========================================
    // PRIORITY MEDIUM - deactivateBudget()
    // ========================================

    @Nested
    @DisplayName("deactivateBudget() - Budget Deactivation")
    class DeactivateBudgetTests {

        /**
         * Verifies that deactivateBudget changes state to INACTIVE.
         * <p>
         * Bug it could reveal: State not changed on deactivation.
         */
        @Test
        @DisplayName("deactivateBudget on active state changes state to INACTIVE")
        void deactivateBudget_activeState_changesStateToInactive() {
            Budget budget = new Budget(validBudgetID, validUserId, validLimit, validCategory);
            assertEquals(BudgetState.ACTIVE, budget.getState(), "Should start active");

            budget.deactivateBudget();

            assertEquals(BudgetState.INACTIVE, budget.getState(),
                    "State should be INACTIVE after deactivation");
        }

        /**
         * Verifies that deactivating an already inactive budget throws IllegalStateException.
         * <p>
         * Business Rule: Cannot deactivate twice.
         * <p>
         * Bug it could reveal: Allows double deactivation.
         */
        @Test
        @DisplayName("deactivateBudget on already inactive throws IllegalStateException")
        void deactivateBudget_alreadyInactive_throwsIllegalState() {
            Budget budget = new Budget(validBudgetID, validUserId, validLimit, validCategory);
            budget.deactivateBudget();

            IllegalStateException exception = assertThrows(
                    IllegalStateException.class,
                    () -> budget.deactivateBudget()
            );

            assertEquals("Budget is already inactive", exception.getMessage(),
                    "Exception message should indicate already inactive");
        }
    }

    // ========================================
    // PRIORITY MEDIUM - Equality and HashCode
    // ========================================

    @Nested
    @DisplayName("Equality and HashCode")
    class EqualityTests {

        /**
         * Verifies that two Budgets with same values are equal.
         * <p>
         * Bug it could reveal: equals() compares references instead of values.
         */
        @Test
        @DisplayName("equals with same values returns true")
        void equals_sameValues_returnsTrue() {
            Budget budget1 = new Budget(validBudgetID, validUserId, validLimit, validCategory);
            Budget budget2 = new Budget(validBudgetID, validUserId, validLimit, validCategory);

            assertEquals(budget1, budget2, "Budgets with same values should be equal");
        }

        /**
         * Verifies that Budgets with different IDs are not equal.
         * <p>
         * Bug it could reveal: equals() ignores budgetID.
         */
        @Test
        @DisplayName("equals with different budgetID returns false")
        void equals_differentBudgetID_returnsFalse() {
            Budget budget1 = new Budget(BudgetID.of(1L), validUserId, validLimit, validCategory);
            Budget budget2 = new Budget(BudgetID.of(2L), validUserId, validLimit, validCategory);

            assertNotEquals(budget1, budget2, "Budgets with different IDs should not be equal");
        }

        /**
         * Verifies that Budgets with different userIds are not equal.
         * <p>
         * Bug it could reveal: equals() ignores userId.
         */
        @Test
        @DisplayName("equals with different userId returns false")
        void equals_differentUserId_returnsFalse() {
            Budget budget1 = new Budget(validBudgetID, UserId.of(1L), validLimit, validCategory);
            Budget budget2 = new Budget(validBudgetID, UserId.of(2L), validLimit, validCategory);

            assertNotEquals(budget1, budget2, "Budgets with different userIds should not be equal");
        }

        /**
         * Verifies that Budgets with different limits are not equal.
         * <p>
         * Bug it could reveal: equals() ignores limit.
         */
        @Test
        @DisplayName("equals with different limit returns false")
        void equals_differentLimit_returnsFalse() {
            Budget budget1 = new Budget(validBudgetID, validUserId, Money.of(new BigDecimal("100")), validCategory);
            Budget budget2 = new Budget(validBudgetID, validUserId, Money.of(new BigDecimal("200")), validCategory);

            assertNotEquals(budget1, budget2, "Budgets with different limits should not be equal");
        }

        /**
         * Verifies that comparing with null returns false.
         * <p>
         * Bug it could reveal: NPE when comparing with null.
         */
        @Test
        @DisplayName("equals with null returns false")
        void equals_null_returnsFalse() {
            Budget budget = new Budget(validBudgetID, validUserId, validLimit, validCategory);

            assertNotEquals(null, budget, "Budget should not equal null");
        }

        /**
         * Verifies that comparing with different type returns false.
         * <p>
         * Bug it could reveal: ClassCastException.
         */
        @Test
        @DisplayName("equals with different type returns false")
        void equals_differentType_returnsFalse() {
            Budget budget = new Budget(validBudgetID, validUserId, validLimit, validCategory);
            String differentType = "Budget";

            assertNotEquals(differentType, budget, "Budget should not equal different type");
        }

        /**
         * Verifies hashCode contract: equal objects have same hashCode.
         * <p>
         * Bug it could reveal: hashCode contract violated.
         */
        @Test
        @DisplayName("hashCode same for equal objects")
        void hashCode_equalObjects_sameHash() {
            Budget budget1 = new Budget(validBudgetID, validUserId, validLimit, validCategory);
            Budget budget2 = new Budget(validBudgetID, validUserId, validLimit, validCategory);

            assertEquals(budget1.hashCode(), budget2.hashCode(),
                    "Equal objects must have same hashCode");
        }

        /**
         * Verifies that Budgets with different categories are not equal.
         * <p>
         * Bug it could reveal: equals() ignores category.
         */
        @Test
        @DisplayName("equals with different category returns false")
        void equals_differentCategory_returnsFalse() {
            Budget budget1 = new Budget(validBudgetID, validUserId, validLimit, TransactionCategoryEnum.ALIMENTATION);
            Budget budget2 = new Budget(validBudgetID, validUserId, validLimit, TransactionCategoryEnum.TRANSPORTATION);

            assertNotEquals(budget1, budget2, "Budgets with different categories should not be equal");
        }

        /**
         * Verifies that Budgets with different states are still equal.
         * <p>
         * Business Rule: state is NOT included in equals() - two budgets
         * with same configuration but different states are "equal" for identity purposes.
         * <p>
         * Bug it could reveal: state incorrectly included in equals().
         */
        @Test
        @DisplayName("equals with different state still returns true")
        void equals_differentState_returnsTrue() {
            Budget budget1 = new Budget(validBudgetID, validUserId, validLimit, validCategory);
            Budget budget2 = new Budget(validBudgetID, validUserId, validLimit, validCategory);
            budget2.deactivateBudget(); // Make budget2 INACTIVE

            assertEquals(budget1, budget2,
                    "Budgets with same identity but different state should be equal (state not in equals)");
        }

        /**
         * Verifies reflexivity: a budget equals itself.
         * <p>
         * Bug it could reveal: Reflexivity contract violated.
         */
        @Test
        @DisplayName("equals reflexivity - object equals itself")
        void equals_reflexivity_returnsTrue() {
            Budget budget = new Budget(validBudgetID, validUserId, validLimit, validCategory);

            assertEquals(budget, budget, "Budget should equal itself");
        }

        /**
         * Verifies that hashCode is consistent with equals.
         * <p>
         * If two objects are equal, they must have the same hashCode.
         * <p>
         * Bug it could reveal: Inconsistency between equals and hashCode.
         */
        @Test
        @DisplayName("hashCode consistent with equals")
        void hashCode_consistentWithEquals() {
            Budget budget1 = new Budget(validBudgetID, validUserId, validLimit, validCategory);
            Budget budget2 = new Budget(validBudgetID, validUserId, validLimit, validCategory);

            if (budget1.equals(budget2)) {
                assertEquals(budget1.hashCode(), budget2.hashCode(),
                        "If equals() returns true, hashCode must be the same");
            }
        }
    }

    // ========================================
    // Additional Constructor Tests
    // ========================================

    @Nested
    @DisplayName("Constructor - Additional Tests")
    class ConstructorAdditionalTests {

        /**
         * Verifies that null budgetID is allowed (for transient new budgets).
         * <p>
         * Business Rule: New budgets before persistence don't have an ID yet.
         * <p>
         * Bug it could reveal: Null budgetID incorrectly rejected.
         */
        @Test
        @DisplayName("constructor with null budgetID is allowed")
        void constructor_nullBudgetID_allowed() {
            Budget budget = new Budget(null, validUserId, validLimit, validCategory);

            assertNotNull(budget, "Budget should be created");
            assertNull(budget.getBudgetID(), "BudgetID should be null for transient budget");
        }

        /**
         * Verifies that constructor sets initial period to current month.
         * <p>
         * Bug it could reveal: Period not initialized correctly.
         */
        @Test
        @DisplayName("constructor sets initial period to current month")
        void constructor_setsInitialPeriodToCurrentMonth() {
            Budget budget = new Budget(validBudgetID, validUserId, validLimit, validCategory);
            LocalDate today = LocalDate.now();
            LocalDate expectedStart = today.withDayOfMonth(1);

            assertEquals(expectedStart, budget.getPeriod().getStartDate(),
                    "Initial period should start on first day of current month");
        }
    }

    // ========================================
    // Additional changeLimit Tests
    // ========================================

    @Nested
    @DisplayName("changeLimit() - Additional Tests")
    class ChangeLimitAdditionalTests {

        /**
         * Verifies that changeLimit preserves all other fields.
         * <p>
         * Bug it could reveal: changeLimit accidentally mutates other fields.
         */
        @Test
        @DisplayName("changeLimit preserves other fields")
        void changeLimit_preservesOtherFields() {
            Budget budget = new Budget(validBudgetID, validUserId, validLimit, validCategory);
            BudgetPeriod originalPeriod = budget.getPeriod();
            BudgetState originalState = budget.getState();
            Money newLimit = Money.of(new BigDecimal("1000.00"));

            budget.changeLimit(newLimit);

            assertEquals(validBudgetID, budget.getBudgetID(), "BudgetID should be unchanged");
            assertEquals(validUserId, budget.getUserId(), "UserId should be unchanged");
            assertEquals(validCategory, budget.getCategory(), "Category should be unchanged");
            assertEquals(originalPeriod, budget.getPeriod(), "Period should be unchanged");
            assertEquals(originalState, budget.getState(), "State should be unchanged");
        }
    }

    // ========================================
    // Additional changeCategory Tests
    // ========================================

    @Nested
    @DisplayName("changeCategory() - Additional Tests")
    class ChangeCategoryAdditionalTests {

        /**
         * Verifies that changeCategory preserves all other fields.
         * <p>
         * Bug it could reveal: changeCategory accidentally mutates other fields.
         */
        @Test
        @DisplayName("changeCategory preserves other fields")
        void changeCategory_preservesOtherFields() {
            Budget budget = new Budget(validBudgetID, validUserId, validLimit, validCategory);
            BudgetPeriod originalPeriod = budget.getPeriod();
            BudgetState originalState = budget.getState();
            Money originalLimit = budget.getLimit();
            TransactionCategoryEnum newCategory = TransactionCategoryEnum.TRANSPORTATION;

            budget.changeCategory(newCategory);

            assertEquals(validBudgetID, budget.getBudgetID(), "BudgetID should be unchanged");
            assertEquals(validUserId, budget.getUserId(), "UserId should be unchanged");
            assertEquals(originalLimit, budget.getLimit(), "Limit should be unchanged");
            assertEquals(originalPeriod, budget.getPeriod(), "Period should be unchanged");
            assertEquals(originalState, budget.getState(), "State should be unchanged");
        }
    }

    // ========================================
    // Additional isExceeded Tests
    // ========================================

    @Nested
    @DisplayName("isExceeded() - Additional Tests")
    class IsExceededAdditionalTests {

        /**
         * Verifies that zero amount spent is not exceeded.
         * <p>
         * Bug it could reveal: Zero incorrectly treated as exceeded.
         */
        @Test
        @DisplayName("isExceeded with zero amount spent returns false")
        void isExceeded_zeroAmountSpent_returnsFalse() {
            Budget budget = new Budget(validBudgetID, validUserId, validLimit, validCategory);
            Money zeroSpent = Money.of(BigDecimal.ZERO);

            assertFalse(budget.isExceeded(zeroSpent),
                    "Zero spending should not be exceeded");
        }
    }

    // ========================================
    // Additional getRemainingAmount Tests
    // ========================================

    @Nested
    @DisplayName("getRemainingAmount() - Additional Tests")
    class GetRemainingAmountAdditionalTests {

        /**
         * Verifies that zero spent returns full limit as remaining.
         * <p>
         * Bug it could reveal: Doesn't return full limit when nothing spent.
         */
        @Test
        @DisplayName("getRemainingAmount with zero spent returns full limit")
        void getRemainingAmount_zeroSpent_returnsFullLimit() {
            Budget budget = new Budget(validBudgetID, validUserId, validLimit, validCategory);
            Money zeroSpent = Money.of(BigDecimal.ZERO);

            Money remaining = budget.getRemainingAmount(zeroSpent);

            assertEquals(validLimit, remaining,
                    "Remaining should equal full limit when nothing spent");
        }

        /**
         * Verifies correct remaining for partial spending.
         * <p>
         * Example: limit = 500, spent = 123.45 → remaining = 376.55
         * <p>
         * Bug it could reveal: Incorrect subtraction or precision loss.
         */
        @Test
        @DisplayName("getRemainingAmount with partial spent returns correct remaining")
        void getRemainingAmount_partialSpent_returnsCorrectRemaining() {
            Budget budget = new Budget(validBudgetID, validUserId, validLimit, validCategory);
            Money partialSpent = Money.of(new BigDecimal("123.45"));

            Money remaining = budget.getRemainingAmount(partialSpent);

            assertEquals(Money.of(new BigDecimal("376.55")), remaining,
                    "Remaining should be 500 - 123.45 = 376.55");
        }
    }

    // ========================================
    // Additional getUsagePercentage Tests
    // ========================================

    @Nested
    @DisplayName("getUsagePercentage() - Additional Tests")
    class GetUsagePercentageAdditionalTests {

        /**
         * Verifies precision handling in percentage calculation.
         * <p>
         * Example: limit = 500, spent = 123.45 → usage = 24.69%
         * <p>
         * Bug it could reveal: Precision loss or incorrect rounding.
         */
        @Test
        @DisplayName("getUsagePercentage handles precision correctly")
        void getUsagePercentage_precisionHandling() {
            Budget budget = new Budget(validBudgetID, validUserId, validLimit, validCategory);
            Money spent = Money.of(new BigDecimal("123.45"));

            BigDecimal usage = budget.getUsagePercentage(spent);

            // 123.45 / 500 * 100 = 24.69
            assertEquals(0, usage.compareTo(new BigDecimal("24.69")),
                    "Usage should be 24.69% (123.45 / 500 * 100)");
        }

        /**
         * Verifies that exactly 150% overspending is capped at 100%.
         * <p>
         * Example: limit = 500, spent = 750 → usage = 100% (capped)
         * <p>
         * Bug it could reveal: Returns 150% instead of capped 100%.
         */
        @Test
        @DisplayName("getUsagePercentage with 150% overspending is capped at 100")
        void getUsagePercentage_150PercentOverspent_cappedAt100() {
            Budget budget = new Budget(validBudgetID, validUserId, validLimit, validCategory);
            Money overspent = Money.of(new BigDecimal("750.00")); // 150% of 500

            BigDecimal usage = budget.getUsagePercentage(overspent);

            assertEquals(0, usage.compareTo(new BigDecimal("100")),
                    "Usage should be capped at 100% even when 150% spent");
        }
    }

    // ========================================
    // Additional renewBudget Tests
    // ========================================

    @Nested
    @DisplayName("renewBudget() - Additional Tests")
    class RenewBudgetAdditionalTests {

        /**
         * Verifies that renewBudget preserves all fields except period.
         * <p>
         * Bug it could reveal: renewBudget accidentally mutates other fields.
         */
        @Test
        @DisplayName("renewBudget preserves other fields")
        void renewBudget_preservesOtherFields() {
            Budget budget = new Budget(validBudgetID, validUserId, validLimit, validCategory);
            Money originalLimit = budget.getLimit();
            BudgetState originalState = budget.getState();

            budget.renewBudget();

            assertEquals(validBudgetID, budget.getBudgetID(), "BudgetID should be unchanged");
            assertEquals(validUserId, budget.getUserId(), "UserId should be unchanged");
            assertEquals(originalLimit, budget.getLimit(), "Limit should be unchanged");
            assertEquals(validCategory, budget.getCategory(), "Category should be unchanged");
            assertEquals(originalState, budget.getState(), "State should be unchanged");
        }
    }

    // ========================================
    // Additional deactivateBudget Tests
    // ========================================

    @Nested
    @DisplayName("deactivateBudget() - Additional Tests")
    class DeactivateBudgetAdditionalTests {

        /**
         * Verifies that deactivateBudget preserves all fields except state.
         * <p>
         * Bug it could reveal: deactivateBudget accidentally mutates other fields.
         */
        @Test
        @DisplayName("deactivateBudget preserves other fields")
        void deactivateBudget_preservesOtherFields() {
            Budget budget = new Budget(validBudgetID, validUserId, validLimit, validCategory);
            Money originalLimit = budget.getLimit();
            BudgetPeriod originalPeriod = budget.getPeriod();

            budget.deactivateBudget();

            assertEquals(validBudgetID, budget.getBudgetID(), "BudgetID should be unchanged");
            assertEquals(validUserId, budget.getUserId(), "UserId should be unchanged");
            assertEquals(originalLimit, budget.getLimit(), "Limit should be unchanged");
            assertEquals(validCategory, budget.getCategory(), "Category should be unchanged");
            assertEquals(originalPeriod, budget.getPeriod(), "Period should be unchanged");
        }
    }
}

