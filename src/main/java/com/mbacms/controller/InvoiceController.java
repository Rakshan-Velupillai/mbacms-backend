package com.mbacms.controller;


import com.mbacms.DTO.InvoiceReqDto;
import com.mbacms.DTO.InvoiceRespDto;
import com.mbacms.service.InvoiceService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/invoice")
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:5173/")

public class InvoiceController {


    private final InvoiceService invoiceService;
    @PostMapping("/generate")
    public InvoiceRespDto generate(Principal principal, @Valid @RequestBody InvoiceReqDto dto) {
        return invoiceService.generateInvoice(principal.getName(), dto);
    }

    @GetMapping("/getInvoiceById/{invoiceId}")
    public InvoiceRespDto getInvoiceById(@PathVariable int invoiceId, Principal principal){
        return invoiceService.getInvoiceById(invoiceId, principal.getName());
    }

    @GetMapping("/healthcare-invoices/")
    public List<InvoiceRespDto> getAllInvoiceById(Principal principal,
                                                  @RequestParam(defaultValue = "0") int page,
                                                  @RequestParam(defaultValue = "5") int size,
                                                  @RequestParam(required = false) String search,
                                                  @RequestParam(required = false) String status,
                                                  @RequestParam(required = false) String sortBy,
                                                  @RequestParam(required = false) String sortDir){
        return invoiceService.getHealthcareInvoiceById(principal.getName(), page, size, search, status, sortBy, sortDir);
    }

    @GetMapping("/patient-invoices")
    public List<InvoiceRespDto> getPatientInvoices(Principal principal) {
        return invoiceService.getPatientInvoices(principal.getName());
    }

    @PostMapping("/pay/{invoiceId}")
    public void payInvoice(@PathVariable int invoiceId, Principal principal) {
        invoiceService.payInvoice(invoiceId, principal.getName());
    }
}
