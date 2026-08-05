package com.mbacms.service;

import com.mbacms.DTO.MedicalServiceReqDto;
import com.mbacms.DTO.MedicalServiceRespDto;
import com.mbacms.exception.ResourceNotFoundException;
import com.mbacms.model.Healthcare;
import com.mbacms.model.MedicalService;
import com.mbacms.repository.MedicalServiceRepository;
import com.mbacms.mapper.MedicalServiceMapper;
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
public class MedicalServiceServiceTest {

    // 1. which repo to mock
    @Mock
    private MedicalServiceRepository medicalServiceRepository;

    @Mock
    private HealthcareService healthcareService;

    @Mock
    private MedicalServiceMapper medicalServiceMapper;

    // 2. which service are u mocking
    @InjectMocks
    private MedicalServiceService medicalServiceService;

    private MedicalService medicalService1;
    private MedicalService medicalService2;
    private MedicalService medicalService3;

    // create mock data for testing don't rely on db
    @BeforeEach
    public void sampleData() {
        medicalService1 = new MedicalService();
        medicalService1.setId(1);
        medicalService1.setServiceName("Diagnostic Scan");
        medicalService1.setDescription("Full body MRI scan");
        medicalService1.setBasePrice(new BigDecimal("150.00"));

        medicalService2 = new MedicalService();
        medicalService2.setId(2);
        medicalService2.setServiceName("Blood Test");
        medicalService2.setDescription("Complete blood panel test");
        medicalService2.setBasePrice(new BigDecimal("50.00"));

        medicalService3 = new MedicalService();
        medicalService3.setId(3);
        medicalService3.setServiceName("X-Ray");
        medicalService3.setDescription("Chest scan");
        medicalService3.setBasePrice(new BigDecimal("80.00"));
    }

    @Test
    public void getAllServices_ReturnSomething() {
        when(medicalServiceRepository.findAll()).thenReturn(List.of(medicalService1, medicalService2));

        MedicalServiceRespDto dto1 = new MedicalServiceRespDto(1, "Diagnostic Scan", "Full body MRI scan", new BigDecimal("150.00"));
        MedicalServiceRespDto dto2 = new MedicalServiceRespDto(2, "Blood Test", "Complete blood panel test", new BigDecimal("50.00"));

        when(medicalServiceMapper.entityToDto(medicalService1)).thenReturn(dto1);
        when(medicalServiceMapper.entityToDto(medicalService2)).thenReturn(dto2);

        List<MedicalServiceRespDto> actualCall = medicalServiceService.getAllServices();

        assertThat(actualCall).hasSize(2);
        assertThat(actualCall.getFirst().serviceName()).isEqualToIgnoringCase("Diagnostic Scan");
        assertThat(actualCall.get(1).serviceName()).isEqualToIgnoringCase("Blood Test");
    }

    @Test
    public void getAllServices_ReturnEmptyList() {
        when(medicalServiceRepository.findAll()).thenReturn(List.of());

        List<MedicalServiceRespDto> actualCall = medicalServiceService.getAllServices();

        assertThat(actualCall).hasSize(0);
        assertThat(actualCall).isEmpty();
    }

    @Test
    public void getServiceById_serviceExists() {
        when(medicalServiceRepository.findById(1)).thenReturn(Optional.of(medicalService1));
        when(medicalServiceRepository.findById(2)).thenReturn(Optional.of(medicalService2));

        MedicalServiceRespDto dto1 = new MedicalServiceRespDto(1, "Diagnostic Scan", "Full body MRI scan", new BigDecimal("150.00"));
        MedicalServiceRespDto dto2 = new MedicalServiceRespDto(2, "Blood Test", "Complete blood panel test", new BigDecimal("50.00"));

        when(medicalServiceMapper.entityToDto(medicalService1)).thenReturn(dto1);
        when(medicalServiceMapper.entityToDto(medicalService2)).thenReturn(dto2);

        assertThat(medicalServiceService.getServiceById(1).id()).isEqualTo(1);
        assertThat(medicalServiceService.getServiceById(2).id()).isEqualTo(2);

        assertThat(medicalServiceService.getServiceById(1).serviceName()).isEqualToIgnoringCase("Diagnostic Scan");
        assertThat(medicalServiceService.getServiceById(2).serviceName()).isEqualToIgnoringCase("Blood Test");
    }

    @Test
    public void getServiceById_doesNotExists() {
        when(medicalServiceRepository.findById(1)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> medicalServiceService.getServiceById(1))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Medical Service Not Found");
    }

    @Test
    public void addService_mustSaveAndReturnNothing() {
        Healthcare healthcare = new Healthcare();
        healthcare.setId(1);
        healthcare.setHealthcareName("General Clinic");

        when(healthcareService.getHealthcareByName("General Clinic")).thenReturn(healthcare);
        when(medicalServiceRepository.save(any(MedicalService.class))).thenReturn(medicalService3);

        MedicalServiceReqDto dto = new MedicalServiceReqDto("X-Ray", "Chest scan", new BigDecimal("80.00"));

        medicalServiceService.addService("General Clinic", dto);

        verify(healthcareService, times(1)).getHealthcareByName("General Clinic");
        verify(medicalServiceRepository, times(1)).save(any(MedicalService.class));
    }

    @Test
    public void updateService_mustSaveAndReturnService() {
        when(medicalServiceRepository.findById(3)).thenReturn(Optional.of(medicalService3));
        when(medicalServiceRepository.save(any(MedicalService.class))).thenReturn(medicalService3);

        MedicalServiceRespDto dto3 = new MedicalServiceRespDto(3,"X-Ray","Chest scan",
                new BigDecimal("80.00"));
        when(medicalServiceMapper.entityToDto(any(MedicalService.class))).thenReturn(dto3);

        MedicalServiceReqDto reqDto = new MedicalServiceReqDto("X-Ray", "Chest scan", new BigDecimal("80.00"));

        MedicalServiceRespDto actualCall = medicalServiceService.updateService(3, reqDto);

        assertThat(actualCall.serviceName()).isEqualTo(reqDto.serviceName());
        assertThat(actualCall.basePrice()).isEqualTo(reqDto.basePrice());

        verify(medicalServiceRepository, times(1)).findById(3);
        verify(medicalServiceRepository, times(1)).save(any(MedicalService.class));
    }
}
