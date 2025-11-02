package com.apis.fintrack.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RoleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long roleId;

    @NotNull
    @Enumerated(EnumType.STRING)
    CategoryEnum category;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "role")
    List<UserEntity> usersWithRole;

    @ManyToMany(mappedBy = "rolesWithProperty")
    Set<PropertyEntity> propertyWithRole;


}
