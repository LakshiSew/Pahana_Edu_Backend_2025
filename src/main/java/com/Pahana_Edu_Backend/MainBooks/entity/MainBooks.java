package com.Pahana_Edu_Backend.MainBooks.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "mainBooks")
public class MainBooks {

    @Id
    private String bookId;

    private String categoryId;
    private String bookTitle;
    private String authorName;
    private String aboutAuthor;
    private String image;
    private Double price;
    private String description;
    private String publisherName;
    private String language;
    private Integer publicationYear;
    private Integer stockQty;
    private Integer pages;
    private Double discount;
    private String status; // "Active", "Inactive", "Out of Stock"

    private LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt;

    // You can also define additional constructors if needed
    public MainBooks(String categoryId, String bookTitle, String authorName, String aboutAuthor,
                     String image, Double price, String description, String publisherName,
                     String language, Integer publicationYear, Integer stockQty, Integer pages,
                     Double discount, String status) {
        this.categoryId = categoryId;
        this.bookTitle = bookTitle;
        this.authorName = authorName;
        this.aboutAuthor = aboutAuthor;
        this.image = image;
        this.price = price;
        this.description = description;
        this.publisherName = publisherName;
        this.language = language;
        this.publicationYear = publicationYear;
        this.stockQty = stockQty;
        this.pages = pages;
        this.discount = discount;
        this.status = status;
        this.createdAt = LocalDateTime.now();
    }
}
