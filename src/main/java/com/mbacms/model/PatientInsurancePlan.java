package com.mbacms.model;

import com.mbacms.enums.ActiveStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PatientInsurancePlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(unique = true)
    private String policyNumber;

    @Enumerated(EnumType.STRING)
    private ActiveStatus activeStatus;

    private LocalDate startDate;

    private LocalDate endDate;

    private int priorityOrder;

    @ManyToOne
    private Patient patient;

    @ManyToOne
    private InsurancePlan insurancePlan;
}