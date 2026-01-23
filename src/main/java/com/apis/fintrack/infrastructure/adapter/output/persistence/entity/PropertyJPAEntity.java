package com.apis.fintrack.infrastructure.adapter.output.persistence.entity;

import com.apis.fintrack.infrastructure.security.model.PropertyEnum;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PropertyJPAEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long propertyId;

    @NotNull
    @Enumerated(EnumType.STRING)
    private PropertyEnum property;

    @ManyToMany
    @JoinTable(
            name = "Properties_Roles",
            joinColumns = @JoinColumn(name = "propertyId"),
            inverseJoinColumns = @JoinColumn(name = "roleId")
    )
    Set<RoleJPAEntity> rolesWithProperty;
}

