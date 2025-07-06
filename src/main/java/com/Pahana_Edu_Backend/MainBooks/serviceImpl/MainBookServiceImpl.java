package com.Pahana_Edu_Backend.MainBooks.serviceImpl;

import com.Pahana_Edu_Backend.MainBooks.entity.MainBooks;
import com.Pahana_Edu_Backend.MainBooks.repository.MainBooksRepository;
import com.Pahana_Edu_Backend.MainBooks.service.MainBookService;
import com.Pahana_Edu_Backend.Category.repository.CategoryRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MainBookServiceImpl implements MainBookService {

    private final MainBooksRepository mainBooksRepository;
    private final CategoryRepository categoryRepository;

    @Autowired
    public MainBookServiceImpl(MainBooksRepository mainBooksRepository, CategoryRepository categoryRepository) {
        this.mainBooksRepository = mainBooksRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public MainBooks addBook(String categoryId, String bookTitle, String authorName, String aboutAuthor,
                              String image, Double price, String description, String publisherName,
                              String language, Integer publicationYear, Integer stockQty, Integer pages,
                              Double discount, String status) {

        if (bookTitle == null || bookTitle.isEmpty()) {
            throw new IllegalArgumentException("Book title is required");
        }
        if (categoryId == null || !categoryRepository.existsById(categoryId)) {
            throw new IllegalArgumentException("Valid category ID is required");
        }
        if (price == null || price < 0) {
            throw new IllegalArgumentException("Price must be non-negative");
        }
        if (stockQty == null || stockQty < 0) {
            throw new IllegalArgumentException("Stock quantity must be non-negative");
        }

        MainBooks book = new MainBooks();
        book.setCategoryId(categoryId);
        book.setBookTitle(bookTitle);
        book.setAuthorName(authorName);
        book.setAboutAuthor(aboutAuthor);
        book.setImage(image);
        book.setPrice(price);
        book.setDescription(description);
        book.setPublisherName(publisherName);
        book.setLanguage(language);
        book.setPublicationYear(publicationYear);
        book.setStockQty(stockQty);
        book.setPages(pages);
        book.setDiscount(discount);
        book.setStatus((status == null || status.isEmpty()) ? "Active" : status);
        book.setCreatedAt(LocalDateTime.now());
        book.setUpdatedAt(LocalDateTime.now());

        return mainBooksRepository.save(book);
    }

    @Override
    public MainBooks updateBook(String bookId, String categoryId, String bookTitle, String authorName,
                                String aboutAuthor, String image, Double price, String description,
                                String publisherName, String language, Integer publicationYear,
                                Integer stockQty, Integer pages, Double discount, String status) {

        MainBooks existing = mainBooksRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book with ID " + bookId + " not found"));

        if (bookTitle != null) existing.setBookTitle(bookTitle);
        if (authorName != null) existing.setAuthorName(authorName);
        if (aboutAuthor != null) existing.setAboutAuthor(aboutAuthor);
        if (image != null) existing.setImage(image);
        if (price != null && price >= 0) existing.setPrice(price);
        if (description != null) existing.setDescription(description);
        if (publisherName != null) existing.setPublisherName(publisherName);
        if (language != null) existing.setLanguage(language);
        if (publicationYear != null) existing.setPublicationYear(publicationYear);
        if (stockQty != null && stockQty >= 0) existing.setStockQty(stockQty);
        if (pages != null && pages >= 0) existing.setPages(pages);
        if (discount != null && discount >= 0) existing.setDiscount(discount);
        if (status != null && (status.equals("Active") || status.equals("Inactive") || status.equals("Out of Stock"))) {
            existing.setStatus(status);
        }
        if (categoryId != null && categoryRepository.existsById(categoryId)) {
            existing.setCategoryId(categoryId);
        }

        existing.setUpdatedAt(LocalDateTime.now());

        return mainBooksRepository.save(existing);
    }

    @Override
    public void deleteBook(String bookId) {
        if (!mainBooksRepository.existsById(bookId)) {
            throw new RuntimeException("Book with ID " + bookId + " not found");
        }
        mainBooksRepository.deleteById(bookId);
    }

    @Override
    public MainBooks getBookById(String bookId) {
        return mainBooksRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book with ID " + bookId + " not found"));
    }

    @Override
    public List<MainBooks> getAllBooks() {
        return mainBooksRepository.findAll();
    }

    @Override
    public List<MainBooks> getBooksByCategoryId(String categoryId) {
        if (!categoryRepository.existsById(categoryId)) {
            throw new IllegalArgumentException("Category with ID " + categoryId + " not found");
        }
        return mainBooksRepository.findByCategoryId(categoryId);
    }
}
