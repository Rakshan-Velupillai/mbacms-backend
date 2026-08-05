package com.mbacms.DTO;

import com.mbacms.enums.Gender;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record PatientReqDto(
        @NotNull(message = "Date of birth is required.")
        LocalDate dob,
        @NotNull(message = "Gender is required.")
        Gender gender,
        @NotBlank(message = "Address is required.")
        String address,
        @NotBlank(message = "Blood group is required.")
        String bloodGroup
) {
}
