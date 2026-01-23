package com.apis.fintrack.domain.budget.service;

import com.apis.fintrack.domain.budget.exception.BudgetWithCategoryAlreadyExists;
import com.apis.fintrack.domain.shared.model.Money;
import com.apis.fintrack.domain.transaction.model.TransactionCategoryEnum;
import com.apis.fintrack.domain.user.model.*;
import com.apis.fintrack.infrastructure.adapter.output.persistence.entity.BudgetJPAEntity;
import com.apis.fintrack.infrastructure.adapter.output.persistence.repository.BudgetRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

/**
 * Unit tests for BudgetDuplicationService.
 *
 * Tests follow specification-based testing approach, covering:
 * - Input validation (null user, null category)
 * - Core logic (budget exists → exception, budget not exists → no exception)
 * - Edge cases (different category, different user, user with no budgets)
 * - Repository interaction verification
 *
 * BudgetDuplicationService is a domain service that checks if a budget
 * with a specific category already exists for a user, preventing duplicates.
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("BudgetDuplicationService Tests")
class BudgetDuplicationServiceTest {

    @Mock
    private BudgetRepository budgetRepository;

    @Mock
    private BudgetJPAEntity mockBudgetEntity;

    private BudgetDuplicationService service;

    private User testUser;

    @BeforeEach
    void setUp() {
        service = new BudgetDuplicationService(budgetRepository);
        testUser = createTestUser(1L, "John", "Doe");
    }

    // ========================================
    // Helper Methods
    // ========================================

    /**
     * Creates a test user with the given parameters.
     */
    private User createTestUser(Long id, String name, String surname) {
        return new User(
                id != null ? UserId.of(id) : UserId.empty(),
                FullName.of(name, surname),
                Email.of("test@example.com"),
                Password.of("Password123!"),
                BirthDate.of(LocalDate.of(1990, 1, 15)),
                Money.zero()
        );
    }

    // ========================================
    // PRIORITY CRITICAL - Input Validation
    // ========================================

    @Nested
    @DisplayName("Input Validation")
    class InputValidationTests {

        /**
         * Verifies that passing null user throws NullPointerException.
         * <p>
         * Bug it could reveal: Missing null validation for user parameter.
         * Current implementation will throw NPE when calling user.getId().
         */
        @Test
        @DisplayName("checkIfBudgetExists with null user throws NullPointerException")
        void checkIfBudgetExists_nullUser_throwsNPE() {
            TransactionCategoryEnum category = TransactionCategoryEnum.ALIMENTATION;

            assertThrows(
                    NullPointerException.class,
                    () -> service.checkIfBudgetNameExistsForUser(null, category),
                    "Should throw NullPointerException when user is null"
            );
        }

        /**
         * Verifies behavior when null category is passed.
         * <p>
         * Current implementation passes null to repository which may cause issues.
         */
        @Test
        @DisplayName("checkIfBudgetExists with null category - current behavior")
        void checkIfBudgetExists_nullCategory_behavior() {
            // Setup - repository returns empty for null category
            when(budgetRepository.findByUserIdAndCategory(eq(1L), isNull()))
                    .thenReturn(Optional.empty());

            // Current implementation doesn't validate null category explicitly
            assertDoesNotThrow(
                    () -> service.checkIfBudgetNameExistsForUser(testUser, null),
                    "Current implementation allows null category (no validation)"
            );
        }
    }

    // ========================================
    // PRIORITY CRITICAL - Core Logic
    // ========================================

    @Nested
    @DisplayName("Core Logic - Budget Existence Check")
    class CoreLogicTests {

        /**
         * Verifies that when a budget with the same category exists for the user,
         * BudgetWithCategoryAlreadyExists exception is thrown.
         * <p>
         * Business Rule: A user cannot have two budgets for the same category.
         * <p>
         * Bug it could reveal: Duplicate budgets allowed.
         */
        @Test
        @DisplayName("checkIfBudgetExists when budget exists throws BudgetWithCategoryAlreadyExists")
        void checkIfBudgetExists_budgetExists_throwsException() {
            // Setup
            TransactionCategoryEnum category = TransactionCategoryEnum.ALIMENTATION;
            when(budgetRepository.findByUserIdAndCategory(1L, category))
                    .thenReturn(Optional.of(mockBudgetEntity));

            // Act & Assert
            BudgetWithCategoryAlreadyExists exception = assertThrows(
                    BudgetWithCategoryAlreadyExists.class,
                    () -> service.checkIfBudgetNameExistsForUser(testUser, category)
            );

            assertNotNull(exception.getMessage(), "Exception should have a message");
        }

        /**
         * Verifies that when no budget exists for the category, no exception is thrown.
         * <p>
         * Business Rule: Creating a new budget for an unused category is allowed.
         * <p>
         * Bug it could reveal: Exception thrown when it shouldn't be.
         */
        @Test
        @DisplayName("checkIfBudgetExists when budget not exists does not throw exception")
        void checkIfBudgetExists_budgetNotExists_noException() {
            // Setup
            TransactionCategoryEnum category = TransactionCategoryEnum.ALIMENTATION;
            when(budgetRepository.findByUserIdAndCategory(1L, category))
                    .thenReturn(Optional.empty());

            // Act & Assert
            assertDoesNotThrow(
                    () -> service.checkIfBudgetNameExistsForUser(testUser, category),
                    "Should not throw exception when budget doesn't exist"
            );
        }

        /**
         * Verifies that the exception message contains the category name.
         * <p>
         * Bug it could reveal: Uninformative error message.
         */
        @Test
        @DisplayName("exception message contains category name")
        void checkIfBudgetExists_exceptionContainsCategory() {
            // Setup
            TransactionCategoryEnum category = TransactionCategoryEnum.TRANSPORTATION;
            when(budgetRepository.findByUserIdAndCategory(1L, category))
                    .thenReturn(Optional.of(mockBudgetEntity));

            // Act
            BudgetWithCategoryAlreadyExists exception = assertThrows(
                    BudgetWithCategoryAlreadyExists.class,
                    () -> service.checkIfBudgetNameExistsForUser(testUser, category)
            );

            // Assert
            assertTrue(exception.getMessage().contains("TRANSPORTATION"),
                    "Exception message should contain category name");
        }

        /**
         * Verifies that the exception message contains the user's full name.
         * <p>
         * Bug it could reveal: Uninformative error message without user context.
         */
        @Test
        @DisplayName("exception message contains user full name")
        void checkIfBudgetExists_exceptionContainsUserName() {
            // Setup
            TransactionCategoryEnum category = TransactionCategoryEnum.HOUSING;
            User aliceUser = createTestUser(2L, "Alice", "Smith");
            when(budgetRepository.findByUserIdAndCategory(2L, category))
                    .thenReturn(Optional.of(mockBudgetEntity));

            // Act
            BudgetWithCategoryAlreadyExists exception = assertThrows(
                    BudgetWithCategoryAlreadyExists.class,
                    () -> service.checkIfBudgetNameExistsForUser(aliceUser, category)
            );

            // Assert
            assertTrue(exception.getMessage().contains("Alice") ||
                       exception.getMessage().contains("Smith"),
                    "Exception message should contain user's name");
        }
    }

    // ========================================
    // PRIORITY HIGH - Edge Cases
    // ========================================

    @Nested
    @DisplayName("Edge Cases")
    class EdgeCaseTests {

        /**
         * Verifies that having a budget in a different category doesn't trigger exception.
         * <p>
         * Example: User has ALIMENTATION budget, checking for TRANSPORTATION → ok
         * <p>
         * Bug it could reveal: Incorrect category comparison.
         */
        @Test
        @DisplayName("checkIfBudgetExists with different category does not throw exception")
        void checkIfBudgetExists_differentCategory_noException() {
            // Setup - user has ALIMENTATION budget, checking for TRANSPORTATION
            TransactionCategoryEnum newCategory = TransactionCategoryEnum.TRANSPORTATION;
            when(budgetRepository.findByUserIdAndCategory(1L, newCategory))
                    .thenReturn(Optional.empty());

            // Act & Assert
            assertDoesNotThrow(
                    () -> service.checkIfBudgetNameExistsForUser(testUser, newCategory),
                    "Should not throw when checking different category"
            );
        }

        /**
         * Verifies that same category for different user doesn't trigger exception.
         * <p>
         * Example: User A has ALIMENTATION, User B checks ALIMENTATION → ok for User B
         * <p>
         * Bug it could reveal: Query ignores userId.
         */
        @Test
        @DisplayName("checkIfBudgetExists same category different user does not throw exception")
        void checkIfBudgetExists_sameCategory_differentUser_noException() {
            // Setup - different user (ID = 2) doesn't have the budget
            TransactionCategoryEnum category = TransactionCategoryEnum.ALIMENTATION;
            User differentUser = createTestUser(2L, "Jane", "Doe");
            when(budgetRepository.findByUserIdAndCategory(2L, category))
                    .thenReturn(Optional.empty());

            // Act & Assert
            assertDoesNotThrow(
                    () -> service.checkIfBudgetNameExistsForUser(differentUser, category),
                    "Different user should be able to create budget for same category"
            );
        }

        /**
         * Verifies that a user with no budgets at all doesn't trigger exception.
         * <p>
         * Bug it could reveal: Error handling empty results.
         */
        @Test
        @DisplayName("checkIfBudgetExists user with no budgets does not throw exception")
        void checkIfBudgetExists_userWithNoBudgets_noException() {
            // Setup - new user with no budgets
            User newUser = createTestUser(999L, "New", "User");
            when(budgetRepository.findByUserIdAndCategory(eq(999L), any()))
                    .thenReturn(Optional.empty());

            // Act & Assert - should work for any category
            assertDoesNotThrow(
                    () -> service.checkIfBudgetNameExistsForUser(newUser, TransactionCategoryEnum.ALIMENTATION),
                    "New user should be able to create any budget"
            );
        }
    }

    // ========================================
    // PRIORITY MEDIUM - Repository Interaction
    // ========================================

    @Nested
    @DisplayName("Repository Interaction")
    class RepositoryInteractionTests {

        /**
         * Verifies that repository is called with correct parameters.
         * <p>
         * Bug it could reveal: Wrong parameters passed to repository.
         */
        @Test
        @DisplayName("checkIfBudgetExists calls repository with correct parameters")
        void checkIfBudgetExists_callsRepositoryWithCorrectParams() {
            // Setup
            Long expectedUserId = 1L;
            TransactionCategoryEnum expectedCategory = TransactionCategoryEnum.HEALTH;
            when(budgetRepository.findByUserIdAndCategory(expectedUserId, expectedCategory))
                    .thenReturn(Optional.empty());

            // Act
            service.checkIfBudgetNameExistsForUser(testUser, expectedCategory);

            // Assert - verify correct parameters
            verify(budgetRepository).findByUserIdAndCategory(expectedUserId, expectedCategory);
        }

        /**
         * Verifies that when repository returns Optional.empty(), no exception is thrown.
         * <p>
         * Bug it could reveal: Incorrect Optional handling.
         */
        @Test
        @DisplayName("checkIfBudgetExists repository returns empty does not throw")
        void checkIfBudgetExists_repositoryReturnsEmpty_noException() {
            // Setup
            when(budgetRepository.findByUserIdAndCategory(anyLong(), any()))
                    .thenReturn(Optional.empty());

            // Act & Assert
            assertDoesNotThrow(
                    () -> service.checkIfBudgetNameExistsForUser(testUser, TransactionCategoryEnum.ALIMENTATION)
            );
        }

        /**
         * Verifies that when repository returns Optional.of(budget), exception is thrown.
         * <p>
         * Bug it could reveal: isPresent() not checked correctly.
         */
        @Test
        @DisplayName("checkIfBudgetExists repository returns present throws exception")
        void checkIfBudgetExists_repositoryReturnsPresent_throwsException() {
            // Setup
            when(budgetRepository.findByUserIdAndCategory(anyLong(), any()))
                    .thenReturn(Optional.of(mockBudgetEntity));

            // Act & Assert
            assertThrows(
                    BudgetWithCategoryAlreadyExists.class,
                    () -> service.checkIfBudgetNameExistsForUser(testUser, TransactionCategoryEnum.ALIMENTATION)
            );
        }
    }

    // ========================================
    // PRIORITY MEDIUM - Constructor
    // ========================================

    @Nested
    @DisplayName("Constructor")
    class ConstructorTests {

        /**
         * Verifies behavior when constructor receives null repository.
         * <p>
         * Note: Current implementation does NOT validate this.
         * <p>
         * Bug it could reveal: NullPointerException deferred to method call.
         */
        @Test
        @DisplayName("constructor with null repository creates service but fails on use")
        void constructor_nullRepository_failsOnUse() {
            // Current implementation doesn't validate constructor params
            BudgetDuplicationService serviceWithNull = new BudgetDuplicationService(null);

            assertNotNull(serviceWithNull, "Service is created even with null repository");

            // But it will fail when trying to use it
            assertThrows(
                    NullPointerException.class,
                    () -> serviceWithNull.checkIfBudgetNameExistsForUser(testUser, TransactionCategoryEnum.ALIMENTATION),
                    "Should throw NPE when trying to use service with null repository"
            );
        }

        /**
         * Verifies that constructor with valid repository creates service.
         * <p>
         * Bug it could reveal: Constructor fails with valid input.
         */
        @Test
        @DisplayName("constructor with valid repository creates service")
        void constructor_validRepository_createsService() {
            BudgetDuplicationService validService = new BudgetDuplicationService(budgetRepository);

            assertNotNull(validService, "Service should be created with valid repository");
        }
    }
}

