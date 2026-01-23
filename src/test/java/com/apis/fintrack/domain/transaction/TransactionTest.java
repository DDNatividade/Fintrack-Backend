package com.apis.fintrack.domain.transaction;

import com.apis.fintrack.domain.transaction.model.*;
import com.apis.fintrack.domain.user.model.UserId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the {@link Transaction} domain entity.
 *
 * <p>Tests cover factory methods, constructor validation, domain behavior,
 * and query methods following specification-based testing approaches.</p>
 */
@DisplayName("Transaction Domain Entity Tests")
class TransactionTest {

    private Description validDescription;
    private TransactionAmount validIncomeAmount;
    private TransactionAmount validExpenseAmount;
    private TransactionDate validDate;
    private Category validCategory;
    private UserId validUserId;

    @BeforeEach
    void setUp() {
        validDescription = Description.of("Test transaction");
        validIncomeAmount = TransactionAmount.income(new BigDecimal("100.00"));
        validExpenseAmount = TransactionAmount.expense(new BigDecimal("50.00"));
        validDate = TransactionDate.now();
        validCategory = Category.of(TransactionCategoryEnum.SALARY);
        validUserId = UserId.of(1L);
    }

    // ==================== CONSTRUCTOR - HIGH PRIORITY ====================

    @Nested
    @DisplayName("Constructor")
    class ConstructorTests {

        /**
         * Verifies that the constructor creates a valid Transaction.
         */
        @Test
        @DisplayName("Should create Transaction with valid parameters")
        void constructor_withValidParams_shouldCreateTransaction() {
            Transaction transaction = new Transaction(
                    TransactionId.empty(),
                    validDescription,
                    validIncomeAmount,
                    validDate,
                    validCategory,
                    validUserId
            );

            assertNotNull(transaction);
            assertTrue(transaction.getId().isEmpty());
            assertEquals(validDescription, transaction.getDescription());
            assertEquals(validIncomeAmount, transaction.getAmount());
            assertEquals(validDate, transaction.getTransactionDate());
            assertEquals(validCategory, transaction.getCategory());
            assertEquals(validUserId, transaction.getUserId());
        }

        /**
         * Verifies that null description throws NullPointerException.
         */
        @Test
        @DisplayName("Should throw NullPointerException when description is null")
        void constructor_nullDescription_shouldThrowNullPointerException() {
            assertThrows(NullPointerException.class, () ->
                    new Transaction(
                            TransactionId.empty(),
                            null,
                            validIncomeAmount,
                            validDate,
                            validCategory,
                            validUserId
                    )
            );
        }

        /**
         * Verifies that null amount throws NullPointerException.
         */
        @Test
        @DisplayName("Should throw NullPointerException when amount is null")
        void constructor_nullAmount_shouldThrowNullPointerException() {
            assertThrows(NullPointerException.class, () ->
                    new Transaction(
                            TransactionId.empty(),
                            validDescription,
                            null,
                            validDate,
                            validCategory,
                            validUserId
                    )
            );
        }

        /**
         * Verifies that null date throws NullPointerException.
         */
        @Test
        @DisplayName("Should throw NullPointerException when date is null")
        void constructor_nullDate_shouldThrowNullPointerException() {
            assertThrows(NullPointerException.class, () ->
                    new Transaction(
                            TransactionId.empty(),
                            validDescription,
                            validIncomeAmount,
                            null,
                            validCategory,
                            validUserId
                    )
            );
        }

        /**
         * Verifies that null category throws NullPointerException.
         */
        @Test
        @DisplayName("Should throw NullPointerException when category is null")
        void constructor_nullCategory_shouldThrowNullPointerException() {
            assertThrows(NullPointerException.class, () ->
                    new Transaction(
                            TransactionId.empty(),
                            validDescription,
                            validIncomeAmount,
                            validDate,
                            null,
                            validUserId
                    )
            );
        }

