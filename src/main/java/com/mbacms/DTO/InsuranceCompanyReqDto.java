package com.mbacms.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record InsuranceCompanyReqDto(
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
        //insurance company
        @NotBlank(message = "Company name is required.")
        String companyName,
        @NotBlank(message = "Registration number is required.")
        String regNo,
        @NotBlank(message = "Address is required.")
        String address
) {
}
