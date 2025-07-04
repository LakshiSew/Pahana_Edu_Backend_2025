package com.Pahana_Edu_Backend.Category.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.Pahana_Edu_Backend.Category.entity.Category;

@Repository
public interface CategoryRepository extends MongoRepository<Category, String>{
    
}
