package com.mbacms.DTO;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public record SelectPlanReqDto(

        @Min(value = 1, message = "Insurance Plan selection is required.")
        int insurancePlanId,
        @NotBlank(message = "Policy number is required.")
        String policyNumber,
        @NotNull(message = "Coverage Start Date is required.")
        LocalDate startDate,
        @NotNull(message = "Coverage End Date is required.")
        LocalDate endDate
) {
}
