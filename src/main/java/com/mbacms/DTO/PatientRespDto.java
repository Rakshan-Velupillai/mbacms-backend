package com.mbacms.DTO;

import com.mbacms.enums.Gender;

import java.time.LocalDate;

public record PatientRespDto(

        int id,
        String username,
        String name,
        String address,
        LocalDate dob,
        String phoneNumber,
        String patientCode,
        Gender gender,
        String bloodGroup,
        int insurancePlanCount
) {
}
