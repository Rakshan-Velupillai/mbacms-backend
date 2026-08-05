package com.mbacms.repository;

import com.mbacms.model.Healthcare;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface HealthcareRepository extends JpaRepository<Healthcare,Integer> {


    Optional<Healthcare> findByUserUsername(String name);
}
