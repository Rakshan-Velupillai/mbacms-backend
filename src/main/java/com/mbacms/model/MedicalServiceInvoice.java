package com.mbacms.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "medical_service_invoice")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MedicalServiceInvoice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(precision = 12,scale = 2)
    private BigDecimal actualAmount;


    @ManyToOne
    private MedicalService medicalService;

    @ManyToOne
    private Invoice invoice;

}
