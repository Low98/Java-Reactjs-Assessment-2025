package com.assessment.service;

import com.assessment.entity.Customer;
import com.assessment.dto.ExternalUserDTO;
import com.assessment.repository.CustomerRepository;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ExternalDataService {

    private final CustomerRepository customerRepository;
    private final RestTemplate restTemplate;

    public ExternalDataService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
        this.restTemplate = new RestTemplate();
    }

    /**
     * Simple method to fetch users from 3rd party API and save to database
     */
    @Transactional
    public List<Customer> fetchAndSaveExternalUsers() {
        List<Customer> savedCustomers = new ArrayList<>();

        try {
            // Step 1: Call 3rd party API
            String apiUrl = "https://jsonplaceholder.typicode.com/users";
            ResponseEntity<String> response = restTemplate.getForEntity(apiUrl, String.class);

            // Step 2: Parse the JSON response
            ObjectMapper mapper = new ObjectMapper();
            List<ExternalUserDTO> externalUsers = mapper.readValue(
                    response.getBody(),
                    mapper.getTypeFactory().constructCollectionType(List.class, ExternalUserDTO.class)
            );

            // Step 3: Convert to Customer entities and save
            for (ExternalUserDTO externalUser : externalUsers) {
                // Create Customer entity from ExternalUser
                Customer customer = convertToCustomer(externalUser);

                // Check if customer already exists (by email or custno)
                if (!customerRepository.existsByEmail(customer.getEmail())) {
                    Customer savedCustomer = customerRepository.save(customer);
                    savedCustomers.add(savedCustomer);
                }
            }

            return savedCustomers;

        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch and save external users: " + e.getMessage(), e);
        }
    }

    /**
     * Convert ExternalUser to Customer entity
     */
    private Customer convertToCustomer(ExternalUserDTO externalUser) {
        Customer customer = new Customer();

        // Generate custno from ID (or use email)
        customer.setCustno("EXT" + externalUser.getId());  // EXT1, EXT2, etc.
        customer.setName(externalUser.getName());
        customer.setEmail(externalUser.getEmail());
        customer.setCreatedDate(LocalDateTime.now());

        return customer;
    }

    /**
     * Alternative: Save specific user by ID
     */
    @Transactional
    public Customer fetchAndSaveUserById(Long userId) {
        try {
            String apiUrl = "https://jsonplaceholder.typicode.com/users/" + userId;
            ResponseEntity<String> response = restTemplate.getForEntity(apiUrl, String.class);

            ObjectMapper mapper = new ObjectMapper();
            ExternalUserDTO externalUser = mapper.readValue(response.getBody(), ExternalUserDTO.class);

            Customer customer = convertToCustomer(externalUser);

            // Check if exists before saving
            if (!customerRepository.existsByCustno(customer.getCustno())) {
                return customerRepository.save(customer);
            } else {
                throw new RuntimeException("Customer already exists with custno: " + customer.getCustno());
            }

        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch and save user by ID: " + e.getMessage(), e);
        }
    }
}

