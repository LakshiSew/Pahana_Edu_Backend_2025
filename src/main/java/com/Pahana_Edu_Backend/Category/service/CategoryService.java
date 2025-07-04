package com.Pahana_Edu_Backend.Category.service;

import java.util.List;

import com.Pahana_Edu_Backend.Category.entity.Category;

public interface CategoryService {

        Category addCategory(Category category);
    Category updateCategory(String categoryId, Category category);
    void deleteCategory(String categoryId);
    Category getCategoryById(String categoryId);
    List<Category> getAllCategory();
    List<Category> getBookCategories();
    List<Category> getAccessoryCategories();

}
