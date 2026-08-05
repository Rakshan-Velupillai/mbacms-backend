package com.mbacms.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record PatientUpdateReqDto(

        @NotBlank(message = "Name is required.")
        String name,
        @NotBlank(message = "Address is required.")
        String address,
        @NotNull(message = "Date of birth is required.")
        LocalDate dob,
        @NotBlank(message = "Phone number is required.")
        String phoneNumber,
        @NotBlank(message = "Blood group is required.")
        String bloodGroup
) {
}
