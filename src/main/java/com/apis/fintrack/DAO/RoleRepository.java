package com.apis.fintrack.DAO;

import com.apis.fintrack.Entity.RoleEntity;
import com.apis.fintrack.Entity.RoleEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, Long> {
    @Query(value = "Select r from RoleEntity r WHERE r.roleName=?1")
    public RoleEntity findByRoleName(RoleEnum role);

}
