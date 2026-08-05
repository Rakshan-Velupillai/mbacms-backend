package com.mbacms.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Healthcare {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String healthcareName;
    private String specialization;

    @Column(unique = true)
    private String licenseNumber;

    @Column(length = 1000)
    private String address;

    @OneToOne
    private User user;

}