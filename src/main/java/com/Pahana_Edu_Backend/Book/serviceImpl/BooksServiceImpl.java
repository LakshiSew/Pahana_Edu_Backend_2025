package com.Pahana_Edu_Backend.Book.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Pahana_Edu_Backend.Book.entity.Book;
import com.Pahana_Edu_Backend.Book.repository.BookRepository;
import com.Pahana_Edu_Backend.Book.service.BookService;

@Service
public class BooksServiceImpl implements BookService {
        private final BookRepository bookRepository;

    @Autowired
    public BooksServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public Book addBooks(String pdfUrl, String imageUrl, String title,
                              String description, String category,Integer stockQty, Double price,String author) {
        Book book = new Book(pdfUrl, imageUrl, title, description, category,stockQty, price, author);
        return bookRepository.save(book);
    }

    @Override
    public Book getbooksById(String id) {
        return bookRepository.findById(id).orElse(null);
    }
}
