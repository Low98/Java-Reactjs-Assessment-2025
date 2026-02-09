package com.assessment.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;

@Entity
@Data
public class Card {

    @Id
    private String cardno;

    private String custno;

    private BigDecimal cdamt;

}
