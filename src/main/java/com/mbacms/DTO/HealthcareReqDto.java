package com.mbacms.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record HealthcareReqDto(
        //user account
        @NotBlank(message = "Contact Person full name is required.")
        String fullName,
        @NotBlank(message = "Username is required.")
        String username,
        @NotBlank(message = "Email is required.")
        String email,
        @NotBlank(message = "Password is required.")
        String password,
        @NotBlank(message = "Phone number is required.")
        String phoneNumber,
        //healthcare profile
        @NotBlank(message = "Healthcare organization name is required.")
        String healthcareName,
        @NotBlank(message = "Specialization is required.")
        String specialization,
        @NotBlank(message = "License number is required.")
        String licenseNumber,
        @NotBlank(message = "Address is required.")
        String address
) {
}
