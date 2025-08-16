
     package com.Pahana_Edu_Backend.Order.entity;

     import org.springframework.data.annotation.Id;
     import org.springframework.data.mongodb.core.mapping.Document;
     import org.springframework.data.mongodb.core.mapping.Field;
     import lombok.Getter;
     import lombok.Setter;
     import java.time.LocalDateTime;
     import java.util.HashMap;
     import java.util.List;
     import java.util.Map;

     @Getter
     @Setter
     @Document(collection = "orders")
     public class Orders {
         @Id
         private String id;
         private String customerId;
         private String customerName;
         private String customerEmail;
         private String customerPhone;
         private String address;
         private String orderDate;
         private String categoryId;
         @Field("productIds") // Explicitly map to MongoDB field
         private List<String> productIds;
         @Field("productQuantities") // Explicitly map to MongoDB field
         private Map<String, Integer> productQuantities;
         @Field("productTypes") // Explicitly map to MongoDB field
         private Map<String, String> productTypes;
         private Double totalPrice;
         private String status;
         private LocalDateTime createdAt = LocalDateTime.now();
         private LocalDateTime updatedAt;

         public Map<String, String> getProductTypes() {
             return productTypes != null ? productTypes : new HashMap<>();
         }

         public void setProductTypes(Map<String, String> productTypes) {
             this.productTypes = productTypes;
         }

         public Map<String, Integer> getProductQuantities() {
             return productQuantities != null ? productQuantities : new HashMap<>();
         }

         public void setProductQuantities(Map<String, Integer> productQuantities) {
             this.productQuantities = productQuantities;
         }
     }