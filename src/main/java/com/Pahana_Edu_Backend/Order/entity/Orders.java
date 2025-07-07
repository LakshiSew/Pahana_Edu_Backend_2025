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

    public Orders() {
    }

    public Orders(String customerId, String customerName, String customerEmail, String address,
                  String orderDate, String categoryId, List<String> productIds, String productType,
                  Double totalPrice, String status) {
        this.customerId = customerId;
        this.customerName = customerName;
        this.customerEmail = customerEmail;
        this.address = address;
        this.orderDate = orderDate;
        this.categoryId = categoryId;
        this.productIds = productIds;
        this.productType = productType;
        this.totalPrice = totalPrice;
        this.status = status;
    }
}