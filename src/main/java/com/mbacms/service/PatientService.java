package com.mbacms.service;

import com.mbacms.DTO.*;
import com.mbacms.enums.ActiveStatus;
import com.mbacms.exception.ResourceNotFoundException;
import com.mbacms.mapper.InsurancePlanMapper;
import com.mbacms.mapper.PatientMapper;
import com.mbacms.model.InsurancePlan;
import com.mbacms.model.Patient;
import com.mbacms.model.PatientInsurancePlan;
import com.mbacms.model.User;
import com.mbacms.repository.PatientRepository;
import com.mbacms.repository.PlanRepository;
import com.mbacms.repository.UserRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@AllArgsConstructor
public class PatientService {
    private final UserService userService;
    private final PatientRepository patientRepository;
    private final PatientMapper patientMapper;
    private final UserRepository userRepository;
    private final PlanRepository planRepository;
    private final PatientInsurancePlanService patientInsurancePlanService;
    private final InsurancePlanService insurancePlanService;
    private final InsurancePlanMapper insurancePlanMapper;


    private static final Set<String> mockInsurance = Set.of(
            "POL-10001", "POL-10002", "POL-10003", "POL-10004", "POL-10005",
            "POL-10006", "POL-10007", "POL-10008", "POL-10009", "POL-10010",
            "POL-10011", "POL-10012", "POL-10013", "POL-10014", "POL-10015",
            "POL-10016", "POL-10017", "POL-10018", "POL-10019", "POL-10020"
    );

    public void patientProfile(@Valid PatientReqDto dto, Principal principal) {
        User user=(User)userService.loadUserByUsername(principal.getName());
        Patient patient=new Patient();
        patient.setDob(dto.dob());
        patient.setGender(dto.gender());
        patient.setAddress(dto.address());
        patient.setBloodGroup(dto.bloodGroup());
        patient.setPatientCode("PAT-" + java.util.UUID.randomUUID().toString().substring(0, 8).toUpperCase());


        patient.setUser(user);

        patientRepository.save(patient);

    }


    public PatientRespDto getPatient(String name) {
     User user=(User) userService.loadUserByUsername(name);
     Patient patient=patientRepository.findByUser(user).orElseThrow(() ->
             new RuntimeException("Patient profile not found"));

        List<PatientInsurancePlan> plans =
                patientInsurancePlanService.findByPatient(patient);

        return patientMapper.entityToDto(patient,user,plans.size());
    }

    public PatientRespDto patientUpdate(String name, PatientUpdateReqDto dto) {

        User user=(User) userService.loadUserByUsername(name);

        if(dto.name()!=null) {
            user.setFullName(dto.name());
        }
        if(dto.phoneNumber()!=null){
            user.setPhoneNumber(dto.phoneNumber());
        }
        Patient patient=patientRepository.findByUser(user).orElseThrow(() ->
                new ResourceNotFoundException("Patient profile not found"));
        if (dto.address()!=null){
        patient.setAddress(dto.address());
        }
        if(dto.dob()!=null){
        patient.setDob(dto.dob());
        }
        if(dto.bloodGroup()!=null){
            patient.setBloodGroup(dto.bloodGroup());
        }


        patientRepository.save(patient);
        userRepository.save(user);

        List<PatientInsurancePlan> plans = patientInsurancePlanService.findByPatient(patient);
        return patientMapper.entityToDto(patient, user, plans.size());

    }

    public Patient getPatientById(int id) {

        return patientRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Patient not found"));
    }

