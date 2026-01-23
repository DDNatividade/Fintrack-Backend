package com.apis.fintrack.infrastructure.adapter.output.persistence.repository;

import com.apis.fintrack.infrastructure.adapter.output.persistence.entity.RoleJPAEntity;
import com.apis.fintrack.domain.user.model.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<RoleJPAEntity, Long> {
    @Query(value = "Select r from RoleJPAEntity r WHERE r.roleName=?1")
    Optional<RoleJPAEntity> findByRoleName(RoleType role);



}

