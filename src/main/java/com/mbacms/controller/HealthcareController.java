package com.mbacms.controller;


import com.mbacms.DTO.HealthcareReqDto;
import com.mbacms.DTO.HealthcareRespDto;
import com.mbacms.service.HealthcareService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/healthcare")
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:5173/")

public class HealthcareController {

    private final HealthcareService healthcareService;


    @PostMapping("/user-profile")
    public void healthcareProfile(@Valid @RequestBody HealthcareReqDto dto) {
        healthcareService.healthcareProfile(dto);
    }

    @GetMapping("/get-healthcare")
    public HealthcareRespDto getHealthcare(Principal principal){
        return healthcareService.getHealthcare(principal.getName());
    }

    @PatchMapping("/update-profile")
    public HealthcareRespDto updateProfile(Principal principal, @Valid @RequestBody HealthcareRespDto dto) {
        return healthcareService.updateProfile(principal.getName(), dto);
    }
}
