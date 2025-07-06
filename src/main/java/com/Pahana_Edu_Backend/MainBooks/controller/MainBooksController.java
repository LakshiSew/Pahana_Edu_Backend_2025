package com.Pahana_Edu_Backend.MainBooks.controller;

import com.Pahana_Edu_Backend.MainBooks.entity.MainBooks;
import com.Pahana_Edu_Backend.MainBooks.service.MainBookService;
import com.Pahana_Edu_Backend.Cloudinary.CloudinaryService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@CrossOrigin
// @RequestMapping("/auth")
public class MainBooksController {

    private final MainBookService mainBookService;
    private final CloudinaryService cloudinaryService;

    public MainBooksController(MainBookService mainBookService, CloudinaryService cloudinaryService) {
        this.mainBookService = mainBookService;
        this.cloudinaryService = cloudinaryService;
    }

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
            @RequestParam("stockQty") Integer stockQty,
            @RequestParam(value = "pages", required = false) Integer pages,
            @RequestParam(value = "discount", required = false) Double discount,
            @RequestParam(value = "status", required = false) String status
    ) {
        try {
            String imageUrl = null;
            if (image != null && !image.isEmpty()) {
                imageUrl = cloudinaryService.uploadImage(image);
            }

            MainBooks savedBook = mainBookService.addBook(
                    categoryId,
                    bookTitle,
                    authorName,
                    aboutAuthor,
                    imageUrl,
                    price,
                    description,
                    publisherName,
                    language,
                    publicationYear,
                    stockQty,
                    pages,
                    discount,
                    status != null && !status.isEmpty() ? status : "Active"
            );

            return ResponseEntity.ok(savedBook);

        } catch (IOException e) {
            return ResponseEntity.internalServerError()
                    .body("Error uploading image or saving book: " + e.getMessage());
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
            @RequestParam(value = "status", required = false) String status
    ) {
        try {
            String imageUrl = null;
            if (image != null && !image.isEmpty()) {
                imageUrl = cloudinaryService.uploadImage(image);
            }

            MainBooks updatedBook = mainBookService.updateBook(
                    bookId,
                    categoryId,
                    bookTitle,
                    authorName,
                    aboutAuthor,
                    imageUrl,
                    price,
                    description,
                    publisherName,
                    language,
                    publicationYear,
                    stockQty,
                    pages,
                    discount,
                    status
            );

            if (updatedBook == null) {
                return ResponseEntity.notFound().build();
            }

            return ResponseEntity.ok(updatedBook);

        } catch (IOException e) {
            return ResponseEntity.internalServerError()
                    .body("Error uploading image or updating book: " + e.getMessage());
        }
    }

    @DeleteMapping("/deletebook/{bookId}")
    public ResponseEntity<?> deleteBook(@PathVariable String bookId) {
        mainBookService.deleteBook(bookId);
        return ResponseEntity.ok("Book with ID " + bookId + " has been deleted.");
    }

    @GetMapping("/getbookbyid/{bookId}")
    public ResponseEntity<?> getBookById(@PathVariable String bookId) {
        MainBooks book = mainBookService.getBookById(bookId);
        if (book == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(book);
    }

    @GetMapping("/getallbooks")
    public ResponseEntity<List<MainBooks>> getAllBooks() {
        return ResponseEntity.ok(mainBookService.getAllBooks());
    }

    @GetMapping("/getbooksbycategoryid/{categoryId}")
    public ResponseEntity<List<MainBooks>> getBooksByCategoryId(@PathVariable String categoryId) {
        return ResponseEntity.ok(mainBookService.getBooksByCategoryId(categoryId));
    }
}
