package com.mbacms.DTO;

import com.mbacms.enums.InvoiceStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record InvoiceRespDto(
        int id,
        String invoiceNumber,
        LocalDate invoiceDate,
        LocalDate dueDate,
        BigDecimal subtotal,
        BigDecimal taxRate,
        BigDecimal taxAmount,
        BigDecimal totalDueAmount,
        InvoiceStatus invoiceStatus,
        String patientName,
        String healthcareName,
        String symptomsDesc,
        String treatmentDesc,
        List<MedicalServiceInvoiceRespDto> items
) {
}
