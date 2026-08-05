package com.mbacms.repository;

import com.mbacms.enums.InvoiceStatus;
import com.mbacms.model.Invoice;
import com.mbacms.model.Patient;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice,Integer> {

    @Query("""
       select i
       from Invoice i
       where i.healthcare.user.username = :name
       """)
    List<Invoice> getInvoicesByHealthcare(@Param("name") String name, Pageable pageable);

    @Query("""
            select i
            from Invoice i
            where i.healthcare.user.username=?1
            and (?2 is null or ?2='' or
                 lower(i.invoiceNumber) like lower(concat('%',?2,'%')) or
                 lower(i.patient.user.fullName) like lower(concat('%',?2,'%')) or
                 lower(i.patient.user.username) like lower(concat('%',?2,'%'))
                )
            and (?3 is null or i.invoiceStatus=?3)
       """)
    List<Invoice> getInvoicesByHealthcareWithSearchAndFilter(String name,String search,InvoiceStatus status,Pageable pageable);

    List<Invoice> findByPatient(Patient patient);
    Optional<Invoice> findByInvoiceNumber(String invoiceNumber);
}
