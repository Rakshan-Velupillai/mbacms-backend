package com.mbacms.service;

import com.mbacms.exception.ResourceNotFoundException;
import com.mbacms.model.InsurancePlan;
import com.mbacms.model.Patient;
import com.mbacms.model.PatientInsurancePlan;
import com.mbacms.repository.PatientInsurancePlanRepository;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class PatientInsurancePlanService {
        private final PatientInsurancePlanRepository planRepository;
    public void save(PatientInsurancePlan patientInsurance) {
        planRepository.save(patientInsurance);
    }

    public boolean existsByPolicyNumber(@NotNull String s) {
        return planRepository.existsByPolicyNumber(s);
    }


    public PatientInsurancePlan getById(int id) {
        return planRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("No policy found"));

    }



    public boolean existsByPatientAndInsurancePlan(int p_id, int in_id) {
        return planRepository.existsByPatientIdAndInsurancePlanId(p_id,in_id);
    }

    public List<PatientInsurancePlan> findByPatient(Patient patient) {
        return planRepository.findByPatient(patient);
    }
}
