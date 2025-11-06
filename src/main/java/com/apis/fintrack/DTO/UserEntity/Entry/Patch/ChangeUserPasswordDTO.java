package com.apis.fintrack.DTO.UserEntity.Entry.Patch;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChangeUserPasswordDTO {
    @NotEmpty
    @NotBlank
    private String password;
}