package com.apis.fintrack.domain.role.model;

import com.apis.fintrack.domain.property.model.PropertyEnum;
import lombok.Getter;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Getter
public class Role {
    private final RoleID roleId;
    private RoleType roleType;
    private Set<PropertyEnum> properties;

    public Role(RoleID roleId) {
        this.roleId = roleId;
        this.properties = new HashSet<>();
    }

    public void changeRoleType(RoleType roleType){
        this.roleType = roleType;
    }


    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Role role = (Role) o;
        return Objects.equals(roleId, role.roleId) && roleType == role.roleType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(roleId, roleType);
    }

    @Override
    public String toString() {
        return "Role{" +
                "roleId=" + roleId +
                ", roleType=" + roleType +
                '}';
    }
}