        /**
         * Verifies that null userId throws NullPointerException.
         */
        @Test
        @DisplayName("Should throw NullPointerException when userId is null")
        void constructor_nullUserId_shouldThrowNullPointerException() {
            assertThrows(NullPointerException.class, () ->
                    new Transaction(
                            TransactionId.empty(),
                            validDescription,
                            validIncomeAmount,
                            validDate,
                            validCategory,
                            null
                    )
            );
        }
    }

    // ==================== FACTORY METHOD: createIncome() - HIGH PRIORITY ====================

    @Nested
    @DisplayName("Factory Method: createIncome()")
    class CreateIncomeTests {

        /**
         * Verifies that createIncome() creates an income transaction.
         */
        @Test
        @DisplayName("Should create income transaction with positive amount")
        void createIncome_shouldCreateIncomeTransaction() {
            Transaction transaction = Transaction.createIncome(
                    "Monthly salary",
                    new BigDecimal("3000.00"),
                    TransactionCategoryEnum.SALARY,
                    1L
            );

            assertNotNull(transaction);
            assertTrue(transaction.isIncome());
            assertFalse(transaction.isExpense());
            assertTrue(transaction.getAmount().getValue().compareTo(BigDecimal.ZERO) > 0);
            assertEquals(TransactionType.INCOME, transaction.getType());
        }

        /**
         * Verifies that createIncome() assigns empty ID.
         */
        @Test
        @DisplayName("Should assign empty ID for new transaction")
        void createIncome_shouldAssignEmptyId() {
            Transaction transaction = Transaction.createIncome(
                    "Salary",
                    new BigDecimal("1000.00"),
                    TransactionCategoryEnum.SALARY,
                    1L
            );

            assertTrue(transaction.getId().isEmpty());
        }
    }

    // ==================== FACTORY METHOD: createExpense() - HIGH PRIORITY ====================

    @Nested
    @DisplayName("Factory Method: createExpense()")
    class CreateExpenseTests {

        /**
         * Verifies that createExpense() creates an expense transaction.
         */
        @Test
        @DisplayName("Should create expense transaction with negative amount")
        void createExpense_shouldCreateExpenseTransaction() {
            Transaction transaction = Transaction.createExpense(
                    "Grocery shopping",
                    new BigDecimal("150.00"),
                    TransactionCategoryEnum.ALIMENTATION,
                    1L
            );

            assertNotNull(transaction);
            assertTrue(transaction.isExpense());
            assertFalse(transaction.isIncome());
            assertTrue(transaction.getAmount().getValue().compareTo(BigDecimal.ZERO) < 0);
            assertEquals(TransactionType.EXPENSE, transaction.getType());
        }

        /**
         * Verifies that createExpense() stores amount as negative.
         */
        @Test
        @DisplayName("Should store amount as negative value")
        void createExpense_shouldStoreNegativeAmount() {
            Transaction transaction = Transaction.createExpense(
                    "Rent",
                    new BigDecimal("800.00"),
                    TransactionCategoryEnum.HOUSING,
                    1L
            );

            assertEquals(new BigDecimal("-800.00"), transaction.getAmount().getValue());
        }
    }

    // ==================== FACTORY METHOD: create() - HIGH PRIORITY ====================

    @Nested
    @DisplayName("Factory Method: create()")
    class CreateTests {

        /**
         * Verifies that create() with isIncome=true creates income.
         */
        @Test
        @DisplayName("Should create income when isIncome is true")
        void create_withIsIncomeTrue_shouldCreateIncome() {
            Transaction transaction = Transaction.create(
                    "Freelance payment",
                    new BigDecimal("500.00"),
                    true,
                    TransactionCategoryEnum.FREELANCE,
                    1L
            );

            assertTrue(transaction.isIncome());
        }

        /**
         * Verifies that create() with isIncome=false creates expense.
         */
        @Test
        @DisplayName("Should create expense when isIncome is false")
        void create_withIsIncomeFalse_shouldCreateExpense() {
            Transaction transaction = Transaction.create(
                    "Movie tickets",
                    new BigDecimal("25.00"),
                    false,
                    TransactionCategoryEnum.RECREATIONAL,
                    1L
            );

            assertTrue(transaction.isExpense());
        }
    }

