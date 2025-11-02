package com.apis.fintrack.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long userId;

    @NotEmpty
    @NotBlank
    String name;

    @NotEmpty
    @NotBlank
    String surname;

    @Email
    @NotNull
    String email;


    @NotEmpty
    @NotBlank
    String password;

    @NotNull
    LocalDate birthDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "roleId")
    private RoleEntity role;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private List<TransactionEntity> transactions;


}
