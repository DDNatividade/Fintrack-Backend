package com.apis.fintrack.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @NotEmpty
    @NotBlank
    private String name;

    @NotEmpty
    @NotBlank
    private String surname;

    @Email
    @NotNull
    private String email;


    @NotEmpty
    @NotBlank
    private String password;

    @NotNull
    private LocalDate birthDate;

    @NotNull
    @NotNull
    private  BigDecimal availableFunds;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "roleId")
    private RoleEntity role;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private List<TransactionEntity> transactions;


}
