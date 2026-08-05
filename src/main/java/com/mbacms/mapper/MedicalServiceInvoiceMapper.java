package com.mbacms.mapper;

import com.mbacms.DTO.MedicalServiceInvoiceRespDto;
import com.mbacms.model.MedicalServiceInvoice;
import org.springframework.stereotype.Component;

@Component
public class MedicalServiceInvoiceMapper {

    public MedicalServiceInvoiceRespDto entityToDto(MedicalServiceInvoice medicalServiceInvoice){
        return new MedicalServiceInvoiceRespDto(
                medicalServiceInvoice.getId(),
                medicalServiceInvoice.getMedicalService().getServiceName(),
                medicalServiceInvoice.getActualAmount()
        );

    }

}
