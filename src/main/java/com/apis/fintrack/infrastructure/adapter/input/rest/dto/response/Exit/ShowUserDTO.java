package com.apis.fintrack.infrastructure.adapter.input.rest.dto.response.Exit;

import com.apis.fintrack.domain.user.model.RoleType;

import java.math.BigDecimal;


public record ShowUserDTO(
        String name,
        String surname,
        String email,
        BigDecimal availableFunds,
        RoleType role

) {
}

