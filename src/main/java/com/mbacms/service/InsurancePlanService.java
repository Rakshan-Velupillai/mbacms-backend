package com.mbacms.service;

import com.mbacms.DTO.InsurancePlanDto;
import com.mbacms.enums.ActiveStatus;
import com.mbacms.exception.ResourceNotFoundException;
import com.mbacms.mapper.InsurancePlanMapper;
import com.mbacms.model.InsuranceCompany;
import com.mbacms.model.InsurancePlan;
import com.mbacms.repository.InsuranceCompanyRepository;
import com.mbacms.repository.InsurancePlanRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class InsurancePlanService {

    private final InsurancePlanRepository insurancePlanRepository;
    private final InsuranceCompanyRepository insuranceCompanyRepository;
    private final InsurancePlanMapper insurancePlanMapper;


    public List<InsurancePlan> getAllPlans() {

        return insurancePlanRepository.findAll();
    }

    public List<InsurancePlanDto> getPlansByCompany(String username) {
        InsuranceCompany company = insuranceCompanyRepository.findByUserUsername(username);
        if (company == null) {
            throw new ResourceNotFoundException("Insurance Company not found");
        }
        return insurancePlanRepository.findByInsuranceCompany(company)
                .stream()
                .map(insurancePlanMapper::entityToDto)
                .toList();
    }

    public InsurancePlanDto addPlan(String username, InsurancePlanDto dto) {
        InsuranceCompany company = insuranceCompanyRepository.findByUserUsername(username);
        if (company == null) {
            throw new ResourceNotFoundException("Insurance Company not found");
        }
        InsurancePlan plan = new InsurancePlan();
        plan.setPlanName(dto.planName());
        plan.setPlanType(dto.planType());
        plan.setPlanDesc(dto.planDescription());
        plan.setCoverageAmount(dto.coverageAmount());
        plan.setPremiumAmount(dto.premiumAmount());
        plan.setDurationMonths(dto.durationMonths());
        plan.setActiveStatus(dto.activeStatus());
        plan.setInsuranceCompany(company);

        InsurancePlan saved = insurancePlanRepository.save(plan);
        return insurancePlanMapper.entityToDto(saved);
    }

    public InsurancePlanDto updatePlan(String username, int id, InsurancePlanDto dto) {
        InsuranceCompany company = insuranceCompanyRepository.findByUserUsername(username);
        if (company == null) {
            throw new ResourceNotFoundException("Insurance Company not found");
        }
        InsurancePlan plan = insurancePlanRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Insurance Plan not found"));

        if (plan.getInsuranceCompany().getId() != company.getId()) {
            throw new RuntimeException("Unauthorized access to this insurance plan");
        }

        if (dto.planName() != null) plan.setPlanName(dto.planName());
        if (dto.planType() != null) plan.setPlanType(dto.planType());
        if (dto.planDescription() != null) plan.setPlanDesc(dto.planDescription());
        if (dto.coverageAmount() != null) plan.setCoverageAmount(dto.coverageAmount());
        if (dto.premiumAmount() != null) plan.setPremiumAmount(dto.premiumAmount());
        if (dto.durationMonths() > 0) plan.setDurationMonths(dto.durationMonths());
        if (dto.activeStatus() != null) plan.setActiveStatus(dto.activeStatus());

        InsurancePlan saved = insurancePlanRepository.save(plan);
        return insurancePlanMapper.entityToDto(saved);
    }

    public void deletePlan(String username, int id) {
        InsuranceCompany company = insuranceCompanyRepository.findByUserUsername(username);
        if (company == null) {
            throw new ResourceNotFoundException("Insurance Company not found");
        }
        InsurancePlan plan = insurancePlanRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Insurance Plan not found"));

        if (plan.getInsuranceCompany().getId() != company.getId()) {
            throw new RuntimeException("Unauthorized access to this insurance plan");
        }

        insurancePlanRepository.delete(plan);
    }
}
