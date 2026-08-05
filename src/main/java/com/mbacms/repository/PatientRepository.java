package com.mbacms.repository;

import com.mbacms.model.Patient;
import com.mbacms.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PatientRepository extends JpaRepository<Patient,Integer> {


   Optional<Patient> findByUser(User user);
   Optional<Patient> findByUserUsername(String name);
   Optional<Patient> findByPatientCode(String patientCode);
}
