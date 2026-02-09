package com.assessment.repository;

import com.assessment.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, String> {

    boolean existsByCustno(String custno);
    boolean existsByEmail(String email);
    Optional<Customer> findByCustno(String custno);
    Optional<Customer> findByEmail(String email);

    @Query("SELECT c FROM Customer c WHERE c.custno LIKE 'EXT%'")
    List<Customer> findExternalCustomers();
}
