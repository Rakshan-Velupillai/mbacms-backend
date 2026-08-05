package com.mbacms.DTO;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record MedicalServiceInvoiceItemDto(
        @NotNull int medicalServiceId,
        @NotNull BigDecimal actualAmount
) {
}
