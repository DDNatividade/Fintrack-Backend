package com.apis.fintrack.DTO.UserEntity.Exit;

import com.apis.fintrack.Entity.RoleEntity;
import com.apis.fintrack.Entity.RoleEnum;


import java.math.BigDecimal;


public record ShowUserDTO(
        String name,
        String surname,
        String email,
        BigDecimal availableFunds,
        RoleEnum role

) {
}
