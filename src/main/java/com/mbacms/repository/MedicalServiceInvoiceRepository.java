package com.mbacms.repository;

import com.mbacms.model.MedicalServiceInvoice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MedicalServiceInvoiceRepository extends JpaRepository<MedicalServiceInvoice,Integer> {

    List<MedicalServiceInvoice> findByInvoiceId(int invoiceId);
}
