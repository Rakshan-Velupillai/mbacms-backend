package com.mbacms.DTO;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record InvoiceReqDto(
        @NotNull(message = "Patient ID selection is required.")
        int patientId,
        @NotNull(message = "Due date is required.")
        @FutureOrPresent(message = "Due date cannot be in the past.")
        LocalDate dueDate,
        @NotNull(message = "Tax rate is required.")
        BigDecimal taxRate,
        @NotBlank(message = "Symptoms description is required.")
        String symptomsDesc,
        @NotBlank(message = "Treatment description is required.")
        String treatmentDesc,
        @NotNull(message = "Medical services list cannot be null.")
        List<MedicalServiceInvoiceItemDto> services
) {
}
