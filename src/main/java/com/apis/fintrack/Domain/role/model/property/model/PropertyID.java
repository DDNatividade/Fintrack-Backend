package com.apis.fintrack.domain.user.model.role.model.property.model;

import lombok.Getter;

import java.util.Objects;

@Getter
public class PropertyID {
    private final Long id;

    private PropertyID(Long id) {
        Objects.requireNonNull(id, "PropertyID id cannot be null");
        this.id = id;
    }

    public static PropertyID of(Long id) {
        return new PropertyID(id);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        PropertyID that = (PropertyID) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
