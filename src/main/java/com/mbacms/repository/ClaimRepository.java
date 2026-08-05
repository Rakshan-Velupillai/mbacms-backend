package com.mbacms.repository;

import com.mbacms.enums.ClaimStatus;
import com.mbacms.model.Claim;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ClaimRepository extends JpaRepository<Claim,Integer> {

    @Query("""
select c from Claim c
 WHERE c.patientInsurancePlan.patient.user.username = :username
  order by c.submissionDate DESC
""")
    List<Claim> getClaimsByPatientUsername(String username);

    @Query("""
select c from Claim c
where c.patientInsurancePlan.insurancePlan.insuranceCompany.user.username = :username
order by c.submissionDate DESC
""")
    List<Claim> getClaimsByCompanyUsername(String username);

    boolean existsByInvoiceInvoiceNumber(String invoiceNumber);

    long countByClaimStatus(ClaimStatus claimStatus);
}
