package com.assessment.controller;

import com.assessment.dto.CustnoRequestDTO;
import com.assessment.dto.CustomerUpdateDTO;
import com.assessment.entity.Card;
import com.assessment.entity.CustCard;
import com.assessment.entity.Customer;
import com.assessment.service.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cards")
public class CardController {

    @Autowired
    private CardService cardService;

    @PostMapping("/create")
    public Card create(@RequestBody Card card){
        return cardService.createCard(card);
    }

    @GetMapping("/cardbycustno")
    public List<Card> getByCustno(@RequestBody CustnoRequestDTO request){
        return cardService.getCardsByCustNo(request.getCustno());
    }

    @GetMapping("/cust&cardbycustno")
    public List<CustCard> getCustCards(@RequestBody CustnoRequestDTO request){
        return cardService.getCustCardsByCustNo(request.getCustno());
    }

}
