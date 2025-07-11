package com.Pahana_Edu_Backend.Book.serviceImpl;

import com.Pahana_Edu_Backend.Book.entity.Book;
import com.Pahana_Edu_Backend.Book.repository.BookRepository;
import com.Pahana_Edu_Backend.Book.service.BookService;
import com.Pahana_Edu_Backend.Category.repository.CategoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class BookServiceImpl implements BookService {

    private static final Logger logger = LoggerFactory.getLogger(BookServiceImpl.class);

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public Book addBook(Book book) {
        // Validate required fields
        if (book.getTitle() == null || book.getTitle().trim().isEmpty()) {
            logger.error("Title is required");
            throw new IllegalArgumentException("Title is required");
        }
        if (book.getCategoryId() == null || book.getCategoryId().trim().isEmpty()) {
            logger.error("Category ID is required");
            throw new IllegalArgumentException("Category ID is required");
        }
        if (!categoryRepository.existsById(book.getCategoryId())) {
            logger.error("Category with ID {} not found", book.getCategoryId());
            throw new IllegalArgumentException("Category with ID " + book.getCategoryId() + " not found");
        }
        if (book.getPrice() == null || book.getPrice() < 0) {
            logger.error("Price is required and must be non-negative");
            throw new IllegalArgumentException("Price is required and must be non-negative");
        }
        if (book.getStockQty() == null || book.getStockQty() < 0) {
            logger.error("Stock quantity must be non-negative");
            throw new IllegalArgumentException("Stock quantity must be non-negative");
        }
        if (book.getPages() != null && book.getPages() < 0) {
            logger.error("Pages must be non-negative");
            throw new IllegalArgumentException("Pages must be non-negative");
        }

        // Validate status
        if (book.getStatus() != null && !book.getStatus().isEmpty()) {
            if (!book.getStatus().equals("Active") && !book.getStatus().equals("Inactive")) {
                logger.error("Status must be 'Active' or 'Inactive'");
                throw new IllegalArgumentException("Status must be 'Active' or 'Inactive'");
            }
        } else {
            book.setStatus("Active");
        }

        // Set timestamps
        if (book.getCreatedAt() == null) {
            book.setCreatedAt(LocalDateTime.now());
        }
        if (book.getUpdatedAt() == null) {
            book.setUpdatedAt(LocalDateTime.now());
        }

        Book savedBook = bookRepository.save(book);
        logger.info("Added book: {}", savedBook.getBookId());
        return savedBook;
    }

    @Override
    public Book updateBook(String id, Book book) {
        Book existingBook = bookRepository.findById(id).orElse(null);
        if (existingBook == null) {
            logger.error("Book not found with ID: {}", id);
            throw new RuntimeException("Book with ID " + id + " not found");
        }

        // Update fields if provided
        if (book.getTitle() != null && !book.getTitle().trim().isEmpty()) {
            existingBook.setTitle(book.getTitle());
        }
        if (book.getCategoryId() != null && !book.getCategoryId().trim().isEmpty()) {
            if (!categoryRepository.existsById(book.getCategoryId())) {
                logger.error("Category with ID {} not found", book.getCategoryId());
                throw new IllegalArgumentException("Category with ID " + book.getCategoryId() + " not found");
            }
            existingBook.setCategoryId(book.getCategoryId());
        }
        if (book.getAuthor() != null && !book.getAuthor().trim().isEmpty()) {
            existingBook.setAuthor(book.getAuthor());
        }
        if (book.getAboutAuthor() != null && !book.getAboutAuthor().trim().isEmpty()) {
            existingBook.setAboutAuthor(book.getAboutAuthor());
        }
        if (book.getPrice() != null && book.getPrice() >= 0) {
            existingBook.setPrice(book.getPrice());
        }
        if (book.getStockQty() != null && book.getStockQty() >= 0) {
            existingBook.setStockQty(book.getStockQty());
        }
        if (book.getImage() != null && !book.getImage().trim().isEmpty()) {
            existingBook.setImage(book.getImage());
        }
        if (book.getPdf() != null && !book.getPdf().trim().isEmpty()) {
            existingBook.setPdf(book.getPdf());
        }
        if (book.getDiscount() != null && book.getDiscount() >= 0) {
            existingBook.setDiscount(book.getDiscount());
        }
        if (book.getDescription() != null && !book.getDescription().trim().isEmpty()) {
            existingBook.setDescription(book.getDescription());
        }
        if (book.getStatus() != null && !book.getStatus().isEmpty()) {
            if (!book.getStatus().equals("Active") && !book.getStatus().equals("Inactive")) {
                logger.error("Status must be 'Active' or 'Inactive'");
                throw new IllegalArgumentException("Status must be 'Active' or 'Inactive'");
            }
            existingBook.setStatus(book.getStatus());
        }
        if (book.getLanguage() != null && !book.getLanguage().trim().isEmpty()) {
            existingBook.setLanguage(book.getLanguage());
        }
        if (book.getPublisherName() != null && !book.getPublisherName().trim().isEmpty()) {
            existingBook.setPublisherName(book.getPublisherName());
        }
        if (book.getPublicationYear() != null && book.getPublicationYear() >= 0) {
            existingBook.setPublicationYear(book.getPublicationYear());
        }
        if (book.getPages() != null && book.getPages() >= 0) {
            existingBook.setPages(book.getPages());
        }

        existingBook.setUpdatedAt(LocalDateTime.now());
        Book updatedBook = bookRepository.save(existingBook);
        logger.info("Updated book: {}", id);
        return updatedBook;
    }

    @Override
    public void deleteBook(String id) {
        if (bookRepository.existsById(id)) {
            bookRepository.deleteById(id);
            logger.info("Deleted book: {}", id);
        } else {
            logger.error("Book not found for deletion with ID: {}", id);
            throw new RuntimeException("Book with ID " + id + " not found");
        }
    }

    @Override
    public Book getBookById(String id) {
        Book book = bookRepository.findById(id).orElse(null);
        if (book == null) {
            logger.error("Book not found with ID: {}", id);
            throw new RuntimeException("Book with ID " + id + " not found");
        }
        logger.info("Fetched book: {}", id);
        return book;
    }

    @Override
    public List<Book> getAllBooks() {
        List<Book> books = bookRepository.findAll();
        logger.info("Fetched {} books", books.size());
        return books;
    }

    @Override
    public List<Book> getBooksByCategoryId(String categoryId) {
        if (categoryId == null || categoryId.trim().isEmpty()) {
            logger.error("Category ID is required");
            throw new IllegalArgumentException("Category ID is required");
        }
        if (!categoryRepository.existsById(categoryId)) {
            logger.error("Category with ID {} not found", categoryId);
            throw new IllegalArgumentException("Category with ID " + categoryId + " not found");
        }

        List<Book> books = bookRepository.findByCategoryId(categoryId);
        logger.info("Fetched {} books for category ID: {}", books.size(), categoryId);
        return books;
    }
}