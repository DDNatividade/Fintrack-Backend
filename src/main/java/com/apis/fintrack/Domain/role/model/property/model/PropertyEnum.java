package com.apis.fintrack.domain.user.model.role.model.property.model;

public enum PropertyEnum {
    READ,
    WRITE,
    DELETE,
    UPDATE,
    CREATE;

    public void selectProperty(String propertyName) {
        for (PropertyEnum property : PropertyEnum.values()) {
            if (property.name().equalsIgnoreCase(propertyName)) {
                System.out.println("Propiedad seleccionada: " + property.name());
                return;
            }
        }
        System.out.println("Propiedad no encontrada: " + propertyName);
    }


}
