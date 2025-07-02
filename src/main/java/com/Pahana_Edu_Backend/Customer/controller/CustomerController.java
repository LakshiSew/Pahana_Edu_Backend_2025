package com.Pahana_Edu_Backend.Customer.controller;

import com.Pahana_Edu_Backend.Customer.entity.Customer;
import com.Pahana_Edu_Backend.Customer.service.CustomerService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    private final PasswordEncoder passwordEncoder;

  
    public CustomerController(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    // ✅ Create customer
    @PostMapping("/auth/createcustomer")
    public ResponseEntity<?> createCustomer(@RequestBody Customer customer) {
        if (customerService.existsByUserName(customer.getUserName())) {
            return ResponseEntity.status(409).body("Username already exists");
        }

        customer.setPassword(passwordEncoder.encode(customer.getPassword()));
        Customer savedCustomer = customerService.addCustomer(customer);
        return ResponseEntity.ok(savedCustomer);
    }

    // ✅ Get all customers
    @GetMapping("/getallcustomers")
    public List<Customer> getAllCustomers() {
        return customerService.getAllCustomers();
    }

    // ✅ Get customer by ID
    @GetMapping("/getcustomerbyid/{customerId}")
    public ResponseEntity<?> getCustomerById(@PathVariable String customerId) {
        Customer customer = customerService.getCustomerById(customerId).orElse(null);
        if (customer != null) {
            return ResponseEntity.ok(customer);
        } else {
            return ResponseEntity.status(404).body("Customer not found");
        }
    }

    // ✅ Update customer
    @PutMapping("/updatecustomer/{customerId}")
    public ResponseEntity<?> updateCustomer(@PathVariable String customerId, @RequestBody Customer customer) {
        Customer updated = customerService.updateCustomer(customerId, customer);
        if (updated != null) {
            return ResponseEntity.ok(updated);
        } else {
            return ResponseEntity.status(404).body("Customer not found");
        }
    }

    // ✅ Delete customer
     @DeleteMapping("/deletecustomer/{customerId}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable String customerId) {
        customerService.deleteCustomer(customerId);
        return ResponseEntity.noContent().build();
    }

        @GetMapping("/auth/checkUsername")
public ResponseEntity<Map<String, Boolean>> checkUsernameAvailability(@RequestParam String userName) {
    boolean exists = customerService.existsByUserName(userName);
    return ResponseEntity.ok().body(Map.of("exists", exists));
}
}
