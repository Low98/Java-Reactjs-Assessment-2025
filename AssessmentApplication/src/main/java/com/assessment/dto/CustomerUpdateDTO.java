package com.assessment.dto;

public class CustomerUpdateDTO {
    private String custno;
    private String name;
    private String email;

    // Constructors, getters, and setters
    public CustomerUpdateDTO() {}

    public CustomerUpdateDTO(String custno, String name, String email) {
        this.custno = custno;
        this.name = name;
        this.email = email;
    }

    // Getters and setters
    public String getCustno() { return custno; }
    public void setCustno(String custno) { this.custno = custno; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}
