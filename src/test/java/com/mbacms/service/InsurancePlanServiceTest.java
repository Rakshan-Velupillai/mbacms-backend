package com.mbacms.service;

import com.mbacms.DTO.InsurancePlanDto;
import com.mbacms.enums.ActiveStatus;
import com.mbacms.exception.ResourceNotFoundException;
import com.mbacms.model.InsuranceCompany;
import com.mbacms.model.InsurancePlan;
import com.mbacms.repository.InsuranceCompanyRepository;
import com.mbacms.repository.InsurancePlanRepository;
import com.mbacms.mapper.InsurancePlanMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class InsurancePlanServiceTest {

    // 1. which repo to mock
    @Mock
    private InsurancePlanRepository insurancePlanRepository;

    @Mock
    private InsuranceCompanyRepository insuranceCompanyRepository;

    @Mock
    private InsurancePlanMapper insurancePlanMapper;

    // 2. which service are u mocking
    @InjectMocks
    private InsurancePlanService insurancePlanService;

    private InsuranceCompany insuranceCompany;
    private InsurancePlan insurancePlan1;
    private InsurancePlan insurancePlan2;
    private InsurancePlan insurancePlan3;

    // create mock data for testing don't rely on db
    @BeforeEach
    public void sampleData() {
        insuranceCompany = new InsuranceCompany();
        insuranceCompany.setId(100);
        insuranceCompany.setCompanyName("Star Health");
        insuranceCompany.setRegNo("REG123");
        insuranceCompany.setAddress("123 Health Ave");

        insurancePlan1 = new InsurancePlan();
        insurancePlan1.setId(1);
        insurancePlan1.setPlanName("Basic Plan");
        insurancePlan1.setPlanType("Health");
        insurancePlan1.setPlanDesc("Basic coverage plan");
        insurancePlan1.setCoverageAmount(new BigDecimal("5000.00"));
        insurancePlan1.setPremiumAmount(new BigDecimal("50.00"));
        insurancePlan1.setDurationMonths(12);
        insurancePlan1.setActiveStatus(ActiveStatus.ACTIVE);
        insurancePlan1.setInsuranceCompany(insuranceCompany);

        insurancePlan2 = new InsurancePlan();
        insurancePlan2.setId(2);
        insurancePlan2.setPlanName("Premium Plan");
        insurancePlan2.setPlanType("Medical");
        insurancePlan2.setPlanDesc("Premium coverage plan");
        insurancePlan2.setCoverageAmount(new BigDecimal("20000.00"));
        insurancePlan2.setPremiumAmount(new BigDecimal("150.00"));
        insurancePlan2.setDurationMonths(12);
        insurancePlan2.setActiveStatus(ActiveStatus.ACTIVE);
        insurancePlan2.setInsuranceCompany(insuranceCompany);

        insurancePlan3 = new InsurancePlan();
        insurancePlan3.setId(3);
        insurancePlan3.setPlanName("Gold Plan");
        insurancePlan3.setPlanType("Dental");
        insurancePlan3.setPlanDesc("Gold coverage plan");
        insurancePlan3.setCoverageAmount(new BigDecimal("10000.00"));
        insurancePlan3.setPremiumAmount(new BigDecimal("100.00"));
        insurancePlan3.setDurationMonths(12);
        insurancePlan3.setActiveStatus(ActiveStatus.ACTIVE);
        insurancePlan3.setInsuranceCompany(insuranceCompany);
    }

    @Test
    public void getAllPlans_ReturnSomething() {
        when(insurancePlanRepository.findAll()).thenReturn(List.of(insurancePlan1, insurancePlan2));

        List<InsurancePlan> actualCall = insurancePlanService.getAllPlans();

        assertThat(actualCall).hasSize(2);
        assertThat(actualCall.getFirst().getPlanName()).isEqualToIgnoringCase("Basic Plan");
        assertThat(actualCall.get(1).getPlanName()).isEqualToIgnoringCase("Premium Plan");
    }

    @Test
    public void getAllPlans_ReturnEmptyList() {
        when(insurancePlanRepository.findAll()).thenReturn(List.of());

        List<InsurancePlan> actualCall = insurancePlanService.getAllPlans();

        assertThat(actualCall).hasSize(0);
        assertThat(actualCall).isEmpty();
    }

    @Test
    public void getPlansByCompany_companyExists() {
        when(insuranceCompanyRepository.findByUserUsername("starHealth")).thenReturn(insuranceCompany);
        when(insurancePlanRepository.findByInsuranceCompany(insuranceCompany)).thenReturn(List.of(insurancePlan1, insurancePlan2));

        InsurancePlanDto dto1 = new InsurancePlanDto(1, "Basic Plan", "Health", "Basic coverage plan", new BigDecimal("5000.00"), new BigDecimal("50.00"), 12, ActiveStatus.ACTIVE);
        InsurancePlanDto dto2 = new InsurancePlanDto(2, "Premium Plan", "Medical", "Premium coverage plan", new BigDecimal("20000.00"), new BigDecimal("150.00"), 12, ActiveStatus.ACTIVE);

        when(insurancePlanMapper.entityToDto(insurancePlan1)).thenReturn(dto1);
        when(insurancePlanMapper.entityToDto(insurancePlan2)).thenReturn(dto2);

        List<InsurancePlanDto> actualCall = insurancePlanService.getPlansByCompany("starHealth");

        assertThat(actualCall).hasSize(2);
        assertThat(actualCall.getFirst().planName()).isEqualToIgnoringCase("Basic Plan");
        assertThat(actualCall.get(1).planName()).isEqualToIgnoringCase("Premium Plan");
    }

    @Test
    public void getPlansByCompany_companyDoesNotExist() {
        when(insuranceCompanyRepository.findByUserUsername("invalid_company")).thenReturn(null);

        assertThatThrownBy(() -> insurancePlanService.getPlansByCompany("invalid_company"))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Insurance Company not found");
    }

    @Test
    public void addPlan_mustSaveAndReturnPlan() {
        when(insuranceCompanyRepository.findByUserUsername("starHealth")).thenReturn(insuranceCompany);
        when(insurancePlanRepository.save(any(InsurancePlan.class))).thenReturn(insurancePlan3);

        InsurancePlanDto reqDto = new InsurancePlanDto(0, "Gold Plan", "Dental", "Gold coverage plan", new BigDecimal("10000.00"), new BigDecimal("100.00"), 12, ActiveStatus.ACTIVE);
        InsurancePlanDto respDto = new InsurancePlanDto(3, "Gold Plan", "Dental", "Gold coverage plan", new BigDecimal("10000.00"), new BigDecimal("100.00"), 12, ActiveStatus.ACTIVE);

        when(insurancePlanMapper.entityToDto(insurancePlan3)).thenReturn(respDto);

        InsurancePlanDto actualCall = insurancePlanService.addPlan("starHealth", reqDto);

        assertThat(actualCall.planName()).isEqualTo(reqDto.planName());
        assertThat(actualCall.coverageAmount()).isEqualTo(reqDto.coverageAmount());

        verify(insuranceCompanyRepository, times(1)).findByUserUsername("starHealth");
        verify(insurancePlanRepository, times(1)).save(any(InsurancePlan.class));
    }

    @Test
    public void addPlan_companyDoesNotExist() {
        when(insuranceCompanyRepository.findByUserUsername("invalid_company")).thenReturn(null);

        InsurancePlanDto reqDto = new InsurancePlanDto(0, "Gold Plan", "Dental", "Gold coverage plan", new BigDecimal("10000.00"), new BigDecimal("100.00"), 12, ActiveStatus.ACTIVE);

        assertThatThrownBy(() -> insurancePlanService.addPlan("invalid_company", reqDto))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Insurance Company not found");
    }

    @Test
    public void updatePlan_mustUpdateAndReturnPlan() {
        when(insuranceCompanyRepository.findByUserUsername("starHealth")).thenReturn(insuranceCompany);
        when(insurancePlanRepository.findById(1)).thenReturn(Optional.of(insurancePlan1));
        when(insurancePlanRepository.save(any(InsurancePlan.class))).thenReturn(insurancePlan1);

        InsurancePlanDto updateDto = new InsurancePlanDto(1, "Updated Basic Plan", "Health", "Updated basic coverage plan", new BigDecimal("6000.00"), new BigDecimal("60.00"), 12, ActiveStatus.ACTIVE);
        when(insurancePlanMapper.entityToDto(insurancePlan1)).thenReturn(updateDto);

        InsurancePlanDto actualCall = insurancePlanService.updatePlan("starHealth", 1, updateDto);

        assertThat(actualCall.planName()).isEqualTo(updateDto.planName());
        assertThat(actualCall.coverageAmount()).isEqualTo(updateDto.coverageAmount());

        verify(insuranceCompanyRepository, times(1)).findByUserUsername("starHealth");
        verify(insurancePlanRepository, times(1)).findById(1);
        verify(insurancePlanRepository, times(1)).save(any(InsurancePlan.class));
    }

    @Test
    public void updatePlan_unauthorizedAccess() {
        InsuranceCompany anotherCompany = new InsuranceCompany();
        anotherCompany.setId(200);

        InsurancePlan planOfAnotherCompany = new InsurancePlan();
        planOfAnotherCompany.setId(4);
        planOfAnotherCompany.setInsuranceCompany(anotherCompany);

        when(insuranceCompanyRepository.findByUserUsername("starHealth")).thenReturn(insuranceCompany);
        when(insurancePlanRepository.findById(4)).thenReturn(Optional.of(planOfAnotherCompany));

        InsurancePlanDto updateDto = new InsurancePlanDto(4, "Gold Plan", "Dental", "Gold coverage plan", new BigDecimal("10000.00"), new BigDecimal("100.00"), 12, ActiveStatus.ACTIVE);

        assertThatThrownBy(() -> insurancePlanService.updatePlan("starHealth", 4, updateDto))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Unauthorized access to this insurance plan");
    }

    @Test
    public void deletePlan_mustDeleteAndReturnNothing() {
        when(insuranceCompanyRepository.findByUserUsername("starHealth")).thenReturn(insuranceCompany);
        when(insurancePlanRepository.findById(1)).thenReturn(Optional.of(insurancePlan1));
        doNothing().when(insurancePlanRepository).delete(insurancePlan1);

        insurancePlanService.deletePlan("starHealth", 1);

        verify(insuranceCompanyRepository, times(1)).findByUserUsername("starHealth");
        verify(insurancePlanRepository, times(1)).findById(1);
        verify(insurancePlanRepository, times(1)).delete(insurancePlan1);
    }
}