    // ==================== QUERY METHODS - MEDIUM PRIORITY ====================

    @Nested
    @DisplayName("Query Methods")
    class QueryMethodsTests {

        /**
         * Verifies isIncome() for income transaction.
         */
        @Test
        @DisplayName("isIncome() should return true for income transaction")
        void isIncome_incomeTransaction_shouldReturnTrue() {
            Transaction income = new Transaction(
                    TransactionId.empty(),
                    validDescription,
                    validIncomeAmount,
                    validDate,
                    validCategory,
                    validUserId
            );

            assertTrue(income.isIncome());
        }

        /**
         * Verifies isExpense() for expense transaction.
         */
        @Test
        @DisplayName("isExpense() should return true for expense transaction")
        void isExpense_expenseTransaction_shouldReturnTrue() {
            Transaction expense = new Transaction(
                    TransactionId.empty(),
                    validDescription,
                    validExpenseAmount,
                    validDate,
                    Category.of(TransactionCategoryEnum.HOUSING),
                    validUserId
            );

            assertTrue(expense.isExpense());
        }

        /**
         * Verifies getType() returns correct type for income.
         */
        @Test
        @DisplayName("getType() should return INCOME for income transaction")
        void getType_incomeTransaction_shouldReturnIncome() {
            Transaction income = new Transaction(
                    TransactionId.empty(),
                    validDescription,
                    validIncomeAmount,
                    validDate,
                    validCategory,
                    validUserId
            );

            assertEquals(TransactionType.INCOME, income.getType());
        }

        /**
         * Verifies getType() returns correct type for expense.
         */
        @Test
        @DisplayName("getType() should return EXPENSE for expense transaction")
        void getType_expenseTransaction_shouldReturnExpense() {
            Transaction expense = new Transaction(
                    TransactionId.empty(),
                    validDescription,
                    validExpenseAmount,
                    validDate,
                    Category.of(TransactionCategoryEnum.HOUSING),
                    validUserId
            );

            assertEquals(TransactionType.EXPENSE, expense.getType());
        }
    }

    // ==================== BUSINESS METHODS - MEDIUM PRIORITY ====================

    @Nested
    @DisplayName("Business Methods")
    class BusinessMethodsTests {

        /**
         * Verifies changeDescription() updates description.
         */
        @Test
        @DisplayName("changeDescription() should update description")
        void changeDescription_shouldUpdateDescription() {
            Transaction transaction = Transaction.createIncome(
                    "Original",
                    new BigDecimal("100.00"),
                    TransactionCategoryEnum.SALARY,
                    1L
            );

            transaction.changeDescription("Updated description");

            assertEquals("Updated description", transaction.getDescription().getValue());
        }

        /**
         * Verifies changeCategory() updates category.
         */
        @Test
        @DisplayName("changeCategory() should update category")
        void changeCategory_shouldUpdateCategory() {
            Transaction transaction = Transaction.createExpense(
                    "Food",
                    new BigDecimal("50.00"),
                    TransactionCategoryEnum.ALIMENTATION,
                    1L
            );

            transaction.changeCategory(TransactionCategoryEnum.RECREATIONAL);

            assertEquals(TransactionCategoryEnum.RECREATIONAL, transaction.getCategory().getValue());
        }

        /**
         * Verifies changeDate() updates date.
         */
        @Test
        @DisplayName("changeDate() should update date")
        void changeDate_shouldUpdateDate() {
            Transaction transaction = Transaction.createIncome(
                    "Salary",
                    new BigDecimal("1000.00"),
                    TransactionCategoryEnum.SALARY,
                    1L
            );
            LocalDate newDate = LocalDate.now().minusDays(5);

            transaction.changeDate(newDate);

            assertEquals(newDate, transaction.getTransactionDate().getValue());
        }
    }
}

