package com.apis.fintrack.DTO.UserEntity.Entry;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateUserDTO {
    @NotEmpty
    @NotBlank
    private String name;

    @NotEmpty
    @NotBlank
    private String surname;

    @NotEmpty
    @NotBlank
    private String email;

    @NotEmpty
    @NotBlank
    private String password;

    @NotEmpty
    @NotBlank
    private LocalDate birthday;
}
