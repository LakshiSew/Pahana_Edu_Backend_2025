package com.Pahana_Edu_Backend.Help.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Document(collection = "help")
@Getter
@Setter
public class Help {

    @Id
    private String id;

    private String fullName;

    private String email;

    private String phoneNumber;

    private String message;

    private String reply;

    private LocalDateTime submittedAt;

    private LocalDateTime repliedAt;
}