package com.apis.fintrack.domain.user.model;

import com.apis.fintrack.domain.user.model.RoleType;
import com.apis.fintrack.domain.shared.model.Money;
import lombok.Getter;

import java.util.Objects;

/**
 * Entidad de dominio User.
 *
 * Representa un usuario del sistema Fintrack.
 * Esta es una entidad PURA sin dependencias de frameworks (JPA, Spring, etc.)
 *
 * Características:
 * - Usa Value Objects para encapsular validaciones
 * - Inmutable en sus identificadores
 * - Métodos de negocio para operaciones del dominio
 */
@Getter
public class User {

    private final UserId id;
    private FullName fullName;
    private Email email;
    private Password password;
    private BirthDate birthDate;
    private Money availableFunds;
    private RoleType role;

    /**
     * Constructor completo para uso de Mappers.
     *
     * @param id identificador del usuario (puede ser null para nuevos usuarios)
     * @param fullName nombre completo
     * @param email email del usuario
     * @param password contraseña
     * @param birthDate fecha de nacimiento
     * @param availableFunds fondos disponibles
+     */
    public User(UserId id, FullName fullName, Email email, Password password,
                BirthDate birthDate, Money availableFunds) {
        this.id = id;
        this.fullName = Objects.requireNonNull(fullName, "El nombre no puede ser nulo");
        this.email = Objects.requireNonNull(email, "El email no puede ser nulo");
        this.password = Objects.requireNonNull(password, "La contraseÃ±a no puede ser nula");
        this.birthDate = Objects.requireNonNull(birthDate, "La fecha de nacimiento no puede ser nula");
        this.availableFunds = availableFunds != null ? availableFunds : Money.zero();
        this.role = RoleType.USER;
    }

    /**
     * Crea un nuevo usuario (sin ID asignado).
     * Se usa para registro de nuevos usuarios.
     */
    public static User create(FullName fullName, Email email, Password password,
                              BirthDate birthDate) {
        return new User(
            UserId.empty(),
            fullName,
            email,
            password,
            birthDate,
            Money.zero()
        );
    }
    

    
    // ==================== MÃ‰TODOS DE NEGOCIO ====================
    
    /**
     * Cambia el nombre del usuario.
     * 
     * @param newName el nuevo nombre
     */
    public void changeName(String newName) {
        this.fullName = this.fullName.withName(newName);
    }
    
    /**
     * Cambia el apellido del usuario.
     * 
     * @param newSurname el nuevo apellido
     */
    public void changeSurname(String newSurname) {
        this.fullName = this.fullName.withSurname(newSurname);
    }
    
    /**
     * Cambia el email del usuario.
     * 
     * @param newEmail el nuevo email
     */
    public void changeEmail(Email newEmail) {
        Objects.requireNonNull(newEmail, "El email no puede ser nulo");
        this.email = newEmail;
    }
    
    /**
     * Cambia la contraseña del usuario.
     * 
     * @param newPassword la nueva contraseÃ±a
     */
    public void changePassword(Password newPassword) {
        Objects.requireNonNull(newPassword, "La contraseÃ±a no puede ser nula");
        this.password = newPassword;
    }
    
    /**
     * Cambia la fecha de nacimiento del usuario.
     * 
     * @param newBirthDate la nueva fecha de nacimiento
     */
    public void changeBirthDate(BirthDate newBirthDate) {
        Objects.requireNonNull(newBirthDate, "La fecha de nacimiento no puede ser nula");
        this.birthDate = newBirthDate;
    }
    
    /**
     * AÃ±ade fondos a la cuenta del usuario.
     * 
     * @param amount la cantidad a aÃ±adir
     * @throws IllegalArgumentException si la cantidad es negativa
     */
    public void addFunds(Money amount) {
        Objects.requireNonNull(amount, "Amount cannot be null");
        if (amount.isNegative()) {
            throw new IllegalArgumentException("Cannot add negative funds");
        }
        this.availableFunds = this.availableFunds.add(amount);
    }
    
    /**
     * Retira fondos de la cuenta del usuario.
     * 
     * @param amount la cantidad a retirar
     * @throws IllegalArgumentException si la cantidad es negativa o excede los fondos disponibles
     */
    public void withdrawFunds(Money amount) {
        Objects.requireNonNull(amount, "Amount cannot be null");
        if (amount.isNegative()) {
            throw new IllegalArgumentException("Cannot withdraw negative funds");
        }
        if (amount.isGreaterThan(this.availableFunds)) {
            throw new IllegalArgumentException("Insufficient funds");
        }
        this.availableFunds = this.availableFunds.subtract(amount);
    }
    
    /**
     * Verifica si el usuario tiene fondos suficientes.
     * 
     * @param amount la cantidad a verificar
     * @return true si tiene fondos suficientes
     */
    public boolean hasSufficientFunds(Money amount) {
        return this.availableFunds.isGreaterThanOrEqual(amount);
    }
    
    /**
     * Cambia el rol del usuario.
     * 
     * @param newRole el nuevo rol
     */
    public void changeRole(RoleType newRole) {
        Objects.requireNonNull(newRole, "El rol no puede ser nulo");
        this.role = newRole;
    }
    
    /**
     * Verifica si el usuario es administrador.
     * 
     * @return true si es admin
     */
    public boolean isAdmin() {
        return role.isAdmin();
    }
    
    /**
     * Verifica si el usuario tiene suscripciÃ³n activa.
     * 
     * @return true si tiene suscripciÃ³n
     */
    public boolean hasActiveSubscription() {
        return role.hasSubscription();
    }
    
    /**
     * Obtiene la edad del usuario.
     * 
     * @return la edad en aÃ±os
     */
    public int getAge() {
        return birthDate.getAge();
    }
    
    /**
     * Verifica si es el cumpleaÃ±os del usuario.
     * 
     * @return true si hoy es su cumpleaÃ±os
     */
    public boolean isBirthdayToday() {
        return birthDate.isBirthdayToday();
    }
    
    // ==================== EQUALS, HASHCODE, TOSTRING ====================
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        // Dos usuarios son iguales si tienen el mismo ID
        // Si ambos no tienen ID, comparamos por email (identificador natural)
        if (id.isEmpty() && user.id.isEmpty()) {
            return Objects.equals(email, user.email);
        }
        return Objects.equals(id, user.id);
    }
    
    @Override
    public int hashCode() {
        return id.isEmpty() ? Objects.hash(email) : Objects.hash(id);
    }
    
    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", fullName=" + fullName +
                ", email=" + email +
                ", role=" + role +
                ", availableFunds=" + availableFunds +
                '}';
    }
}


