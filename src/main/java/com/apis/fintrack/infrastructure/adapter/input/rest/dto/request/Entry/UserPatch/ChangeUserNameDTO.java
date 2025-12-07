package com.apis.fintrack.infrastructure.adapter.input.rest.dto.request.Entry.UserPatch;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChangeUserNameDTO {
    @NotEmpty
    @NotBlank
    private String name;
}
