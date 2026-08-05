package com.mbacms.model;


import com.mbacms.enums.ClaimStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Claim {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(unique = true)
    private  String claimNumber;
    @Column(precision = 12, scale = 2)
     private BigDecimal claimAmount;

     @Column(updatable = false)
     private LocalDate submissionDate;

     private LocalDate approvedDate;

     @Enumerated(EnumType.STRING)
      private ClaimStatus claimStatus;

      private  String rejectionReason;

      private String documentUrl;

      @OneToOne
      private Invoice invoice;

      @ManyToOne
      private PatientInsurancePlan patientInsurancePlan;


}
