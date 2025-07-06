package com.Pahana_Edu_Backend.Book.controller;

import java.util.HashMap;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import com.Pahana_Edu_Backend.Book.entity.Book;
import com.Pahana_Edu_Backend.Book.service.BookService;
import com.Pahana_Edu_Backend.Cloudinary.CloudinaryService;


import org.springframework.web.bind.annotation.*;
import java.util.Map;


import java.io.IOException;




@RestController
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
            @RequestParam("pdf") MultipartFile pdfFile,
            @RequestParam("image") MultipartFile imageFile,
             @RequestParam String title,
        @RequestParam String description,
        @RequestParam String category,
         @RequestParam Integer stockQty,
        @RequestParam Double price,
        @RequestParam String author) {
        
        try {
            // Validate files
            if (!pdfFile.getContentType().equals("application/pdf")) {
                return ResponseEntity.badRequest().body("Only PDF files are allowed");
            }
            if (!imageFile.getContentType().startsWith("image/")) {
                return ResponseEntity.badRequest().body("Only image files are allowed");
            }

            // Upload files to Cloudinary
            String pdfUrl = cloudinaryService.uploadPdf(pdfFile);
            String imageUrl = cloudinaryService.uploadImage(imageFile);

            // Save URLs to MongoDB
            Book savedBooks = bookService.addBooks(pdfUrl, imageUrl, title,
            description,
            category,
            stockQty,
            price,
            author);

            // Return response
            java.util.Map<String, Object> response = new HashMap<>();
            response.put("success", true);

            response.put("pdfUrl", pdfUrl);
            response.put("imageUrl", imageUrl);

            return ResponseEntity.ok(response);

        } catch (IOException e) {
            return ResponseEntity.internalServerError()
                    .body(Map.of(
                            "success", false,
                            "message", "Failed to process files: " + e.getMessage()
                    ));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getDocument(@PathVariable String id) {
        Book document = bookService.getbooksById(id);
        if (document == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(document);
    }
}
