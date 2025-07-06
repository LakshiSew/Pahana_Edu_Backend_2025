package com.Pahana_Edu_Backend.Accessories.controller;

import java.io.IOException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.Pahana_Edu_Backend.Accessories.entity.Accessories;
import com.Pahana_Edu_Backend.Accessories.service.AccessoriesService;
import com.Pahana_Edu_Backend.Cloudinary.CloudinaryService;

@RestController
@CrossOrigin
public class AccessoriesController {

    @Autowired
    private AccessoriesService accessoriesService;

    @Autowired
    private CloudinaryService cloudinaryService;

    @PostMapping("/auth/addaccessory")
    public ResponseEntity<?> addAccessory(
            @RequestParam("itemName") String itemName,
            @RequestParam("categoryId") String categoryId,
            @RequestParam("brand") String brand,
            @RequestParam("price") Double price,
            @RequestParam("stockQty") Integer stockQty,
            @RequestParam(value = "image", required = false) MultipartFile image,
            @RequestParam(value = "discount", required = false) Double discount,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "status", required = false) String status) throws IOException {
        try {
            Accessories accessory = new Accessories();
            accessory.setItemName(itemName);
            accessory.setCategoryId(categoryId);
            accessory.setBrand(brand);
            accessory.setPrice(price);
            accessory.setStockQty(stockQty);
            accessory.setDiscount(discount);
            accessory.setDescription(description);
            accessory.setStatus(status != null && !status.isEmpty() ? status : "Active");

            if (image != null && !image.isEmpty()) {
                String imageUrl = cloudinaryService.uploadImage(image);
                accessory.setImage(imageUrl);
            }

            Accessories savedAccessory = accessoriesService.addAccessory(accessory);
            return ResponseEntity.ok(savedAccessory);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (IOException e) {
            return ResponseEntity.status(500).body("File upload failed: " + e.getMessage());
        }
    }

    @PutMapping("/auth/updateaccessory/{id}")
    public ResponseEntity<?> updateAccessory(
            @PathVariable("id") String id,
            @RequestParam(value = "itemName", required = false) String itemName,
            @RequestParam(value = "categoryId", required = false) String categoryId,
            @RequestParam(value = "brand", required = false) String brand,
            @RequestParam(value = "price", required = false) Double price,
            @RequestParam(value = "stockQty", required = false) Integer stockQty,
            @RequestParam(value = "image", required = false) MultipartFile image,
            @RequestParam(value = "discount", required = false) Double discount,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "status", required = false) String status) throws IOException {
        try {
            Accessories accessory = new Accessories();
            accessory.setItemName(itemName);
            accessory.setCategoryId(categoryId);
            accessory.setBrand(brand);
            accessory.setPrice(price);
            accessory.setStockQty(stockQty);
            accessory.setDiscount(discount);
            accessory.setDescription(description);
            accessory.setStatus(status);

            if (image != null && !image.isEmpty()) {
                String imageUrl = cloudinaryService.uploadImage(image);
                accessory.setImage(imageUrl);
            }

            Accessories updatedAccessory = accessoriesService.updateAccessory(id, accessory);
            return ResponseEntity.ok(updatedAccessory);
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        } catch (IOException e) {
            return ResponseEntity.status(500).body("File upload failed: " + e.getMessage());
        }
    }

    @DeleteMapping("/auth/deleteaccessory/{id}")
    public ResponseEntity<String> deleteAccessory(@PathVariable("id") String id) {
        try {
            accessoriesService.deleteAccessory(id);
            return ResponseEntity.ok("Accessory with ID " + id + " has been deleted.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    @GetMapping("/auth/getaccessorybyid/{id}")
    public ResponseEntity<Accessories> getAccessoryById(@PathVariable("id") String id) {
        try {
            Accessories accessory = accessoriesService.getAccessoryById(id);
            return ResponseEntity.ok(accessory);
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(null);
        }
    }

    @GetMapping("/auth/getallaccessories")
    public ResponseEntity<List<Accessories>> getAllAccessories() {
        return ResponseEntity.ok(accessoriesService.getAllAccessories());
    }

    @GetMapping("/auth/getaccessoriesbycategoryid/{categoryId}")
    public ResponseEntity<?> getAccessoriesByCategoryId(@PathVariable("categoryId") String categoryId) {
        try {
            List<Accessories> accessories = accessoriesService.getAccessoriesByCategoryId(categoryId);
            return ResponseEntity.ok(accessories);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}