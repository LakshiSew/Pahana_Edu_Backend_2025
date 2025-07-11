package com.Pahana_Edu_Backend.Book.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
@Document(collection = "books")
public class Book {

    @Id
    private String bookId;

    private String title;

    private String description;

    private String categoryId;

    private Integer stockQty;

    private Double price;

    private String image;

    private String author;

    private String aboutAuthor;

    private String language;

    private String publisherName;

    private Integer publicationYear;

    private Integer pages;

    private Double discount;

    private String status;
    
    private String pdf; 

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
