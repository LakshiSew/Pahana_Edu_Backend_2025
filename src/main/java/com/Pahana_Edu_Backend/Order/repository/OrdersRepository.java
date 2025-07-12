package com.Pahana_Edu_Backend.Order.repository;

import com.Pahana_Edu_Backend.Order.entity.Orders;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface OrdersRepository extends MongoRepository<Orders, String> {
    List<Orders> findByCustomerId(String customerId);
    List<Orders> findByStatus(String status);
     List<Orders> findByCustomerPhone(String customerPhone); 
}