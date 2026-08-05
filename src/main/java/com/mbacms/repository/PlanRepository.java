package com.mbacms.repository;

import com.mbacms.model.InsurancePlan;
import com.mbacms.model.Patient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlanRepository extends JpaRepository<InsurancePlan,Integer> {


}
