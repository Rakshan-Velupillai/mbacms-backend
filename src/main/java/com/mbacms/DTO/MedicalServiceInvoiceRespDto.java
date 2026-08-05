package com.mbacms.DTO;

import java.math.BigDecimal;

public record MedicalServiceInvoiceRespDto(
        int id,
        String serviceName,
        BigDecimal actualAmount
) {
}
