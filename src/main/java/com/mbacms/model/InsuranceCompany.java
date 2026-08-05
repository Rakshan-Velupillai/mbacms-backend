package com.mbacms.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "insurance_company")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class InsuranceCompany {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;


    private String companyName;

    @Column(unique = true)
    private String regNo;

    @Column(length = 1000, nullable = false)
    private String address;

    @OneToOne
    private User user;

}
