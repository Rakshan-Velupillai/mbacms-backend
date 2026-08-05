package com.mbacms.controller;


import com.mbacms.DTO.MedicalServiceReqDto;
import com.mbacms.DTO.MedicalServiceRespDto;
import com.mbacms.service.MedicalServiceService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/medical-service")
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:5173/")

public class MedicalServiceController {

    private final MedicalServiceService medicalServiceService;

    @PostMapping("/add")
    public void addService(Principal principal, @Valid @RequestBody MedicalServiceReqDto dto){

         medicalServiceService.addService(principal.getName(),dto);
    }

    @GetMapping("/getAll")
    public List<MedicalServiceRespDto> getAllServices(){

        return medicalServiceService.getAllServices();
    }

    @GetMapping("/getById/{id}")
    public MedicalServiceRespDto getById(
            @PathVariable int id){

        return medicalServiceService.getServiceById(id);
    }

    @PutMapping("/update/{id}")
    public MedicalServiceRespDto update(
            @PathVariable int id,
            @Valid @RequestBody MedicalServiceReqDto dto){

        return medicalServiceService.updateService(id,dto);
    }

}
