package com.mbacms.service;

import com.mbacms.DTO.MedicalServiceReqDto;
import com.mbacms.DTO.MedicalServiceRespDto;
import com.mbacms.exception.ResourceNotFoundException;
import com.mbacms.mapper.MedicalServiceMapper;
import com.mbacms.model.Healthcare;
import com.mbacms.model.MedicalService;
import com.mbacms.repository.MedicalServiceRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class MedicalServiceService {


    private final HealthcareService healthcareService;
    private final MedicalServiceRepository medicalServiceRepository;
    private final MedicalServiceMapper medicalServiceMapper;

    public void addService(String name,@Valid MedicalServiceReqDto dto) {
        Healthcare healthcare=healthcareService.getHealthcareByName(name);
        MedicalService medicalService=new MedicalService();

        medicalService.setServiceName(dto.serviceName());
        medicalService.setDescription(dto.description());
        medicalService.setBasePrice(dto.basePrice());

        medicalServiceRepository.save(medicalService);

    }

    public List<MedicalServiceRespDto> getAllServices() {
        return medicalServiceRepository.findAll()
                        .stream()
                .map(medicalServiceMapper::entityToDto)
                .toList();
    }

    public MedicalServiceRespDto getServiceById(int id) {

        MedicalService medicalService= medicalServiceRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("Medical Service Not Found"));

        return medicalServiceMapper.entityToDto(medicalService);
    }

    public MedicalServiceRespDto updateService(int id, @Valid MedicalServiceReqDto dto) {

        MedicalService medicalService =medicalServiceRepository.findById(id)
                        .orElseThrow(() -> new RuntimeException("Medical Service Not Found"));

        medicalService.setServiceName(dto.serviceName());
        medicalService.setDescription(dto.description());
        medicalService.setBasePrice(dto.basePrice());

        medicalService = medicalServiceRepository.save(medicalService);
        return medicalServiceMapper.entityToDto(medicalService);

    }


    public MedicalService getMedicalServiceById(@NotNull int id) {
        return medicalServiceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Medical Service Not Found"));
    }

}
