package com.Pahana_Edu_Backend.Cart.repository;

import com.Pahana_Edu_Backend.Cart.entity.Cart;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface CartRepository extends MongoRepository<Cart, String> {
    List<Cart> findByUserId(String userId);
    void deleteByUserIdAndId(String userId, String id);
    void deleteByUserId(String userId);
}