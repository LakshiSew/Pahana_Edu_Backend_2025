package com.Pahana_Edu_Backend.Book.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Document(collection = "Book")
public class Book {
        @Id
    private String id;
    private String pdfUrl;
    private String imageUrl;
    private String title;
    private String description;
    private String category;
    private Integer stockQty;
    private Double price;
    private String author;
      private String status; 
    //   private String publisherName;// "Active", "Inactive", "Out of Stock"
    private LocalDateTime createdAt = LocalDateTime.now();

    // Getters and Setters  

      public Book(String pdfUrl, String imageUrl, String title, String description,
                   String category,Integer stockQty,Double price, String author,String status) {
        this.pdfUrl = pdfUrl;
        this.imageUrl = imageUrl;
        this.title = title;
        this.description = description;
        this.category = category;
        this.stockQty = stockQty;
        this.author = author;
        this.price = price;
        this.status = status;
        //  this.publisherName = publisherName;
        
    }
}
