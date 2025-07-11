package com.Pahana_Edu_Backend.Book.service;

import com.Pahana_Edu_Backend.Book.entity.Book;
import java.util.List;

public interface BookService {
    Book addBook(Book book);
    Book updateBook(String id, Book book);
    void deleteBook(String id);
    Book getBookById(String id);
    List<Book> getAllBooks();
    List<Book> getBooksByCategoryId(String categoryId); 
}
