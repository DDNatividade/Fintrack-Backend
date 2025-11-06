package com.apis.fintrack.Service;

import com.apis.fintrack.Entity.RoleEntity;
import com.apis.fintrack.Entity.RoleEnum;

public interface RoleService {
    void save(RoleEntity roleEntity);
    void delete(RoleEntity roleEntity);
    RoleEntity findByRoleName(RoleEnum role);
}
