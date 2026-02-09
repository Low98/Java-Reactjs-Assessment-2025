package com.assessment.dto;

public class CustnoRequestDTO {
    private String custno;

    // Constructors
    public CustnoRequestDTO() {}

    public CustnoRequestDTO(String custno) {
        this.custno = custno;
    }

    // Getter and Setter
    public String getCustno() {
        return custno;
    }

    public void setCustno(String custno) {
        this.custno = custno;
    }
}