package com.apis.fintrack.infrastructure.adapter.output.persistence.mapper;

import com.apis.fintrack.domain.role.model.Role;
import com.apis.fintrack.domain.role.model.RoleID;
import com.apis.fintrack.domain.role.model.RoleType;
import com.apis.fintrack.infrastructure.adapter.output.persistence.entity.RoleJPAEntity;

import java.util.Objects;

/**
 * Mapper explícito para convertir entre RoleJPAEntity (persistencia) y Role (dominio).
 * - No usa Spring ni JPA
 * - No contiene lógica de negocio, solo copiado de datos
 * - No mapea relaciones JPA (usuarios, properties), solo los campos primarios
 */
public final class RoleMapper {

    private RoleMapper() {
        // utilidad estática
    }

    /**
     * Convierte una entidad JPA a la entidad de dominio.
     * @param entity RoleJPAEntity (puede ser null)
     * @return Role de dominio (null si la entrada es null)
     */
    public static Role toDomain(RoleJPAEntity entity) {
        if (entity == null) return null;

        RoleID roleId = null;
        Long rawId = entity.getRoleId();
        if (rawId > 0) {
            roleId = RoleID.of(Long.valueOf(rawId));
        }

        Role role = new Role(roleId);

        // Mapear roleName ↔ RoleType
        RoleType roleType = entity.getRoleName();
        if (roleType != null) {
            role.changeRoleType(roleType);
        }

        // No mapeamos usersWithRole ni propertyWithRole aquí.
        return role;
    }

    /**
     * Convierte una entidad de dominio a su equivalente JPA.
     * @param domain Role de dominio (puede ser null)
     * @return RoleJPAEntity (null si la entrada es null)
     */
    public static RoleJPAEntity toEntity(Role domain) {
        Objects.requireNonNull(domain, "El rol de dominio no puede ser null");

        RoleJPAEntity entity = new RoleJPAEntity();

        // Mapear RoleID ↔ roleId
        RoleID domainId = domain.getRoleId();
        if (domainId != null && domainId.getId() != null) {
            entity.setRoleId(domainId.getId());
        }

        // Mapear RoleType ↔ roleName
        entity.setRoleName(domain.getRoleType());

        // No mapear relaciones JPA (usersWithRole, propertyWithRole)
        return entity;
    }
}
