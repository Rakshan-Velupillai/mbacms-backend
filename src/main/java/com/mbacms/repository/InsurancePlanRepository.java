package com.mbacms.repository;

import com.mbacms.model.InsuranceCompany;
import com.mbacms.model.InsurancePlan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InsurancePlanRepository extends JpaRepository<InsurancePlan,Integer> {

        List<InsurancePlan> findByInsuranceCompany(InsuranceCompany insuranceCompany);

}
