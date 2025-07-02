package com.Pahana_Edu_Backend.Admin.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Document(collection = "admins")
public class Admin {
    
    @Id
     private String id;
     private String userName;
     private String email;
     private String password;
     private String position;
     private String adminImage;
    
}
