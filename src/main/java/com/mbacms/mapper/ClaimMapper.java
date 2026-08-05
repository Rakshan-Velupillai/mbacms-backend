package com.mbacms.mapper;

import com.mbacms.DTO.ClaimRespDto;
import com.mbacms.model.Claim;
import org.springframework.stereotype.Component;

@Component
public class ClaimMapper {

    public static ClaimRespDto mapToDto(Claim c){

        return new ClaimRespDto(
                c.getId(),
                c.getPatientInsurancePlan().getPatient().getUser().getFullName(),
                c.getPatientInsurancePlan().getInsurancePlan().getPlanName(),
                c.getClaimNumber(),
                c.getInvoice().getInvoiceNumber(),
                c.getPatientInsurancePlan().getPolicyNumber(),
                c.getClaimAmount(),
                c.getInvoice().getSymptomsDesc(),
                c.getInvoice().getTreatmentDesc(),
                c.getSubmissionDate(),
                c.getApprovedDate(),
                c.getClaimStatus(),
                c.getRejectionReason(),
                c.getDocumentUrl()
        );

    }
}
