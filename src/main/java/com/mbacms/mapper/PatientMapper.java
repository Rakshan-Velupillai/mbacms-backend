package com.mbacms.mapper;

import com.mbacms.DTO.PatientRespDto;
import com.mbacms.model.Patient;
import com.mbacms.model.User;
import org.springframework.stereotype.Component;

@Component
public class PatientMapper{


    public PatientRespDto entityToDto(Patient patient, User user,int planCount) {

        return new PatientRespDto(
                patient.getId(),
                user.getUsername(),
                user.getFullName(),
                patient.getAddress(),
                patient.getDob(),
                user.getPhoneNumber(),
                patient.getPatientCode(),
                patient.getGender(),
                patient.getBloodGroup(),
                planCount

        );

    }
}
