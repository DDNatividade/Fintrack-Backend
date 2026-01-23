package com.apis.fintrack.domain.user.service;

import com.apis.fintrack.domain.user.exception.EmailAlreadyExistsException;
import com.apis.fintrack.infrastructure.adapter.output.persistence.entity.UserJPAEntity;
import com.apis.fintrack.infrastructure.adapter.output.persistence.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit tests for the {@link UserDuplicationService} domain service.
 *
 * <p>Tests cover email duplication check, exception throwing behavior,
 * and edge cases following specification-based testing approaches.</p>
 */
@ExtendWith(MockitoExtension.class)
@DisplayName("UserDuplicationService Domain Service Tests")
class UserDuplicationServiceTest {

    @Mock
    private UserRepository userRepository;

    private UserDuplicationService userDuplicationService;

    @BeforeEach
    void setUp() {
        userDuplicationService = new UserDuplicationService(userRepository);
    }

    // ==================== checkIfEmailExists() - HIGH PRIORITY ====================

    @Nested
    @DisplayName("Method: checkIfEmailExists()")
    class CheckIfEmailExistsTests {

        /**
         * Verifies that no exception is thrown when email does not exist.
         */
        @Test
        @DisplayName("Should not throw exception when email does not exist")
        void checkIfEmailExists_emailNotExists_shouldNotThrow() {
            String newEmail = "new.user@example.com";
            when(userRepository.findByEmail(newEmail)).thenReturn(Optional.empty());

            assertDoesNotThrow(() -> userDuplicationService.checkIfEmailExists(newEmail));

            verify(userRepository, times(1)).findByEmail(newEmail);
        }

        /**
         * Verifies that EmailAlreadyExistsException is thrown when email exists.
         */
        @Test
        @DisplayName("Should throw EmailAlreadyExistsException when email already exists")
        void checkIfEmailExists_emailExists_shouldThrowException() {
            String existingEmail = "existing@example.com";
            UserJPAEntity existingUser = new UserJPAEntity();
            when(userRepository.findByEmail(existingEmail)).thenReturn(Optional.of(existingUser));

            EmailAlreadyExistsException exception = assertThrows(
                    EmailAlreadyExistsException.class,
                    () -> userDuplicationService.checkIfEmailExists(existingEmail)
            );

            assertTrue(exception.getMessage().contains(existingEmail));
            verify(userRepository, times(1)).findByEmail(existingEmail);
        }

        /**
         * Verifies that the exception message contains the duplicate email.
         */
        @Test
        @DisplayName("Exception message should contain the duplicate email")
        void checkIfEmailExists_emailExists_exceptionMessageContainsEmail() {
            String duplicateEmail = "duplicate@example.com";
            when(userRepository.findByEmail(duplicateEmail)).thenReturn(Optional.of(new UserJPAEntity()));

            EmailAlreadyExistsException exception = assertThrows(
                    EmailAlreadyExistsException.class,
                    () -> userDuplicationService.checkIfEmailExists(duplicateEmail)
            );

            assertEquals("Email already exists: " + duplicateEmail, exception.getMessage());
        }

        /**
         * Verifies behavior with null email (repository handles null).
         */
        @Test
        @DisplayName("Should call repository with null email")
        void checkIfEmailExists_nullEmail_shouldCallRepository() {
            when(userRepository.findByEmail(null)).thenReturn(Optional.empty());

            assertDoesNotThrow(() -> userDuplicationService.checkIfEmailExists(null));

            verify(userRepository, times(1)).findByEmail(null);
        }

        /**
         * Verifies behavior with empty email.
         */
        @Test
        @DisplayName("Should call repository with empty email")
        void checkIfEmailExists_emptyEmail_shouldCallRepository() {
            String emptyEmail = "";
            when(userRepository.findByEmail(emptyEmail)).thenReturn(Optional.empty());

            assertDoesNotThrow(() -> userDuplicationService.checkIfEmailExists(emptyEmail));

            verify(userRepository, times(1)).findByEmail(emptyEmail);
        }

        /**
         * Verifies case sensitivity - repository is called with exact email.
         */
        @Test
        @DisplayName("Should pass email to repository as-is (case sensitive)")
        void checkIfEmailExists_shouldPassEmailAsIs() {
            String email = "User@Example.COM";
            when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

            userDuplicationService.checkIfEmailExists(email);

            verify(userRepository, times(1)).findByEmail(email);
        }
    }

    // ==================== CONSTRUCTOR - MEDIUM PRIORITY ====================

    @Nested
    @DisplayName("Constructor")
    class ConstructorTests {

        /**
         * Verifies that constructor accepts repository dependency.
         */
        @Test
        @DisplayName("Should create service with repository dependency")
        void constructor_withRepository_shouldCreateService() {
            UserDuplicationService service = new UserDuplicationService(userRepository);

            assertNotNull(service);
        }
    }

    // ==================== INTERACTION VERIFICATION - MEDIUM PRIORITY ====================

    @Nested
    @DisplayName("Repository Interaction")
    class RepositoryInteractionTests {

        /**
         * Verifies that repository is called exactly once per check.
         */
        @Test
        @DisplayName("Should call repository exactly once per check")
        void checkIfEmailExists_shouldCallRepositoryOnce() {
            String email = "test@example.com";
            when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

            userDuplicationService.checkIfEmailExists(email);

            verify(userRepository, times(1)).findByEmail(email);
            verifyNoMoreInteractions(userRepository);
        }

        /**
         * Verifies that multiple checks call repository multiple times.
         */
        @Test
        @DisplayName("Multiple checks should call repository multiple times")
        void checkIfEmailExists_multipleCalls_shouldCallRepositoryMultipleTimes() {
            String email1 = "user1@example.com";
            String email2 = "user2@example.com";
            when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

            userDuplicationService.checkIfEmailExists(email1);
            userDuplicationService.checkIfEmailExists(email2);

            verify(userRepository, times(1)).findByEmail(email1);
            verify(userRepository, times(1)).findByEmail(email2);
        }
    }
}

