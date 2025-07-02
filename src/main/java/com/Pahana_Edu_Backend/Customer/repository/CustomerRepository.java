package com.Pahana_Edu_Backend.Customer.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.Pahana_Edu_Backend.Customer.entity.Customer;

public interface CustomerRepository extends MongoRepository<Customer, String>{
        Optional<Customer> findByUserName(String userName);
    boolean existsByUserName(String userName);

}
 