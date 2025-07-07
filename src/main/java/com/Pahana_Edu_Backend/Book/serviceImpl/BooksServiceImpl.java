package com.Pahana_Edu_Backend.Book.serviceImpl;

import java.util.List;

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
                     String description,String category,
                     Integer stockQty, Double price, String author, String status) {

    if (status == null || status.isEmpty()) {
        status = "Active";
    } else if (!status.equals("Active") && !status.equals("Inactive") && !status.equals("Out of Stock")) {
        throw new IllegalArgumentException("Status must be either 'Active', 'Inactive', or 'Out of Stock'");
    }

    Book book = new Book(pdfUrl, imageUrl, title, description,
                         category, stockQty, price, author, status);
    
    return bookRepository.save(book);
}
  
@Override
    public Book getbooksById(String id) {
        return bookRepository.findById(id).orElse(null);
    }

    @Override
    public Book updateBooks(String id, String pdfUrl, String imageUrl, String title,
                           String description, String category, Integer stockQty,
                           Double price, String author, String status) {
        Book existingBook = bookRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Book with ID " + id + " not found"));

        if (pdfUrl != null && !pdfUrl.isEmpty()) {
            existingBook.setPdfUrl(pdfUrl);
        }
        if (imageUrl != null && !imageUrl.isEmpty()) {
            existingBook.setImageUrl(imageUrl);
        }
        if (title != null && !title.isEmpty()) {
            existingBook.setTitle(title);
        }
        if (description != null && !description.isEmpty()) {
            existingBook.setDescription(description);
        }
        if (category != null && !category.isEmpty()) {
            existingBook.setCategory(category);
        }
        if (stockQty != null) {
            existingBook.setStockQty(stockQty);
        }
        if (price != null) {
            existingBook.setPrice(price);
        }
        if (author != null && !author.isEmpty()) {
            existingBook.setAuthor(author);
        }
        if (status != null && !status.isEmpty()) {
            if (!status.equals("Active") && !status.equals("Inactive") && !status.equals("Out of Stock")) {
                throw new IllegalArgumentException("Status must be either 'Active', 'Inactive', or 'Out of Stock'");
            }
            existingBook.setStatus(status);
        }

        return bookRepository.save(existingBook);
    }

    @Override
    public void deleteBooks(String id) {
        if (bookRepository.existsById(id)) {
            bookRepository.deleteById(id);
        } else {
            throw new RuntimeException("Book with ID " + id + " not found");
        }
    }

    @Override
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    @Override
    public List<Book> getBooksByCategoryId(String categoryId) {
        return bookRepository.findByCategory(categoryId);
    }
}

