package com.Pahana_Edu_Backend.Order.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Document(collection = "orders")
public class Orders {
    @Id
    private String id;
    private String customerId;
    private String customerName;
    private String customerEmail;
    private String address;
    private String orderDate;
    private String categoryId;
    private List<String> productIds; // IDs of Books or Accessories
    private String productType; // "Book" or "Accessory"
    private Double totalPrice;
    private String status; // "Pending", "Confirmed", "Canceled"
    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt;
}