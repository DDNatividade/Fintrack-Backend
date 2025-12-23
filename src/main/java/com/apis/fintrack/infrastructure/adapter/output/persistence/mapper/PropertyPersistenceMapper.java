package com.apis.fintrack.infrastructure.adapter.output.persistence.mapper;

import com.apis.fintrack.domain.property.model.Property;
import com.apis.fintrack.domain.property.model.PropertyEnum;
import com.apis.fintrack.domain.property.model.PropertyID;
import com.apis.fintrack.domain.role.model.RoleID;
import com.apis.fintrack.infrastructure.adapter.output.persistence.entity.PropertyJPAEntity;
import com.apis.fintrack.infrastructure.adapter.output.persistence.entity.RoleJPAEntity;
import java.util.Objects;

/**
 * Mapper puro que convierte entre PropertyJPAEntity y Property (dominio).
 * - No depende de Spring/JPA
 * - No contiene lógica de negocio, sólo transformación de datos
 * - No mapea relaciones complejas más allá de los IDs
 */
public final class PropertyMapper {

    private PropertyMapper() {}

    /**
     * Convierte una entidad JPA a la entidad de dominio.
     * - Mapea propertyId ↔ PropertyID
     * - Mapea property ↔ defaultPermission
     * - Mapea rolesWithProperty ↔ Set<RoleID> (en dominio)
     * - Inicializa permissionsByRole en el dominio usando defaultPermission para cada rol
     */
    public static Property toDomain(PropertyJPAEntity entity) {
        Objects.requireNonNull(entity, "PropertyJPAEntity cannot be null");
        // propertyId
        PropertyID propertyID = null;
        if (entity.getPropertyId() != null) {
            propertyID = PropertyID.of(entity.getPropertyId());
        }

        // defaultPermission (PropertyEnum) — en JPA ya usamos el enum del dominio
        PropertyEnum defaultPermission = Objects.requireNonNull(entity.getProperty(), "Property enum cannot be null");

        Property domain = new Property(propertyID, defaultPermission);

        // map rolesWithProperty -> Set<RoleID>
        if (entity.getRolesWithProperty() != null) {
            for (RoleJPAEntity roleEntity : entity.getRolesWithProperty()) {
                if (roleEntity != null && roleEntity.getRoleId() != null) {
                    RoleID roleId = RoleID.of(roleEntity.getRoleId());
                    domain.assignRole(roleId); // assignRole inicializa permissionsByRole con defaultPermission
                }
            }
        }

        return domain;
    }

    /**
     * Convierte una entidad de dominio a su equivalente JPA.
     * - Mapea PropertyID -> propertyId
     * - Mapea defaultPermission -> property
     * - NO mapea rolesWithProperty (se deja en null para que el adaptador gestione las relaciones)
     */
    public static PropertyJPAEntity toEntity(Property domain) {
        if (domain == null) return null;

        PropertyJPAEntity entity = new PropertyJPAEntity();

        // propertyId
        if (domain.getPropertyID() != null && domain.getPropertyID().getId() != null) {
            entity.setPropertyId(domain.getPropertyID().getId());
        }

        // defaultPermission
        entity.setProperty(domain.getDefaultPermission());

        // No mapear rolesWithProperty aquí; la gestión de relaciones debe hacerse en el adaptador/infrastructure
        entity.setRolesWithProperty(null);

        return entity;
    }
}

