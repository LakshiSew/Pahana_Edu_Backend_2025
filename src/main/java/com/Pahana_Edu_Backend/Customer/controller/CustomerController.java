
package com.Pahana_Edu_Backend.Customer.controller;

import com.Pahana_Edu_Backend.Cloudinary.CloudinaryService;
import com.Pahana_Edu_Backend.Customer.entity.Customer;
import com.Pahana_Edu_Backend.Customer.service.CustomerService;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@CrossOrigin
@RequestMapping("/auth")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private CloudinaryService cloudinaryService;

    private final PasswordEncoder passwordEncoder;

    public CustomerController(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/createcustomer")
    public ResponseEntity<?> createCustomer(
            @RequestParam("customerName") String customerName,
            @RequestParam("customerEmail") String customerEmail,
            @RequestParam("address") String address,
            @RequestParam("userName") String userName,
            @RequestParam("password") String password,
            @RequestParam("customerPhone") String customerPhone,
            @RequestParam("profileImage") MultipartFile profileImage) throws IOException {
        if (customerService.existsByUserName(userName)) {
            return ResponseEntity.status(409).body("Username already exists");
        }
        if (profileImage == null || profileImage.isEmpty()) {
            return ResponseEntity.status(400).body("Profile image is required");
        }
        String imageUrl = cloudinaryService.uploadImage(profileImage);
        Customer customer = new Customer();
        customer.setCustomerName(customerName);
        customer.setCustomerEmail(customerEmail);
        customer.setAddress(address);
        customer.setUserName(userName);
        customer.setPassword(passwordEncoder.encode(password));
        customer.setCustomerPhone(customerPhone);
        customer.setStatus("PENDING");
        customer.setProfileImage(imageUrl);
        Customer savedCustomer = customerService.addCustomer(customer);
        return ResponseEntity.ok(savedCustomer);
    }

    @GetMapping("/getallcustomers")
    public ResponseEntity<List<Customer>> getAllCustomers() {
        return ResponseEntity.ok(customerService.getAllCustomers());
    }

    @GetMapping("/getcustomerbyid/{customerId}")
    public ResponseEntity<?> getCustomerById(@PathVariable String customerId) {
        Customer customer = customerService.getCustomerById(customerId).orElse(null);
        if (customer != null) {
            return ResponseEntity.ok(customer);
        }
        return ResponseEntity.status(404).body("Customer not found");
    }

    @PutMapping("/updatecustomer/{customerId}")
    public ResponseEntity<?> updateCustomer(@PathVariable String customerId, @RequestBody Customer customer) {
        // Prevent updating userName and password via this endpoint
        if (customer.getUserName() != null || customer.getPassword() != null) {
            return ResponseEntity.status(400).body("Username and password cannot be updated via this endpoint");
        }
        Customer updated = customerService.updateCustomer(customerId, customer);
        if (updated != null) {
            return ResponseEntity.ok(updated);
        }
        return ResponseEntity.status(404).body("Customer not found");
    }

    @PostMapping("/updateprofileimage/{customerId}")
    public ResponseEntity<?> updateProfileImage(@PathVariable String customerId, @RequestParam("image") MultipartFile image) throws IOException {
        if (image == null || image.isEmpty()) {
            return ResponseEntity.status(400).body("No image provided");
        }
        String imageUrl = cloudinaryService.uploadImage(image);
        Customer updatedCustomer = customerService.updateProfileImage(customerId, imageUrl);
        return ResponseEntity.ok(updatedCustomer);
    }

    @DeleteMapping("/deletecustomer/{customerId}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable String customerId) {
        customerService.deleteCustomer(customerId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/checkUsername")
    public ResponseEntity<Map<String, Boolean>> checkUsernameAvailability(@RequestParam String userName) {
        boolean exists = customerService.existsByUserName(userName);
        return ResponseEntity.ok().body(Map.of("exists", exists));
    }

    @PutMapping("/admin/verifycustomer/{customerId}")
    public ResponseEntity<?> verifyCustomer(@PathVariable String customerId) {
        Customer verifiedCustomer = customerService.verifyCustomer(customerId);
        return ResponseEntity.ok(verifiedCustomer);
    }

@PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody Map<String, String> request) {
        String userName = request.get("userName");
        String email = request.get("email");
        try {
            customerService.generateResetCode(userName, email);
            return ResponseEntity.ok("Reset code sent to your email");
        } catch (Exception e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    @PostMapping("/verify-reset-code")
    public ResponseEntity<?> verifyResetCode(@RequestBody Map<String, String> request) {
        String userName = request.get("userName");
        String code = request.get("code");
        try {
            boolean isValid = customerService.verifyResetCode(userName, code);
            if (isValid) {
                return ResponseEntity.ok("Code verified successfully");
            } else {
                return ResponseEntity.status(400).body("Invalid or expired code");
            }
        } catch (Exception e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }

    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody Map<String, String> request) {
        String userName = request.get("userName");
        String code = request.get("code");
        String newPassword = request.get("newPassword");
        try {
            customerService.resetPasswordWithCode(userName, code, newPassword);
            return ResponseEntity.ok("Password reset successfully");
        } catch (Exception e) {
            return ResponseEntity.status(400).body(e.getMessage());
        }
    }
}