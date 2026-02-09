package com.assessment.service;

import com.assessment.entity.Customer;
import com.assessment.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Transactional
    public Customer createCustomer(Customer customer) {
        if(customerRepository.findByCustno(customer.getCustno()).isPresent()){
            throw new RuntimeException("Customer custno already exists");
        }

        customer.setCreatedDate(LocalDateTime.now());
        return customerRepository.save(customer);
    }

//    @Transactional
//    public Customer updateCustomer(Long id, Customer customer) {
//        Customer db = customerRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("Customer not found"));
//
//        db.setName(customer.getName());
//        db.setEmail(customer.getEmail());
//
//        return customerRepository.save(db);
//    }

    public Customer updateCustomer(String custno, String name, String email) {
        Customer customer = customerRepository.findById(custno)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + custno));

        if (name != null) {
            customer.setName(name);
        }
        if (email != null) {
            customer.setEmail(email);
        }

        return customerRepository.save(customer);
    }

    @Transactional(readOnly = true)
    public Page<Customer> getCustomers(int page) {
        return customerRepository.findAll(PageRequest.of(page, 10));
    }
}
