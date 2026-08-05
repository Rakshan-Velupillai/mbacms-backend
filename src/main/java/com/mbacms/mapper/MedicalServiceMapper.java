package com.mbacms.mapper;


import com.mbacms.DTO.MedicalServiceRespDto;
import com.mbacms.model.MedicalService;
import org.springframework.stereotype.Component;

@Component
public class MedicalServiceMapper {

    public MedicalServiceRespDto entityToDto(MedicalService medicalService){
        return new MedicalServiceRespDto(
                medicalService.getId(),
                medicalService.getServiceName(),
                medicalService.getDescription(),
                medicalService.getBasePrice()
        );
    }
}
