package com.Pahana_Edu_Backend.Book.service;

import com.Pahana_Edu_Backend.Book.entity.Book;

public interface BookService {
    Book addBooks(String pdfUrl, String imageUrl, String title, String description,String publisherName, String category,Integer stockQty, Double price,String author);
    Book getbooksById(String id);
}