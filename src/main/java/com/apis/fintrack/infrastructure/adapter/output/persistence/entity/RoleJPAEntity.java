package com.apis.fintrack.infrastructure.adapter.output.persistence.entity;

import com.apis.fintrack.domain.user.model.RoleType;
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
public class RoleJPAEntity implements GrantedAuthority {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long roleId;

    @NotNull
    @Enumerated(EnumType.STRING)
    RoleType roleName;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "role")
    List<UserJPAEntity> usersWithRole;

    @ManyToMany(mappedBy = "rolesWithProperty")
    Set<PropertyJPAEntity> propertyWithRole;


    @Override
    public String getAuthority() {
        return roleName.name();
    }
}


