// Accessories Repository
package com.Pahana_Edu_Backend.Accessories.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import com.Pahana_Edu_Backend.Accessories.entity.Accessories;

@Repository
public interface AccessoriesRepository extends MongoRepository<Accessories, String> {
        List<Accessories> findByCategoryId(String categoryId); // Added method for querying by categoryId

}
