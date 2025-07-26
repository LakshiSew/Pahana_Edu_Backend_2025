package com.Pahana_Edu_Backend.Customer.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Document(collection = "password_reset_tokens")
public class PasswordResetToken {
    @Id
    private String id;
    private String token; // Stores the 4-digit code
    private String userName;
    private LocalDateTime expiryDate;

    public PasswordResetToken() {}

    public PasswordResetToken(String token, String userName, LocalDateTime expiryDate) {
        this.token = token;
        this.userName = userName;
        this.expiryDate = expiryDate;
    }
}