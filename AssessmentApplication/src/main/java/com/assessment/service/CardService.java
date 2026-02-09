package com.assessment.service;

import com.assessment.entity.Card;
import com.assessment.entity.CustCard;
import com.assessment.repository.CustomerRepository;
import com.assessment.repository.CardRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CardService {

    @Autowired
    private CardRepository cardRepository;
    @Autowired
    private CustomerRepository customerRepository;

    @Transactional
    public Card createCard(Card card){
        if(customerRepository.findByCustno(card.getCustno()).isEmpty()){
            throw new RuntimeException("Customer not found");
        }

        // For scenario where Cardno can be dup
        // One Customer can have multiple card
//        if(cardRepository.existsByCustnoAndCardno(card.getCustno(), card.getCardno())){
//            throw new RuntimeException("Card already exists for this customer");
//        }

        if(cardRepository.findByCardno(card.getCardno()).isPresent()){
            throw new RuntimeException("Card no already exists");
        }

        return cardRepository.save(card);
    }


    @Transactional(readOnly = true)
    public List<Card> getCardsByCustNo(String custno){
        return cardRepository.findByCustno(custno);
    }

    public List<CustCard> getCustCardsByCustNo(String custno) {
        List<Object[]> results = cardRepository.findCardsWithCustomerNo(custno);

        return results.stream().map(row -> {
            CustCard custcard = new CustCard();
            custcard.setCustno((String) row[0]);
            custcard.setCardNo((String) row[1]);
            custcard.setCdAmt((BigDecimal) row[2]);
            custcard.setCustomerName((String) row[3]); // customer_name from join

            return custcard;
        }).collect(Collectors.toList());
    }
}
