package com.apis.fintrack.infrastructure.adapter.input.rest.dto.request.Entry.UserPatch;


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