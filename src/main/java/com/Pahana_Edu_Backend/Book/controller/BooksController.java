package com.Pahana_Edu_Backend.Book.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.Pahana_Edu_Backend.Book.entity.Books;
import com.Pahana_Edu_Backend.Book.service.BooksService;
import com.Pahana_Edu_Backend.Cloudinary.CloudinaryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@RestController
@CrossOrigin
public class BooksController {

    @Autowired
    private BooksService booksService;

    @Autowired
    private CloudinaryService cloudinaryService;
    private static final Logger log = LoggerFactory.getLogger(BooksController.class);


 @PostMapping("/auth/addbook")
public ResponseEntity<?> addBook(
        @RequestParam("categoryId") String categoryId,
        @RequestParam("bookTitle") String bookTitle,
        @RequestParam("authorName") String authorName,
        @RequestParam(value = "aboutAuthor", required = false) String aboutAuthor,
        @RequestParam(value = "image", required = false) MultipartFile image,
        @RequestParam("price") Double price,
        @RequestParam(value = "description", required = false) String description,
        @RequestParam(value = "publisherName", required = false) String publisherName,
        @RequestParam(value = "language", required = false) String language,
        @RequestParam(value = "publicationYear", required = false) Integer publicationYear,
        @RequestParam(value = "stockQty", required = false) Integer stockQty,
        @RequestParam(value = "pages", required = false) Integer pages,
        @RequestParam(value = "discount", required = false) Double discount,
        @RequestParam(value = "previewPdf", required = false) MultipartFile previewPdf,
        @RequestParam(value = "status", required = false) String status
) {
    try {
        // Log incoming request details
        log.info("Received addBook request: categoryId={}, bookTitle={}", categoryId, bookTitle);
        log.info("Image file: {}", image != null ? image.getOriginalFilename() : "null");
        log.info("PDF file: {}", previewPdf != null ? previewPdf.getOriginalFilename() : "null");

        // Validate required fields
        if (categoryId == null || categoryId.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Category ID is required");
        }
        if (bookTitle == null || bookTitle.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Book title is required");
        }
        if (authorName == null || authorName.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Author name is required");
        }
        if (price == null || price < 0) {
            return ResponseEntity.badRequest().body("Price is required and must be non-negative");
        }

        // Create and populate Books object
        Books book = new Books();
        book.setCategoryId(categoryId.trim());
        book.setBookTitle(bookTitle.trim());
        book.setAuthorName(authorName.trim());
        book.setAboutAuthor(aboutAuthor != null ? aboutAuthor.trim() : null);
        book.setPrice(price);
        book.setDescription(description != null ? description.trim() : null);
        book.setPublisherName(publisherName != null ? publisherName.trim() : null);
        book.setLanguage(language != null ? language.trim() : null);
        book.setPublicationYear(publicationYear);
        book.setStockQty(stockQty);
        book.setPages(pages);
        book.setDiscount(discount);
        book.setStatus(status != null && !status.trim().isEmpty() ? status.trim() : "Active");

        // Handle image upload
        if (image != null && !image.isEmpty()) {
            log.info("Uploading image: {}", image.getOriginalFilename());
            String imageUrl = cloudinaryService.uploadImage(image);
            book.setImageUrl(imageUrl);
            log.info("Image uploaded: {}", imageUrl);
        }

        // Handle PDF upload
        if (previewPdf != null && !previewPdf.isEmpty()) {
            log.info("Uploading PDF: {}", previewPdf.getOriginalFilename());
            String pdfUrl = cloudinaryService.uploadFile(previewPdf);
            book.setPreviewPdf(pdfUrl);
            log.info("PDF uploaded: {}", pdfUrl);
        }

        // Save book to database
        log.info("Saving book to database...");
        Books savedBook = booksService.addBook(book);
        log.info("Book saved successfully: {}", savedBook.getBookId());
        return ResponseEntity.ok(savedBook);
    } catch (IOException e) {
        log.error("File upload failed: {}", e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("File upload failed: " + e.getMessage());
    } catch (Exception e) {
        log.error("Error in addBook: {}", e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
    }
}

    @PutMapping("/updatebook/{bookId}")
    public ResponseEntity<?> updateBook(
            @PathVariable String bookId,
            @RequestParam(value = "categoryId", required = false) String categoryId,
            @RequestParam(value = "bookTitle", required = false) String bookTitle,
            @RequestParam(value = "authorName", required = false) String authorName,
            @RequestParam(value = "aboutAuthor", required = false) String aboutAuthor,
            @RequestParam(value = "image", required = false) MultipartFile image,
            @RequestParam(value = "price", required = false) Double price,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "publisherName", required = false) String publisherName,
            @RequestParam(value = "language", required = false) String language,
            @RequestParam(value = "publicationYear", required = false) Integer publicationYear,
            @RequestParam(value = "stockQty", required = false) Integer stockQty,
            @RequestParam(value = "pages", required = false) Integer pages,
            @RequestParam(value = "discount", required = false) Double discount,
            @RequestParam(value = "previewPdf", required = false) MultipartFile previewPdf,
            @RequestParam(value = "status", required = false) String status
    ) throws IOException {
        try {
            Books book = new Books();

            book.setCategoryId(categoryId);
            book.setBookTitle(bookTitle);
            book.setAuthorName(authorName);
            book.setAboutAuthor(aboutAuthor);
            book.setPrice(price);
            book.setDescription(description);
            book.setPublisherName(publisherName);
            book.setLanguage(language);
            book.setPublicationYear(publicationYear);
            book.setStockQty(stockQty);
            book.setPages(pages);
            book.setDiscount(discount);
            book.setStatus(status);

            if (image != null && !image.isEmpty()) {
                String imageUrl = cloudinaryService.uploadImage(image);
                book.setImageUrl(imageUrl);
            }

            if (previewPdf != null && !previewPdf.isEmpty()) {
                String pdfUrl = cloudinaryService.uploadFile(previewPdf);
                book.setPreviewPdf(pdfUrl);
            }

            Books updatedBook = booksService.updateBook(bookId, book);
            return ResponseEntity.ok(updatedBook);
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    @DeleteMapping("/deletebook/{bookId}")
    public ResponseEntity<String> deleteBook(@PathVariable String bookId) {
        try {
            booksService.deleteBook(bookId);
            return ResponseEntity.ok("Book with ID " + bookId + " has been deleted.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    @GetMapping("/auth/getbookbyid/{bookId}")
    public ResponseEntity<?> getBookById(@PathVariable String bookId) {
        try {
            Books book = booksService.getBookById(bookId);
            return ResponseEntity.ok(book);
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    @GetMapping("/auth/getallbooks")
    public ResponseEntity<List<Books>> getAllBooks() {
        List<Books> books = booksService.getAllBooks();
        return ResponseEntity.ok(books);
    }

    @GetMapping("/auth/getbooksbycategory/{categoryId}")
    public ResponseEntity<List<Books>> getBooksByCategory(@PathVariable String categoryId) {
        List<Books> books = booksService.getBooksByCategoryId(categoryId);
        return ResponseEntity.ok(books);
    }
}
