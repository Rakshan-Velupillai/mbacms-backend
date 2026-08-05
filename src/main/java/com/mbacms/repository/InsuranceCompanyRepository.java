package com.mbacms.repository;

import com.mbacms.model.InsuranceCompany;
import com.mbacms.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InsuranceCompanyRepository extends JpaRepository<InsuranceCompany,Integer> {

    InsuranceCompany findByUser(User user);

    InsuranceCompany findByUserUsername(String name);
}
