package com.assessment.entity;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Data
public class Customer {

    @Id
    private String custno;

    private String name;

    private String email;

    private LocalDateTime createdDate;


    // Constructors
    public Customer() {}

    public Customer(String custno, String name, String email, LocalDateTime createdDate) {
        this.custno = custno;
        this.name = name;
        this.email = email;
        this.createdDate = createdDate;
    }

    // Getters and Setters
    public String getCustno() { return custno; }
    public void setCustno(String custno) { this.custno = custno; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public LocalDateTime getCreatedDate() { return createdDate; }
    public void setCreatedDate(LocalDateTime createdDate) { this.createdDate = createdDate; }
}
