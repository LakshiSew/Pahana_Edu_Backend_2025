package com.Pahana_Edu_Backend.MainBooks.service;

import com.Pahana_Edu_Backend.MainBooks.entity.MainBooks;

import java.util.List;

public interface MainBookService {

    MainBooks addBook(String categoryId, String bookTitle, String authorName, String aboutAuthor,
                       String image, Double price, String description, String publisherName,
                       String language, Integer publicationYear, Integer stockQty, Integer pages,
                       Double discount, String status);

    MainBooks updateBook(String bookId, String categoryId, String bookTitle, String authorName, String aboutAuthor,
                         String image, Double price, String description, String publisherName,
                         String language, Integer publicationYear, Integer stockQty, Integer pages,
                         Double discount, String status);

    void deleteBook(String bookId);

    MainBooks getBookById(String bookId);

    List<MainBooks> getAllBooks();

    List<MainBooks> getBooksByCategoryId(String categoryId);
}
