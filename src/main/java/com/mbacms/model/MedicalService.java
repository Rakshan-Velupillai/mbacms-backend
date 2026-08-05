package com.mbacms.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "medical_service")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MedicalService {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(unique = true)
    private String serviceName; // e.g: "Diagnostic Scan"
    @Column(length = 1000)
    private String description;
    @Column(precision = 10,scale = 2)
    private BigDecimal basePrice;

}