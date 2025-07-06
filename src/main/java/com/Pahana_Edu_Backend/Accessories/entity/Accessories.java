package com.Pahana_Edu_Backend.Accessories.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
@Document(collection = "accessories")
public class Accessories {

    @Id
    private String id;

    private String itemName;

    private String categoryId;

    private String brand;

    private Double price;

    private Integer stockQty;

    private String image;

    private Double discount;

    private String description;

    private String status;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