    public void selectPlan(SelectPlanReqDto dto, String name) {
        if (dto.endDate().isBefore(dto.startDate()) || dto.endDate().isEqual(dto.startDate())) {
            throw new ResourceNotFoundException("Coverage End Date must be after the Coverage Start Date.");
        }
        User user = userRepository.findByUsername(name)
                .orElseThrow(()->new ResourceNotFoundException("User not found"));

        Patient patient=patientRepository.findByUser(user)
                .orElseThrow(()->new ResourceNotFoundException("Patient not found"));

        InsurancePlan insurancePlan=planRepository.findById(dto.insurancePlanId())
                        .orElseThrow(()->new ResourceNotFoundException("Insurance Plan not found"));

        boolean alreadySelected=patientInsurancePlanService
                        .existsByPatientAndInsurancePlan(patient.getId(), insurancePlan.getId());

        if(alreadySelected) {
            throw new ResourceNotFoundException("Patient has already selected this insurance plan");
        }
        if(patientInsurancePlanService.existsByPolicyNumber(dto.policyNumber())) {
            throw new ResourceNotFoundException("Policy number is not valid");
        }
        //mock insurer db check
        if (!mockInsurance.contains(dto.policyNumber())) {
            throw new ResourceNotFoundException("Insurance verification failed: Policy number does not exist in the Insurer's Database.");
        }


        PatientInsurancePlan patientInsurance = new PatientInsurancePlan();
        patientInsurance.setPatient(patient);
        patientInsurance.setInsurancePlan(insurancePlan);
        patientInsurance.setPolicyNumber(dto.policyNumber());
        patientInsurance.setStartDate(dto.startDate());
        patientInsurance.setEndDate(dto.endDate());
        patientInsurance.setActiveStatus(ActiveStatus.ACTIVE);
        patientInsurancePlanService.save(patientInsurance);


    }

    public Patient getPatientByUsername(String name) {
        return patientRepository.findByUserUsername(name)
                .orElseThrow(()->new ResourceNotFoundException("Patient not found"));
    }

    public List<InsurancePlanDto> getAllInsurancePlans() {
        List<InsurancePlan> planList=insurancePlanService.getAllPlans();
        return planList.stream().map(insurancePlanMapper::entityToDto).toList();
    }



    public List<PatientRespDto> getAllPatients() {
        List<Patient> patients = patientRepository.findAll();
        return patients.stream().map(patient -> {
            User user = patient.getUser();
            List<PatientInsurancePlan> plans = patientInsurancePlanService.findByPatient(patient);
            return patientMapper.entityToDto(patient, user, plans.size());
        }).toList();
    }

    public PatientRespDto verifyPatient(String patientCode) {
        Patient patient = patientRepository.findByPatientCode(patientCode)
                .orElseThrow(() -> new ResourceNotFoundException("Patient profile not found with code: " + patientCode));
        User user = patient.getUser();
        List<PatientInsurancePlan> plans = patientInsurancePlanService.findByPatient(patient);
        return patientMapper.entityToDto(patient, user, plans.size());
    }

    public List<java.util.Map<String, Object>> getPatientPlansList(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Patient patient = patientRepository.findByUser(user)
                .orElseThrow(() -> new ResourceNotFoundException("Patient profile not found"));
        List<PatientInsurancePlan> plans = patientInsurancePlanService.findByPatient(patient);
        return plans.stream().map(p -> {
            java.util.Map<String, Object> map = new java.util.HashMap<>();
            map.put("id", p.getId());
            map.put("policyNumber", p.getPolicyNumber());
            map.put("activeStatus", p.getActiveStatus() != null ? p.getActiveStatus().toString() : null);
            map.put("startDate", p.getStartDate());
            map.put("endDate", p.getEndDate());
            map.put("priorityOrder", p.getPriorityOrder());

            InsurancePlan ip = p.getInsurancePlan();
            if (ip != null) {
                map.put("planName", ip.getPlanName());
                map.put("planType", ip.getPlanType());
                map.put("insurancePlanId", ip.getId());
                if (ip.getInsuranceCompany() != null) {
                    map.put("companyName", ip.getInsuranceCompany().getCompanyName());
                } else {
                    map.put("companyName", null);
                }
            } else {
                map.put("planName", null);
                map.put("planType", null);
                map.put("insurancePlanId", null);
                map.put("companyName", null);
            }
            return map;
        }).toList();
    }
}
