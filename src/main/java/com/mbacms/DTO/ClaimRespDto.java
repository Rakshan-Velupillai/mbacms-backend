package com.mbacms.DTO;

import com.mbacms.enums.ClaimStatus;

import java.math.BigDecimal;
import java.time.LocalDate;

public record ClaimRespDto(

    int id,
    String patientName,
    String insurancePlanName,
    String claimNumber,
    String invoiceNumber,
    String policyNumber,
    BigDecimal claimAmount,
    String diagnosis,
    String treatment,
    LocalDate submissionDate,
    LocalDate approvedDate,
    ClaimStatus claimStatus,
    String rejectionReason,
    String documentUrl
    )
{
}
