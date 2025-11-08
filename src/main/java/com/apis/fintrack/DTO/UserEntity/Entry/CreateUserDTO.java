package com.apis.fintrack.DTO.UserEntity.Entry;

import com.apis.fintrack.DAO.RoleRepository;
import com.apis.fintrack.Entity.RoleEntity;
import com.apis.fintrack.Entity.RoleEnum;
import com.apis.fintrack.Service.Impl.RoleServiceImpl;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.bind.DefaultValue;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserDTO {



    private String name;

    private String surname;

    private String email;

    private String password;

    private RoleEnum role;


}
