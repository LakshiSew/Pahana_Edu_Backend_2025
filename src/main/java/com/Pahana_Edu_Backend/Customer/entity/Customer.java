package com.Pahana_Edu_Backend.Customer.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;


import lombok.Getter;

import lombok.Setter;

@Getter
@Setter

@Document(collection = "customer")
public class Customer {
     @Id
    private String customerId;

    private String customerName;

    private String customerEmail;

    private String address;

    private String status;
    
    private String userName;

    private String password;

    private String customerPhone;

    private String profileImage;

}
