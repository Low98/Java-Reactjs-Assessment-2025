package com.assessment.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.Data;

import java.math.BigDecimal;

@Entity
@Data
public class CustCard {

    @Id
    private String cardno;

    private String custno;

    private BigDecimal cdamt;

    @Transient
    private String customerName;

    // Constructors, getters, and setters
    public CustCard() {}

    // Getters and setters
    public void setCardNo(String cardno) { this.cardno = cardno; }

    public String getCustno() { return custno; }
    public void setCustno(String custno) { this.custno = custno; }

    public BigDecimal getCdAmt() { return cdamt; }
    public void setCdAmt(BigDecimal cdamt) { this.cdamt = cdamt; }
    public String getCustomerName() { return customerName; }
    public void setCustomerName(String customerName) { this.customerName = customerName; }
}
