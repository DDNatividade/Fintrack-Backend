package com.apis.fintrack.domain.property.model;

import com.apis.fintrack.domain.role.model.RoleID;
import lombok.Getter;

import java.util.*;

public class Property {

    // Getters para acceso controlado
    @Getter
    private final PropertyID propertyID;
    private final PropertyEnum defaultPermission; // permiso base o principal
    private final Set<RoleID> rolesWithProperty;
    private final Map<RoleID, Set<PropertyEnum>> permissionsByRole;

    // Constructor
    public Property(PropertyID propertyID, PropertyEnum defaultPermission) {
        this.propertyID = Objects.requireNonNull(propertyID, "PropertyID cannot be null");
        this.defaultPermission = Objects.requireNonNull(defaultPermission, "Default permission cannot be null");
        this.rolesWithProperty = new HashSet<>();
        this.permissionsByRole = new HashMap<>();
    }

    // Getter añadido para defaultPermission
    public PropertyEnum getDefaultPermission() {
        return defaultPermission;
    }

    // Asigna un rol y le da un conjunto de permisos por defecto
    public void assignRole(RoleID roleId) {
        rolesWithProperty.add(roleId);
        permissionsByRole.putIfAbsent(roleId, new HashSet<>(Set.of(defaultPermission)));
    }

    // Permite agregar permisos adicionales a un rol ya asignado
    public void addPermissionsToRole(RoleID roleId, Set<PropertyEnum> permissions) {
        permissionsByRole.computeIfAbsent(roleId, k -> new HashSet<>()).addAll(permissions);
    }

    // Comprueba si alguno de los roles puede realizar la acción solicitada
    public boolean canBeManagedByRoles(Set<RoleID> roleIds, PropertyEnum permission) {
        for (RoleID roleId : roleIds) {
            Set<PropertyEnum> perms = permissionsByRole.get(roleId);
            if (perms != null && permission.isInSet(perms)) {
                return true;
            }
        }
        return false;
    }

    public Set<RoleID> getRolesWithProperty() {
        return Collections.unmodifiableSet(rolesWithProperty);
    }

    public Map<RoleID, Set<PropertyEnum>> getPermissionsByRole() {
        return Collections.unmodifiableMap(permissionsByRole);
    }
}
