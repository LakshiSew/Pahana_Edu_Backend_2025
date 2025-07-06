package com.Pahana_Edu_Backend.Book.service;

import com.Pahana_Edu_Backend.Book.entity.Books;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface BooksService {
    Books addBook(Books books);
    Books updateBook(String id, Books books);
    void deleteBook(String id);
    Books getBookById(String id);
    List<Books> getAllBooks();
    List<Books> getBooksByCategoryId(String categoryId);
}