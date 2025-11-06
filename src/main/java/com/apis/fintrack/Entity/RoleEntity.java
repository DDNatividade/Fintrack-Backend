package com.apis.fintrack.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import java.util.List;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleEntity implements GrantedAuthority {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int roleId;

    @NotNull
    @Enumerated(EnumType.STRING)
    RoleEnum roleName;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "role")
    List<UserEntity> usersWithRole;

    @ManyToMany(mappedBy = "rolesWithProperty")
    Set<PropertyEntity> propertyWithRole;


    @Override
    public String getAuthority() {
        return roleName.name();
    }
}
