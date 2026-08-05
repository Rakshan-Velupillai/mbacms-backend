package com.mbacms.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ChangePasswordReqDto(
        @NotNull @NotBlank String oldPassword,
        @NotNull @NotBlank String newPassword
) {
}
