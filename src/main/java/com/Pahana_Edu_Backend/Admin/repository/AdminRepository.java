package com.Pahana_Edu_Backend.Admin.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.Pahana_Edu_Backend.Admin.entity.Admin;

public interface AdminRepository extends MongoRepository<Admin, String> {
   
 Optional<Admin> findByUserName(String userName); 

 boolean existsByUserName(String userName); 
    
}
