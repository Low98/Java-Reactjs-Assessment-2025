package com.assessment.repository;

import com.assessment.entity.Card;
import com.assessment.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CardRepository extends JpaRepository<Card, Long> {

    List<Card> findByCustno(String custno);
    Optional<Card> findByCardno(String cardno);

    boolean existsByCustnoAndCardno(String custno, String cardno);

    // Custom query to fetch cards with customer name
    @Query(value = "SELECT customer.custno, card.cardno, card.cdamt, customer.name as customer_name " +
            "FROM customer " +
            "LEFT JOIN card ON card.custno = customer.custno " +
            "WHERE customer.custno = :custno", nativeQuery = true)
    List<Object[]> findCardsWithCustomerNo(@Param("custno") String custno);
}
