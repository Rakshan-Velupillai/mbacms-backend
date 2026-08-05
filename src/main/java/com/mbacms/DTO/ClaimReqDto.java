package com.mbacms.DTO;

import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record ClaimReqDto(
        @NotNull
        int invoiceId,
        @NotNull
        int patientInsurancePlanId,
        @NotNull
        String diagnosis,
        @NotNull
        String treatment,
        @NotNull
        BigDecimal claimAmount,
        @NotNull
        String documentUrl
) {

}
