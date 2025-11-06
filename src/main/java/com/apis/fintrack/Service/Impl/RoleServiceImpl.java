package com.apis.fintrack.Service.Impl;

import com.apis.fintrack.DAO.RoleRepository;
import com.apis.fintrack.Entity.RoleEntity;
import com.apis.fintrack.Entity.RoleEnum;
import com.apis.fintrack.Service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class RoleServiceImpl implements RoleService {
    @Autowired
    RoleRepository roleRepository;

    @Override
    public void save(RoleEntity roleEntity) {
        roleRepository.save(roleEntity);
    }

    @Override
    public void delete(RoleEntity roleEntity) {
        roleRepository.delete(roleEntity);
    }

    @Override
    public RoleEntity findByRoleName(RoleEnum role) {
        return roleRepository.findByRoleName(role);
    }
}
