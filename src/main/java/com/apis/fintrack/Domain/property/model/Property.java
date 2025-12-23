package com.apis.fintrack.domain.user.model.role.model.property.model;

import com.apis.fintrack.domain.user.model.role.model.Role;
import com.apis.fintrack.domain.user.model.role.model.RoleID;

import java.util.Objects;
import java.util.Set;

public class Property {
    private final PropertyID propertyID;
    private final PropertyEnum propertyEnum;
    private final Set<Role> rolesWithProperty;

    public Property(PropertyID propertyID, PropertyEnum propertyEnum, Set<Role> rolesWithProperty) {
        this.propertyID = propertyID;
        this.propertyEnum = propertyEnum;
        this.rolesWithProperty = rolesWithProperty;
    }


}
