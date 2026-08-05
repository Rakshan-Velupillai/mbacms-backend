package com.mbacms.model;

import com.mbacms.enums.ActiveStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "insurance_plan")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class InsurancePlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String planName;
    private String planType;

    @Column(name = "plan_description")
    private String planDesc;

    @Column(precision = 12, scale = 2)
    private BigDecimal coverageAmount;

    @Column(precision = 10, scale = 2)
    private BigDecimal premiumAmount;

    private int durationMonths;

    @Enumerated(EnumType.STRING)
    private ActiveStatus activeStatus;

    @ManyToOne
    private InsuranceCompany insuranceCompany;

}

