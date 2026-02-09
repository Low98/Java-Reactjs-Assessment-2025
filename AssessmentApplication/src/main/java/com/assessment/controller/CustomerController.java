package com.assessment.controller;

import com.assessment.dto.CustomerUpdateDTO;
import com.assessment.entity.Customer;
import com.assessment.service.CustomerService;
import com.assessment.repository.CustomerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    @Autowired
    private CustomerService customerService;
    @Autowired
    private CustomerRepository customerRepository;

    private static final Logger logger = LoggerFactory.getLogger(CustomerController.class);

    // Create
    @PostMapping("/create")
    public Customer create(@RequestBody Customer customer) {
        return customerService.createCustomer(customer);
    }

    // Update
//    @PutMapping("/{id}")
//    public Customer update(@PathVariable Long id, @RequestBody Customer customer) {
//        return customerService.updateCustomer(id, customer);
//    }

    @PutMapping("/update")
    public ResponseEntity<Customer> update(@RequestBody CustomerUpdateDTO customerDTO) {
        Customer updatedCustomer = customerService.updateCustomer(
                customerDTO.getCustno(),
                customerDTO.getName(),
                customerDTO.getEmail()
        );
        return ResponseEntity.ok(updatedCustomer);
    }

    // Pagination GET
    @GetMapping("/allcustdetail")
    public Page<Customer> get(@RequestParam(defaultValue = "0") int page) {
        return customerService.getCustomers(page);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<Map<String, Object>> deleteCustomerByBody(@RequestBody Map<String, String> request) {
        // Log REQUEST
        logger.info("REQUEST - DELETE /api/customers/delete - Body: {}", request);
        String custno = request.get("custno");

        if (custno == null || custno.trim().isEmpty()) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "custno is required in request body");
            response.put("timestamp", LocalDateTime.now());
            // Log RESPONSE for error
            logger.info("RESPONSE - DELETE /api/customers/delete - Error: {}", response);
            return ResponseEntity.badRequest().body(response);
        }

        return deleteCustomer(custno);
    }

    private ResponseEntity<Map<String, Object>> deleteCustomer(String custno) {
        Map<String, Object> response = new HashMap<>();

        long startTime = System.currentTimeMillis();

        try {
            logger.info("Attempting to delete customer with custno: {}", custno);
            // Check if customer exists
            Optional<Customer> customerOpt = customerRepository.findByCustno(custno);

            if (customerOpt.isEmpty()) {
                response.put("success", false);
                response.put("message", "Customer not found with custno: " + custno);
                response.put("timestamp", LocalDateTime.now());
                long executionTime = System.currentTimeMillis() - startTime;
                logger.info("RESPONSE - Customer not found - custno: {} - Execution time: {}ms",
                        custno, executionTime);

                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
            }

            Customer customer = customerOpt.get();
            logger.info("Found customer to delete: {} - {} - {}",
                    customer.getCustno(), customer.getName(), customer.getEmail());

            // Delete the customer
            customerRepository.delete(customer);

            response.put("success", true);
            response.put("message", "Customer deleted successfully");
            response.put("deletedCustomer", Map.of(
                    "custno", customer.getCustno(),
                    "name", customer.getName(),
                    "email", customer.getEmail()
            ));
            response.put("timestamp", LocalDateTime.now());

            long executionTime = System.currentTimeMillis() - startTime;
            logger.info("RESPONSE - DELETE successful - custno: {} - Execution time: {}ms",
                    custno, executionTime);
            logger.info("Deleted customer details: custno={}, name={}, email={}",
                    customer.getCustno(), customer.getName(), customer.getEmail());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Failed to delete customer");
            response.put("error", e.getMessage());
            response.put("timestamp", LocalDateTime.now());

            long executionTime = System.currentTimeMillis() - startTime;
            logger.error("ERROR - DELETE failed - custno: {} - Error: {} - Execution time: {}ms",
                    custno, e.getMessage(), executionTime, e);

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);        }
    }
}