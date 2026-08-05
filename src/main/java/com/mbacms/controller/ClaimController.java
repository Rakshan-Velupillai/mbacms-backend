package com.mbacms.controller;


import com.mbacms.DTO.ClaimReqDto;
import com.mbacms.DTO.ClaimRespDto;
import com.mbacms.enums.ClaimStatus;
import com.mbacms.model.Claim;
import com.mbacms.service.ClaimService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/claim")
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:5173/")

public class ClaimController {

    private final ClaimService claimService;

    @PostMapping(value = "/submit", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void submitClaim(
            Principal principal,
            @RequestParam("invoiceNumber") String invoiceNumber,
            @RequestParam("patientInsurancePlanId") int patientInsurancePlanId,
            @RequestParam("file") MultipartFile file
    ) throws IOException {
        claimService.submitClaim(principal.getName(), invoiceNumber, patientInsurancePlanId, file);
    }

    @GetMapping("/patient-claims")
    public List<ClaimRespDto> getPatientClaims(Principal principal) {
        return claimService.getPatientClaims(principal.getName());
    }

    @GetMapping("/company-claims")
    public List<ClaimRespDto> getCompanyClaims(Principal principal) {
        return claimService.getCompanyClaims(principal.getName());
    }

    @PostMapping("/process/{id}")
    public void processClaim(
            Principal principal,
            @PathVariable int id,
            @RequestParam("status") ClaimStatus status,
            @RequestParam(value = "rejectionReason", required = false) String rejectionReason
    ) {
        claimService.processClaim(principal.getName(), id, status, rejectionReason);
    }
}
