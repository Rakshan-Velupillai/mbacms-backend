package com.mbacms.DTO;

import com.mbacms.enums.ActiveStatus;

import java.math.BigDecimal;

public record InsurancePlanDto(
        int id,
        String planName,
        String planType,
        String planDescription,
        BigDecimal coverageAmount,
        BigDecimal premiumAmount,
        int durationMonths,
        ActiveStatus activeStatus
) {
}
