package com.Pahana_Edu_Backend.Customer.serviceImpl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Pahana_Edu_Backend.Customer.entity.Customer;
import com.Pahana_Edu_Backend.Customer.repository.CustomerRepository;
import com.Pahana_Edu_Backend.Customer.service.CustomerService;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    @Override
    public Customer addCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    @Override
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    @Override
    public Optional<Customer> getCustomerById(String customerId) {
        return customerRepository.findById(customerId);
    }

    @Override
    public void deleteCustomer(String customerId) {
        customerRepository.deleteById(customerId);
    }

    @Override
    public Customer updateCustomer(String customerId, Customer customer) {
        Optional<Customer> existingCustomer = customerRepository.findById(customerId);

        if (existingCustomer.isPresent()) {
            Customer custToUpdate = existingCustomer.get();

            // Update fields (you can customize this)
            custToUpdate.setCustomerName(customer.getCustomerName());
            custToUpdate.setCustomerEmail(customer.getCustomerEmail());
            custToUpdate.setAddress(customer.getAddress());
            custToUpdate.setStatus(customer.getStatus());
            custToUpdate.setUserName(customer.getUserName());
            custToUpdate.setPassword(customer.getPassword());
            custToUpdate.setCustomerPhone(customer.getCustomerPhone());

            return customerRepository.save(custToUpdate);
        } else {
            throw new RuntimeException("Customer not found with id: " + customerId);
        }
    }

    @Override
    public boolean existsByUserName(String userName) {
        return customerRepository.existsByUserName(userName);
    }
}
