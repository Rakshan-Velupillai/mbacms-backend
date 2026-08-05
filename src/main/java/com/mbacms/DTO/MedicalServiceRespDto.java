package com.mbacms.DTO;

import java.math.BigDecimal;

public record MedicalServiceRespDto(
        int id,
        String serviceName,
        String description,
        BigDecimal basePrice
) {
}
