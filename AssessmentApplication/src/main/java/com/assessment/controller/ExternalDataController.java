package com.assessment.controller;

import com.assessment.entity.Customer;
import com.assessment.repository.CustomerRepository;
import com.assessment.service.ExternalDataService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/external")
public class ExternalDataController {
    @Autowired
    private final ExternalDataService externalDataService;
    @Autowired
    private CustomerRepository customerRepository;

    public ExternalDataController(ExternalDataService externalDataService) {
        this.externalDataService = externalDataService;
    }

    /**
     * Fetch all users from 3rd party API and save to database
     */
    @PostMapping("/fetch-all")
    public ResponseEntity<Map<String, Object>> fetchAllUsers() {
        Map<String, Object> response = new HashMap<>();

        try {
            List<Customer> savedCustomers = externalDataService.fetchAndSaveExternalUsers();

            response.put("success", true);
            response.put("message", "Successfully fetched and saved " + savedCustomers.size() + " customers");
            response.put("customers", savedCustomers);
            response.put("count", savedCustomers.size());
            response.put("timestamp", LocalDateTime.now());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Failed to fetch and save users");
            response.put("error", e.getMessage());
            response.put("timestamp", LocalDateTime.now());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Fetch specific user by ID and save to database
     */
    @PostMapping("/fetch/{userId}")
    public ResponseEntity<Map<String, Object>> fetchUserById(@PathVariable Long userId) {
        Map<String, Object> response = new HashMap<>();

        try {
            Customer savedCustomer = externalDataService.fetchAndSaveUserById(userId);

            response.put("success", true);
            response.put("message", "Successfully fetched and saved customer");
            response.put("customer", savedCustomer);
            response.put("timestamp", LocalDateTime.now());

            return ResponseEntity.ok(response);

        } catch (RuntimeException e) {
            // Handle already exists case
            if (e.getMessage().contains("already exists")) {
                response.put("success", false);
                response.put("message", e.getMessage());
                response.put("timestamp", LocalDateTime.now());

                return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
            }

            // Handle other errors
            response.put("success", false);
            response.put("message", "Failed to fetch and save user");
            response.put("error", e.getMessage());
            response.put("timestamp", LocalDateTime.now());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    /**
     * Fetch from custom URL and save to database
     */
    @PostMapping("/fetch-custom")
    public ResponseEntity<Map<String, Object>> fetchFromCustomUrl(@RequestBody Map<String, String> request) {
        Map<String, Object> response = new HashMap<>();
        String customUrl = request.get("url");
        if (customUrl == null || customUrl.isEmpty()) {
            response.put("success", false);
            response.put("message", "URL is required");
            return ResponseEntity.badRequest().body(response);
        }

        try {
            // This is a simplified version - you might need to adjust parsing based on API response
            ResponseEntity<String> apiResponse = new RestTemplate().getForEntity(customUrl, String.class);

            ObjectMapper mapper = new ObjectMapper();
            List<Map<String, Object>> externalData = mapper.readValue(
                    apiResponse.getBody(),
                    mapper.getTypeFactory().constructCollectionType(List.class, Map.class)
            );

            List<Customer> savedCustomers = new ArrayList<>();

            for (Map<String, Object> data : externalData) {
                Customer customer = new Customer();

                // Extract data from response - adjust keys based on your API
                String id = String.valueOf(data.get("id"));
                String name = (String) data.get("name");
                String email = (String) data.get("email");

                if (name != null && email != null) {
                    customer.setCustno("EXT-" + id);
                    customer.setName(name);
                    customer.setEmail(email);
                    customer.setCreatedDate(LocalDateTime.now());

                    if (!customerRepository.existsByEmail(email)) {
                        savedCustomers.add(customerRepository.save(customer));
                    }
                }
            }

            response.put("success", true);
            response.put("message", "Saved " + savedCustomers.size() + " customers from custom URL");
            response.put("count", savedCustomers.size());
            response.put("timestamp", LocalDateTime.now());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Failed to fetch from custom URL");
            response.put("error", e.getMessage());
            response.put("timestamp", LocalDateTime.now());

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

}