package com.mbacms.controller;

import com.mbacms.DTO.*;
import com.mbacms.service.PatientService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/patient")
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:5173/")

public class PatientController {

    private final PatientService patientService;


    @PostMapping("/profile")
    public void patientProfile(@Valid @RequestBody PatientReqDto dto, Principal principal) {
        patientService.patientProfile(dto,principal);
    }

    @GetMapping("/getPatient")
    public PatientRespDto getPatient(Principal principal){

        return patientService.getPatient(principal.getName());
    }

    @PatchMapping("/patient-update")
    public PatientRespDto patientUpdate(Principal principal,
                                        @Valid @RequestBody PatientUpdateReqDto dto){

        return patientService.patientUpdate(principal.getName(), dto);

    }

    @PostMapping("/insurance-plan/select")
    public void selectPlan(@Valid @RequestBody SelectPlanReqDto dto, Principal principal){
            patientService.selectPlan(dto,principal.getName());
    }

    @GetMapping("/insurance-plans")
    public List<InsurancePlanDto> getAllInsurancePlans() {
        return patientService.getAllInsurancePlans();
    }

    @GetMapping("/all")
    public List<PatientRespDto> getAllPatients() {
        return patientService.getAllPatients();
    }

    @GetMapping("/verify/{patientCode}")
    public PatientRespDto verifyPatient(@PathVariable String patientCode) {
        return patientService.verifyPatient(patientCode);
    }

    @GetMapping("/my-plans")
    public List<java.util.Map<String, Object>> getMyPlans(Principal principal) {
        return patientService.getPatientPlansList(principal.getName());
    }
}
