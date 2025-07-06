package com.Pahana_Edu_Backend.Book.serviceImpl;

import com.Pahana_Edu_Backend.Book.entity.Books;
import com.Pahana_Edu_Backend.Book.repository.BooksRepository;
import com.Pahana_Edu_Backend.Book.service.BooksService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BooksServiceImpl implements BooksService {

    @Autowired
    private BooksRepository booksRepository;

    @Override
    public Books addBook(Books books) {
        // Validate mandatory fields
        if (books.getCategoryId() == null || books.getCategoryId().trim().isEmpty()) {
            throw new IllegalArgumentException("Category ID is required");
        }
        if (books.getBookTitle() == null || books.getBookTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("Book title is required");
        }
        if (books.getAuthorName() == null || books.getAuthorName().trim().isEmpty()) {
            throw new IllegalArgumentException("Author name is required");
        }
        if (books.getPrice() == null || books.getPrice() < 0) {
            throw new IllegalArgumentException("Price is required and must be non-negative");
        }
        if (books.getStockQty() != null && books.getStockQty() < 0) {
            throw new IllegalArgumentException("Stock quantity cannot be negative");
        }
        if (books.getPages() != null && books.getPages() <= 0) {
            throw new IllegalArgumentException("Pages must be positive");
        }
        if (books.getDiscount() != null && books.getDiscount() < 0) {
            throw new IllegalArgumentException("Discount cannot be negative");
        }
        if (books.getPublicationYear() != null && books.getPublicationYear() <= 0) {
            throw new IllegalArgumentException("Publication year must be positive");
        }
        if (books.getStatus() != null && !books.getStatus().isEmpty() &&
                !books.getStatus().equals("Active") && !books.getStatus().equals("Inactive") &&
                !books.getStatus().equals("Out of Stock")) {
            throw new IllegalArgumentException("Status must be 'Active', 'Inactive', or 'Out of Stock'");
        }

        // Set default values
        if (books.getCreatedAt() == null) {
            books.setCreatedAt(LocalDateTime.now());
        }
        if (books.getUpdatedAt() == null) {
            books.setUpdatedAt(LocalDateTime.now());
        }
        if (books.getStatus() == null || books.getStatus().isEmpty()) {
            books.setStatus("Active");
        }

        return booksRepository.save(books);
    }

    @Override
    public Books updateBook(String id, Books books) {
        Books existingBook = booksRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book with ID " + id + " not found"));

        // Update fields only if provided and valid
        if (books.getCategoryId() != null && !books.getCategoryId().trim().isEmpty()) {
            existingBook.setCategoryId(books.getCategoryId().trim());
        }
        if (books.getBookTitle() != null && !books.getBookTitle().trim().isEmpty()) {
            existingBook.setBookTitle(books.getBookTitle().trim());
        }
        if (books.getAuthorName() != null && !books.getAuthorName().trim().isEmpty()) {
            existingBook.setAuthorName(books.getAuthorName().trim());
        }
        if (books.getAboutAuthor() != null) {
            existingBook.setAboutAuthor(books.getAboutAuthor().trim());
        }
        if (books.getImageUrl() != null) {
            existingBook.setImageUrl(books.getImageUrl());
        }
        if (books.getPrice() != null && books.getPrice() >= 0) {
            existingBook.setPrice(books.getPrice());
        }
        if (books.getDescription() != null) {
            existingBook.setDescription(books.getDescription().trim());
        }
        if (books.getPublisherName() != null) {
            existingBook.setPublisherName(books.getPublisherName().trim());
        }
        if (books.getLanguage() != null) {
            existingBook.setLanguage(books.getLanguage().trim());
        }
        if (books.getPublicationYear() != null && books.getPublicationYear() > 0) {
            existingBook.setPublicationYear(books.getPublicationYear());
        }
        if (books.getStockQty() != null && books.getStockQty() >= 0) {
            existingBook.setStockQty(books.getStockQty());
        }
        if (books.getPages() != null && books.getPages() > 0) {
            existingBook.setPages(books.getPages());
        }
        if (books.getDiscount() != null && books.getDiscount() >= 0) {
            existingBook.setDiscount(books.getDiscount());
        }
        if (books.getPreviewPdf() != null) {
            existingBook.setPreviewPdf(books.getPreviewPdf());
        }
        if (books.getStatus() != null && !books.getStatus().isEmpty()) {
            if (!books.getStatus().equals("Active") && !books.getStatus().equals("Inactive") &&
                    !books.getStatus().equals("Out of Stock")) {
                throw new IllegalArgumentException("Status must be 'Active', 'Inactive', or 'Out of Stock'");
            }
            existingBook.setStatus(books.getStatus());
        }

        existingBook.setUpdatedAt(LocalDateTime.now());
        return booksRepository.save(existingBook);
    }

    @Override
    public void deleteBook(String id) {
        if (!booksRepository.existsById(id)) {
            throw new RuntimeException("Book with ID " + id + " not found");
        }
        booksRepository.deleteById(id);
    }

    @Override
    public Books getBookById(String id) {
        return booksRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Book with ID " + id + " not found"));
    }

    @Override
    public List<Books> getAllBooks() {
        return booksRepository.findAll();
    }

    @Override
    public List<Books> getBooksByCategoryId(String categoryId) {
        if (categoryId == null || categoryId.trim().isEmpty()) {
            throw new IllegalArgumentException("Category ID is required");
        }
        return booksRepository.findByCategoryId(categoryId.trim());
    }
}