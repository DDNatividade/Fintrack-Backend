package com.apis.fintrack.infrastructure.adapter.output.persistence.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserJPAEntity implements UserDetails {



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
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate birthDate;

    @NotNull
    private  BigDecimal availableFunds;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "roleId")
    private RoleJPAEntity role;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private List<TransactionJPAEntity> transactions;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "costumer", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<PaymentJPAEntity> payments;

    @OneToOne(mappedBy = "costumer", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonIgnore
    private SubscriptionJPAEntity subscription;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(role);
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return true; }


    /*PRE-PERSIST CONDITIONS*/

    @PrePersist
    public void prePersistDefaults() {
        if (availableFunds == null) {
            availableFunds = BigDecimal.ZERO;
        }
        if (role == null) {
            RoleJPAEntity defaultRole = new RoleJPAEntity();
            defaultRole.setRoleId(1L);
            this.role = defaultRole;
        }
    }



}

