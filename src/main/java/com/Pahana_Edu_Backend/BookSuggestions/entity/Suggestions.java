package com.Pahana_Edu_Backend.BookSuggestions.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Document(collection = "suggestions")
public class Suggestions {
    @Id
    private String id;

    private String name;

    private String email;

    private String bookTitle;

    private String authorName;

    private String status;
}