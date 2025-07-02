package com.Pahana_Edu_Backend.Customer.service;

import java.util.List;
import java.util.Optional;

import com.Pahana_Edu_Backend.Customer.entity.Customer;

public interface CustomerService {

    Customer addCustomer(Customer customer);

    List<Customer> getAllCustomers();

    Optional<Customer> getCustomerById(String customerId);

    void deleteCustomer(String customerId);

    Customer updateCustomer(String customerId, Customer customer);

    boolean existsByUserName(String userName);
}


