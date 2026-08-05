package com.mbacms.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UserReqDto(
        @NotBlank(message = "Full name is required.")
        String fullName,
        @NotBlank(message = "Username is required.")
        String username,
        @NotBlank(message = "Email is required.")
        String email,
        @NotBlank(message = "Password is required.")
        String password,
        @NotBlank(message = "Phone number is required.")
        String phoneNumber
) {
}
