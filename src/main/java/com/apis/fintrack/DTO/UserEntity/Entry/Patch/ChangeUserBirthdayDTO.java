package com.apis.fintrack.DTO.UserEntity.Entry.Patch;


import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChangeUserBirthdayDTO {
    @NotNull
    private LocalDate birthday;
}