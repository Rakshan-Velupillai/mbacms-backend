package com.mbacms.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record MedicalServiceReqDto(
        @NotBlank
        String serviceName,
        @NotBlank
        String description,
        @NotNull
        BigDecimal basePrice
) {
}
