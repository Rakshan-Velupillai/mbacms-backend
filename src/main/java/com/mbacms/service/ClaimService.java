package com.mbacms.service;


import com.mbacms.DTO.ClaimRespDto;
import com.mbacms.enums.ClaimStatus;
import com.mbacms.exception.OwnershipInvalidException;
import com.mbacms.exception.ResourceNotFoundException;
import com.mbacms.mapper.ClaimMapper;
import com.mbacms.model.Claim;
import com.mbacms.model.Invoice;
import com.mbacms.model.Patient;
import com.mbacms.model.PatientInsurancePlan;
import com.mbacms.repository.ClaimRepository;
import com.mbacms.repository.InvoiceRepository;
import com.mbacms.util.FileUtility;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class ClaimService {

    private final ClaimRepository claimRepository;
    private final PatientService patientService;
    private final InvoiceRepository invoiceRepository;
    private final PatientInsurancePlanService patientInsurancePlanService;

    private static final String UPLOAD_LOC = "c:/Users/kelly/OneDrive/Desktop/Anti-mbacms/mbacms/public/uploads";

    public List<Claim> getAll() {
        return claimRepository.findAll();
    }

    public Claim getById(int id) {
        return claimRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No Claim mentioned by this ID!"));
    }

    public void add(Claim claim) {
        claimRepository.save(claim);
    }

    public void deleteById(int id) {
        getById(id);
        claimRepository.deleteById(id);
    }

    public void update(int id, Claim claim) {
        Claim existingClaim = getById(id);
        existingClaim.setClaimAmount(claim.getClaimAmount());
        existingClaim.setClaimStatus(claim.getClaimStatus());
        claimRepository.save(existingClaim);
    }

    public void submitClaim(String username, String invoiceNumber, int patientInsurancePlanId, MultipartFile file) throws IOException {
        if (claimRepository.existsByInvoiceInvoiceNumber(invoiceNumber)) {
            throw new IllegalArgumentException("This invoice has already been claimed.");
        }
        // Fetch patient
        Patient patient = patientService.getPatientByUsername(username);

        // Fetch invoice and validate ownership
        Invoice invoice = invoiceRepository.findByInvoiceNumber(invoiceNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Invoice with number " + invoiceNumber + " not found."));

        if (invoice.getPatient().getId() != patient.getId()) {
            throw new ResourceNotFoundException("This invoice does not belong to you.");
        }

        // Fetch policy and validate ownership
        PatientInsurancePlan policy = patientInsurancePlanService.getById(patientInsurancePlanId);
        if (policy.getPatient().getId() != patient.getId()) {
            throw new ResourceNotFoundException("Selected policy does not belong to you.");
        }

        FileUtility.validateFile(file);

        // Original filename
        String fileName = file.getOriginalFilename();

        // i am creating the path where I will upload the file: destination
        Path uploadPath =  Paths.get(UPLOAD_LOC);

        // Ensure upload directory exists
        java.io.File uploadDir = uploadPath.toFile();
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        // Attach the file name to the upload path
        Path destinationPath =  uploadPath.resolve(fileName);

        // Copy the original file (Multipart) on to destination upload path
        Files.copy(file.getInputStream(), destinationPath, StandardCopyOption.REPLACE_EXISTING);

        // Save the file name in db
        Claim claim = new Claim();
        claim.setClaimNumber("CLM-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        claim.setClaimAmount(invoice.getTotalDueAmount());
        claim.setSubmissionDate(LocalDate.now());
        claim.setClaimStatus(ClaimStatus.SUBMITTED);
        claim.setDocumentUrl("/uploads/" + fileName);
        claim.setInvoice(invoice);
        claim.setPatientInsurancePlan(policy);

        claimRepository.save(claim);
    }

    public List<ClaimRespDto> getPatientClaims(String username) {
        List<Claim> claims = claimRepository.getClaimsByPatientUsername(username);
        return claims.stream().map(ClaimMapper::mapToDto).toList();
    }

    public List<ClaimRespDto> getCompanyClaims(String username) {
        List<Claim> claims = claimRepository.getClaimsByCompanyUsername(username);
        return claims.stream().map(ClaimMapper::mapToDto).toList();
    }

    public void processClaim(String companyUsername, int claimId, ClaimStatus status, String rejectionReason) {
        Claim claim = getById(claimId);

        String assocCompanyUser = claim.getPatientInsurancePlan().getInsurancePlan().getInsuranceCompany().getUser().getUsername();
        if (!assocCompanyUser.equals(companyUsername)) {
            throw new OwnershipInvalidException("This claim does not belong to your insurance company.");
        }

        claim.setClaimStatus(status);
        if (status == ClaimStatus.APPROVED) {
            claim.setApprovedDate(LocalDate.now());
            claim.setRejectionReason(null);
        } else if (status == ClaimStatus.REJECTED) {
            claim.setApprovedDate(null);
            claim.setRejectionReason(rejectionReason);
        }
        claimRepository.save(claim);
    }

}
