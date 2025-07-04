package com.Pahana_Edu_Backend.Category.controller;

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

import com.Pahana_Edu_Backend.Category.entity.Category;
import com.Pahana_Edu_Backend.Category.service.CategoryService;
import com.Pahana_Edu_Backend.Cloudinary.CloudinaryService;

@RestController
@CrossOrigin
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CloudinaryService cloudinaryService;

    @PostMapping("/auth/addcategory")
    public Category addCategory(
            @RequestParam("categoryName") String categoryName,
            @RequestParam("categoryImg") MultipartFile categoryImg,
            @RequestParam("description") String description,
            @RequestParam("status") String status,
            @RequestParam("type") String type // Required type parameter
    ) throws IOException {
        // Upload category image to Cloudinary
        String categoryImageUrl = cloudinaryService.uploadImage(categoryImg);

        // Create and save category details
        Category category = new Category();
        category.setCategoryName(categoryName);
        category.setCategoryImg(categoryImageUrl);
        category.setDescription(description);
        category.setStatus(status);
        category.setType(type); // Set the type

        return categoryService.addCategory(category);
    }

    @PutMapping("/updatecategory/{categoryId}")
    public ResponseEntity<?> updateCategory(
            @PathVariable("categoryId") String categoryId,
            @RequestParam(value = "categoryName", required = false) String categoryName,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "type", required = false) String type,
            @RequestParam(value = "categoryImg", required = false) MultipartFile categoryImg
    ) throws IOException {
        try {
            // Fetch existing category
            Category existingCategory = categoryService.getCategoryById(categoryId);
            if (existingCategory == null) {
                return ResponseEntity.status(404).body("Category with ID " + categoryId + " not found");
            }

            // Update fields only if provided
            if (categoryName != null && !categoryName.isEmpty()) {
                existingCategory.setCategoryName(categoryName);
            }
            if (description != null && !description.isEmpty()) {
                existingCategory.setDescription(description);
            }
            if (status != null && !status.isEmpty()) {
                existingCategory.setStatus(status);
            }
            if (type != null && !type.isEmpty()) {
                existingCategory.setType(type);
            }
            if (categoryImg != null && !categoryImg.isEmpty()) {
                String categoryImageUrl = cloudinaryService.uploadImage(categoryImg);
                existingCategory.setCategoryImg(categoryImageUrl);
            }

            Category updatedCategory = categoryService.updateCategory(categoryId, existingCategory);
            return ResponseEntity.ok(updatedCategory);
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    @DeleteMapping("/deletecategory/{categoryId}")
    public ResponseEntity<String> deleteCategory(@PathVariable("categoryId") String categoryId) {
        try {
            categoryService.deleteCategory(categoryId);
            return ResponseEntity.ok("Category with ID " + categoryId + " has been deleted.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    @GetMapping("/auth/getcategorybyid/{categoryId}")
    public ResponseEntity<Category> getCategoryById(@PathVariable("categoryId") String categoryId) {
        Category category = categoryService.getCategoryById(categoryId);
        if (category != null) {
            return ResponseEntity.ok(category);
        }
        return ResponseEntity.status(404).body(null);
    }

    @GetMapping("/auth/getallcategories")
    public ResponseEntity<List<Category>> getAllCategories() {
        return ResponseEntity.ok(categoryService.getAllCategory());
    }

    @GetMapping("/auth/getbookcategories")
    public ResponseEntity<List<Category>> getBookCategories() {
        return ResponseEntity.ok(categoryService.getBookCategories());
    }

    @GetMapping("/auth/getaccessorycategories")
    public ResponseEntity<List<Category>> getAccessoryCategories() {
        return ResponseEntity.ok(categoryService.getAccessoryCategories());
    }
}