package com.Pahana_Edu_Backend.Feedback.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Document(collection = "feedbacks")
public class Feedback {

    @Id
    private String id;
    private String quote;
    private String name;
    private String email;
    private String position;
    private String image;
    private int rating;
}
