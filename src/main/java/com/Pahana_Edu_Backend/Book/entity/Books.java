package com.Pahana_Edu_Backend.Book.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "books")
public class Books {

    @Id
    private String bookId;

    private String categoryId;
    private String bookTitle;
    private String authorName;
    private String aboutAuthor;
    private String imageUrl;
    private Double price;
    private String description;
    private String publisherName;
    private String language;
    private Integer publicationYear;
    private Integer stockQty;
    private Integer pages;
    private Double discount;
    private String previewPdf;
    private String status; // e.g., "Active", "Inactive", "Out of Stock"
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

   
   


}