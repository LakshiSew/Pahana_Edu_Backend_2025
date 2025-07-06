package com.Pahana_Edu_Backend.Category.serviceImpl;

import com.Pahana_Edu_Backend.Category.entity.Category;
import com.Pahana_Edu_Backend.Category.repository.CategoryRepository;
import com.Pahana_Edu_Backend.Category.service.CategoryService;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CategoryServiceImpl implements CategoryService {

    private static final Logger logger = LoggerFactory.getLogger(CategoryServiceImpl.class);

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public Category addCategory(Category category) {
        if (category.getCreatedAt() == null) {
            category.setCreatedAt(LocalDateTime.now());
        }
        if (category.getUpdatedAt() == null) {
            category.setUpdatedAt(LocalDateTime.now());
        }
        // Validate status if provided
        if (category.getStatus() != null && !category.getStatus().isEmpty()) {
            if (!category.getStatus().equals("Active") && !category.getStatus().equals("Inactive")) {
                throw new IllegalArgumentException("Status must be 'Active' or 'Inactive'");
            }
        } else {
            category.setStatus("Active"); // Default to "Active" if not provided
        }
        // Validate type if provided
        if (category.getType() != null && !category.getType().isEmpty()) {
            if (!category.getType().equals("book") && !category.getType().equals("accessory")) {
                throw new IllegalArgumentException("Type must be 'book' or 'accessory'");
            }
        } else {
            category.setType("book"); // Default to "book" if not provided
        }
        Category savedCategory = categoryRepository.save(category);
        logger.info("Added category: {}", savedCategory.getCategoryId());
        return savedCategory;
    }

    @Override
    public Category updateCategory(String categoryId, Category category) {
        Category existingCategory = categoryRepository.findById(categoryId).orElse(null);
        if (existingCategory != null) {
            if (category.getCategoryName() != null && !category.getCategoryName().isEmpty()) {
                existingCategory.setCategoryName(category.getCategoryName());
            }
            if (category.getCategoryImg() != null) {
                existingCategory.setCategoryImg(category.getCategoryImg());
            }
            if (category.getDescription() != null) {
                existingCategory.setDescription(category.getDescription());
            }
            if (category.getStatus() != null && !category.getStatus().isEmpty()) {
                if (!category.getStatus().equals("Active") && !category.getStatus().equals("Inactive")) {
                    throw new IllegalArgumentException("Status must be 'Active' or 'Inactive'");
                }
                existingCategory.setStatus(category.getStatus());
            }
            if (category.getType() != null && !category.getType().isEmpty()) {
                if (!category.getType().equals("book") && !category.getType().equals("accessory")) {
                    throw new IllegalArgumentException("Type must be 'book' or 'accessory'");
                }
                existingCategory.setType(category.getType());
            }
            existingCategory.setUpdatedAt(LocalDateTime.now());
            Category updatedCategory = categoryRepository.save(existingCategory);
            logger.info("Updated category: {}", categoryId);
            return updatedCategory;
        }
        logger.error("Category not found with ID: {}", categoryId);
        throw new RuntimeException("Category with ID " + categoryId + " not found");
    }

    @Override
    public void deleteCategory(String categoryId) {
        if (categoryRepository.existsById(categoryId)) {
            categoryRepository.deleteById(categoryId);
            logger.info("Deleted category: {}", categoryId);
        } else {
            logger.error("Category not found for deletion with ID: {}", categoryId);
            throw new RuntimeException("Category with ID " + categoryId + " not found");
        }
    }

    @Override
    public Category getCategoryById(String categoryId) {
        Category category = categoryRepository.findById(categoryId).orElse(null);
        if (category == null) {
            logger.error("Category not found with ID: {}", categoryId);
            throw new RuntimeException("Category with ID " + categoryId + " not found");
        }
        logger.info("Fetched category: {}", categoryId);
        return category;
    }

    @Override
    public List<Category> getAllCategory() {
        List<Category> categories = categoryRepository.findAll();
        logger.info("Fetched {} categories", categories.size());
        return categories;
    }

    @Override
    public List<Category> getBookCategories() {
        List<Category> bookCategories = categoryRepository.findAll().stream()
            .filter(category -> "book".equals(category.getType()))
            .collect(Collectors.toList());
        logger.info("Fetched {} book categories", bookCategories.size());
        return bookCategories;
    }

    @Override
    public List<Category> getAccessoryCategories() {
        List<Category> accessoryCategories = categoryRepository.findAll().stream()
            .filter(category -> "accessory".equals(category.getType()))
            .collect(Collectors.toList());
        logger.info("Fetched {} accessory categories", accessoryCategories.size());
        return accessoryCategories;
    }
}