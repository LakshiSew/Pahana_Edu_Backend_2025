package com.Pahana_Edu_Backend.Category.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
@Document(collection = "categories")
public class Category {
    
    @Id
    private String categoryId;
    
    private String categoryName;
    
    private String categoryImg;
    
    private String description;
    
    private String status;
    
    private String type;
    
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
}