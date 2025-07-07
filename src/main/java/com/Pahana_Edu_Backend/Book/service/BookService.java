package com.Pahana_Edu_Backend.Book.service;

import java.util.List;

import com.Pahana_Edu_Backend.Book.entity.Book;

public interface BookService {
    Book addBooks(String pdfUrl, String imageUrl, String title, String description, String category,Integer stockQty, Double price,String author,String status);
    Book getbooksById(String id);
   
    Book updateBooks(String id, String pdfUrl, String imageUrl, String title, String description, String category, Integer stockQty, Double price, String author, String status);
    void deleteBooks(String id);
    List<Book> getAllBooks();
    List<Book> getBooksByCategoryId(String categoryId);
}


