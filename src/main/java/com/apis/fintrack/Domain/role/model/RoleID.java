package com.apis.fintrack.domain.role.model;

import lombok.Getter;

import java.util.Objects;

@Getter
public class RoleID {
    private final Long id;


    public RoleID(Long id) {
        this.id = id;
    }

    public static RoleID of(Long id){
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("RoleID must be a positive number");
        }
        return new RoleID(id);
    }

    public static RoleID empty(){
        return  new RoleID(null);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        RoleID roleID = (RoleID) o;
        return Objects.equals(id, roleID.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "RoleID{" +
                "id=" + id +
                '}';
    }
}
