package com.mbacms.service;

import com.mbacms.DTO.InvoiceReqDto;
import com.mbacms.DTO.InvoiceRespDto;
import com.mbacms.DTO.MedicalServiceInvoiceItemDto;
import com.mbacms.DTO.MedicalServiceInvoiceRespDto;
import com.mbacms.enums.InvoiceStatus;
import com.mbacms.exception.OwnershipInvalidException;
import com.mbacms.exception.ResourceNotFoundException;
import com.mbacms.mapper.InvoiceMapper;
import com.mbacms.mapper.MedicalServiceInvoiceMapper;
import com.mbacms.model.*;
import com.mbacms.repository.InvoiceRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class InvoiceService {

    private final HealthcareService healthcareService;
    private final PatientService patientService;
    private final InvoiceRepository invoiceRepository;
    private final MedicalServiceService medicalServiceService;
    private final MedicalServiceInvoiceService medicalServiceInvoiceService;
    private final MedicalServiceInvoiceMapper medicalServiceInvoiceMapper;
    private final InvoiceMapper invoiceMapper;

    public InvoiceRespDto generateInvoice(String name,InvoiceReqDto dto) {
        if (dto.dueDate() != null && dto.dueDate().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Due date cannot be in the past.");
        }
        Healthcare healthcare=healthcareService.getHealthcareByName(name);

        Patient patient=patientService.getPatientById(dto.patientId());

        BigDecimal subtotal=BigDecimal.ZERO;
        for(MedicalServiceInvoiceItemDto item:dto.services()) {
            subtotal=subtotal.add(item.actualAmount());
        }
        BigDecimal taxAmount=subtotal.multiply(dto.taxRate())
                        .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);

        BigDecimal totalAmount=subtotal.add(taxAmount);

        Invoice invoice = new Invoice();

        invoice.setInvoiceNumber("INV-"+UUID.randomUUID().toString().substring(0,8).toUpperCase());

        invoice.setInvoiceDate(LocalDate.now());
        invoice.setDueDate(dto.dueDate());
        invoice.setSubtotal(subtotal);
        invoice.setTaxRate(dto.taxRate());
        invoice.setTaxAmount(taxAmount);
        invoice.setTotalDueAmount(totalAmount);
        invoice.setInvoiceStatus(InvoiceStatus.UNPAID);
        invoice.setSymptomsDesc(dto.symptomsDesc());
        invoice.setTreatmentDesc(dto.treatmentDesc());
        invoice.setPatient(patient);
        invoice.setHealthcare(healthcare);

        invoice=invoiceRepository.save(invoice);

        List<MedicalServiceInvoice> invoiceItems = new ArrayList<>();

        for(MedicalServiceInvoiceItemDto item:dto.services()) {
            MedicalService service = medicalServiceService.getMedicalServiceById(item.medicalServiceId());

            MedicalServiceInvoice msi=new MedicalServiceInvoice();

            msi.setInvoice(invoice);
            msi.setMedicalService(service);
            msi.setActualAmount(item.actualAmount());

            invoiceItems.add(msi);
        }
        medicalServiceInvoiceService.saveAll(invoiceItems);

        List<MedicalServiceInvoiceRespDto> services = invoiceItems
                .stream()
                .map(medicalServiceInvoiceMapper::entityToDto)
                .toList();

        return invoiceMapper.entityToDto(invoice,services);
    }

    public InvoiceRespDto getInvoiceById(int invoiceId, String name) {

        Invoice invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(()->new ResourceNotFoundException("Invoice Not Found"));

        Patient patient=patientService.getPatientByUsername(name);

        if(invoice.getPatient().getId()!=patient.getId()){
            throw new OwnershipInvalidException("You cannot view this invoice");
        }

        List<MedicalServiceInvoiceRespDto> services = medicalServiceInvoiceService
                        .getByInvoiceId(invoiceId)
                        .stream()
                        .map(medicalServiceInvoiceMapper::entityToDto)
                        .toList();

        return invoiceMapper.entityToDto(invoice,services);

    }

    public List<InvoiceRespDto> getHealthcareInvoiceById(String name,int page,int size,String search,String statusStr,String sortBy,String sortDir) {

        String resolvedSortBy = "invoiceDate";

        if (sortBy != null && !sortBy.trim().isEmpty()) {

            if (sortBy.equals("patientName")) {
                resolvedSortBy = "patient.user.fullName";
            }
            else if (sortBy.equals("invoiceDate")) {
                resolvedSortBy = "invoiceDate";
            }
            else if (sortBy.equals("dueDate")) {
                resolvedSortBy = "dueDate";
            }
            else if (sortBy.equals("totalDueAmount")) {
                resolvedSortBy = "totalDueAmount";
            }
        }

        Sort.Direction direction = Sort.Direction.DESC;

        if (sortDir != null && sortDir.equalsIgnoreCase("asc")) {
            direction = Sort.Direction.ASC;
        }

        Pageable pageable = PageRequest.of(page,size,Sort.by(direction, resolvedSortBy));

        InvoiceStatus status = null;

        if (statusStr != null && !statusStr.trim().isEmpty() && !statusStr.equalsIgnoreCase("ALL")) {
            try {
                status = InvoiceStatus.valueOf(statusStr.trim().toUpperCase());
            }
            catch (IllegalArgumentException e) {
                status = null;
            }
        }
        String searchValue = "";

        if (search != null) {
            searchValue = search.trim();
        }
        List<Invoice> invoices=invoiceRepository.getInvoicesByHealthcareWithSearchAndFilter(name,searchValue,status,pageable);

        List<InvoiceRespDto> invoiceDtos = new ArrayList<>();

        for (Invoice invoice : invoices) {

            List<MedicalServiceInvoice> medicalServiceInvoices=medicalServiceInvoiceService.getByInvoiceId(invoice.getId());

            List<MedicalServiceInvoiceRespDto> services=medicalServiceInvoices.stream()
                            .map(medicalServiceInvoiceMapper::entityToDto)
                            .toList();

            InvoiceRespDto invoiceRespDto=invoiceMapper.entityToDto(invoice, services);

            invoiceDtos.add(invoiceRespDto);
        }

        return invoiceDtos;
    }


    public Invoice findByInvoiceId(int id) {
        return invoiceRepository.findById(id)
                .orElseThrow(()->new ResourceNotFoundException("Invoice not found"));
    }

    public List<InvoiceRespDto> getPatientInvoices(String username) {
        Patient patient = patientService.getPatientByUsername(username);
        List<Invoice> invoices = invoiceRepository.findByPatient(patient);
        List<InvoiceRespDto> invoiceDtos = new ArrayList<>();

        for (Invoice invoice : invoices) {
            List<MedicalServiceInvoice> medicalServiceInvoices = medicalServiceInvoiceService
                    .getByInvoiceId(invoice.getId());

            List<MedicalServiceInvoiceRespDto> services = medicalServiceInvoices
                    .stream()
                    .map(medicalServiceInvoiceMapper::entityToDto)
                    .toList();

            invoiceDtos.add(invoiceMapper.entityToDto(invoice, services));
        }
        return invoiceDtos;
    }

    public void payInvoice(int invoiceId, String username) {
        Invoice invoice = invoiceRepository.findById(invoiceId)
                .orElseThrow(() -> new ResourceNotFoundException("Invoice not found"));

        Patient patient = patientService.getPatientByUsername(username);

        if (invoice.getPatient().getId() != patient.getId()) {
            throw new OwnershipInvalidException("You cannot pay this invoice");
        }
        invoice.setInvoiceStatus(InvoiceStatus.PAID);

        invoiceRepository.save(invoice);
    }
}
