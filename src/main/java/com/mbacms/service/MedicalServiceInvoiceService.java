package com.mbacms.service;

import com.mbacms.model.MedicalServiceInvoice;
import com.mbacms.repository.MedicalServiceInvoiceRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
@AllArgsConstructor
public class MedicalServiceInvoiceService {
    private final MedicalServiceInvoiceRepository medicalServiceInvoiceRepository;

    public void saveAll(List<MedicalServiceInvoice> invoiceItems) {
        medicalServiceInvoiceRepository.saveAll(invoiceItems);
    }

    public List<MedicalServiceInvoice> getByInvoiceId(int invoiceId) {

        return medicalServiceInvoiceRepository.findByInvoiceId(invoiceId);
    }
}
