package com.mbacms.controller;

import com.mbacms.DTO.InsuranceCompanyReqDto;
import com.mbacms.DTO.InsuranceCompanyRespDto;
import com.mbacms.DTO.InsurancePlanDto;
import com.mbacms.service.InsuranceCompanyService;
import com.mbacms.service.InsurancePlanService;
import com.mbacms.service.PatientService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/insurance-company")
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:5173/")

public class InsuranceCompanyController {

    private final InsuranceCompanyService companyService;
    private final InsurancePlanService planService;
    @PostMapping("/user-profile")
    public void completeProfile(@Valid @RequestBody InsuranceCompanyReqDto dto) {
        companyService.completeProfile(dto);
    }

    @GetMapping("/get-profile")
    public InsuranceCompanyRespDto getProfile(Principal principal) {
        return companyService.getProfile(principal.getName());
    }

    @PatchMapping("/update-profile")
    public InsuranceCompanyRespDto updateProfile(Principal principal, @Valid @RequestBody InsuranceCompanyRespDto dto) {
        return companyService.updateProfile(principal.getName(), dto);
    }

    @GetMapping("/plans")
    public List<InsurancePlanDto> getPlans(Principal principal) {
        return planService.getPlansByCompany(principal.getName());
    }

    @PostMapping("/plans")
    public InsurancePlanDto addPlan(Principal principal, @Valid @RequestBody InsurancePlanDto dto) {
        return planService.addPlan(principal.getName(), dto);
    }

    @PutMapping("/plans/{id}")
    public InsurancePlanDto updatePlan(Principal principal, @PathVariable int id, @Valid @RequestBody InsurancePlanDto dto) {
        return planService.updatePlan(principal.getName(), id, dto);
    }

    @DeleteMapping("/plans/{id}")
    public void deletePlan(Principal principal, @PathVariable int id) {
        planService.deletePlan(principal.getName(), id);
    }
}
