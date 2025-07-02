package com.Pahana_Edu_Backend.Customer.entity;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor 
@Document(collection = "customer")
public class Customer {
    private String customerId;

    private String customerName;

    private String customerEmail;

    private String address;

    private String status;

    private String userName;

    private String password;

    private String customerPhone;

}
