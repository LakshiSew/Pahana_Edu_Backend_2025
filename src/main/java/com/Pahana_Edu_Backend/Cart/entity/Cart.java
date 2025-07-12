package com.Pahana_Edu_Backend.Cart.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
@Document(collection = "carts")
public class Cart {

    @Id
    private String id;

    private String userId;

    private String productId;

    private String productType;

    private Integer quantity;

    private String name;

    private Double price;

    private String image;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}