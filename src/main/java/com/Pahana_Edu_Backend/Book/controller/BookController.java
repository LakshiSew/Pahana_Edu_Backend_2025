package com.Pahana_Edu_Backend.Book.controller;

import java.util.HashMap;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import com.Pahana_Edu_Backend.Book.entity.Book;
import com.Pahana_Edu_Backend.Book.service.BookService;
import com.Pahana_Edu_Backend.Cloudinary.CloudinaryService;


import org.springframework.web.bind.annotation.*;
import java.util.Map;


import java.io.IOException;

@RestController
@CrossOrigin
public class BookController {
    private final CloudinaryService cloudinaryService;
    private final BookService bookService;

    public BookController(CloudinaryService cloudinaryService, 
                            BookService bookService) {
        this.cloudinaryService = cloudinaryService;
        this.bookService = bookService;
    }

@PostMapping("/auth/upload")
public ResponseEntity<?> uploadAndSaveFiles(
        @RequestPart("pdf") MultipartFile pdfFile,
        @RequestPart("image") MultipartFile imageFile,
        @RequestParam String title,
        @RequestParam String description,
        @RequestParam String category,
        @RequestParam Integer stockQty,
        @RequestParam Double price,
        @RequestParam String author,
        //  @RequestParam String publisherName,
        @RequestParam(required = false, defaultValue = "Active") String status // ✅ optional + default
) {
    // Check if files are empty
    if (pdfFile.isEmpty() || imageFile.isEmpty()) {
        return ResponseEntity.badRequest().body("Both PDF and image files must be provided");
    }

    // Validate file types
    if (!pdfFile.getContentType().equals("application/pdf")) {
        return ResponseEntity.badRequest().body("Only PDF files are allowed");
    }

    if (!imageFile.getContentType().startsWith("image/")) {
        return ResponseEntity.badRequest().body("Only image files are allowed");
    }

    // Validate status
    if (!status.equals("Active") && !status.equals("Inactive") && !status.equals("Out of Stock")) {
        return ResponseEntity.badRequest().body("Invalid status: must be 'Active', 'Inactive', or 'Out of Stock'");
    }

    try {
        String pdfUrl = cloudinaryService.uploadPdf(pdfFile);
        String imageUrl = cloudinaryService.uploadImage(imageFile);

        Book savedBooks = bookService.addBooks(
                pdfUrl, imageUrl, title, description,
                category, stockQty, price, author, status
        );

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("pdfUrl", pdfUrl);
        response.put("imageUrl", imageUrl);
        return ResponseEntity.ok(response);

    } catch (IOException e) {
        return ResponseEntity.internalServerError()
                .body(Map.of("success", false, "message", "File error: " + e.getMessage()));
    }
}



 @PutMapping("/auth/updatebooks/{id}")
    public ResponseEntity<?> updateBook(
            @PathVariable String id,
            @RequestPart(value = "pdf", required = false) MultipartFile pdfFile,
            @RequestPart(value = "image", required = false) MultipartFile imageFile,
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) Integer stockQty,
            @RequestParam(required = false) Double price,
            @RequestParam(required = false) String author,
            @RequestParam(required = false) String status) {
        try {
            String pdfUrl = null;
            String imageUrl = null;

            if (pdfFile != null && !pdfFile.isEmpty()) {
                if (!pdfFile.getContentType().equals("application/pdf")) {
                    return ResponseEntity.badRequest().body("Only PDF files are allowed");
                }
                pdfUrl = cloudinaryService.uploadPdf(pdfFile);
            }

            if (imageFile != null && !imageFile.isEmpty()) {
                if (!imageFile.getContentType().startsWith("image/")) {
                    return ResponseEntity.badRequest().body("Only image files are allowed");
                }
                imageUrl = cloudinaryService.uploadImage(imageFile);
            }

            Book updatedBook = bookService.updateBooks(id, pdfUrl, imageUrl, title, description,
                    category, stockQty, price, author, status);

            return ResponseEntity.ok(Map.of("success", true, "book", updatedBook));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", e.getMessage()));
        } catch (IOException e) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("success", false, "message", "File error: " + e.getMessage()));
        }
    }

    @DeleteMapping("/auth/deletebooks/{id}")
    public ResponseEntity<?> deleteBook(@PathVariable String id) {
        try {
            bookService.deleteBooks(id);
            return ResponseEntity.ok(Map.of("success", true, "message", "Book deleted successfully"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", e.getMessage()));
        }
    }

    @GetMapping("/auth/getbooksbyid/{id}")
    public ResponseEntity<?> getDocument(@PathVariable String id) {
        Book document = bookService.getbooksById(id);
        if (document == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(document);
    }


@GetMapping("/auth/allBooks")
public ResponseEntity<List<Book>> getAllBooks() {
    List<Book> books = bookService.getAllBooks();
    return ResponseEntity.ok(books);
}

    @GetMapping("/getcategorybyid/{categoryId}")
    public ResponseEntity<?> getBooksByCategoryId(@PathVariable String categoryId) {
        List<Book> books = bookService.getBooksByCategoryId(categoryId);
        if (books.isEmpty()) {
            return ResponseEntity.ok().body(Map.of("success", false, "message", "No books found for category ID: " + categoryId));
        }
        return ResponseEntity.ok(books);
    }
}
