
package com.Pahana_Edu_Backend.Book.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.Pahana_Edu_Backend.Book.entity.Book;
import java.io.InputStream; 
import com.Pahana_Edu_Backend.Book.service.BookService;
import com.Pahana_Edu_Backend.Cloudinary.CloudinaryService;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/auth")
public class BookController {

    @Autowired
    private BookService bookService;

    @Autowired
    private CloudinaryService cloudinaryService;

    @PostMapping("/addbook")
    public ResponseEntity<?> addBook(
            @RequestParam("title") String title,
            @RequestParam("categoryId") String categoryId,
            @RequestParam("author") String author,
            @RequestParam("aboutAuthor") String aboutAuthor,
            @RequestParam("price") Double price,
            @RequestParam("stockQty") Integer stockQty,
            @RequestParam(value = "image", required = false) MultipartFile image,
            @RequestParam(value = "pdf", required = false) MultipartFile pdf,
            @RequestParam(value = "discount", required = false) Double discount,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "status", required = false) String status) throws IOException {
        try {
            Book book = new Book();
            book.setTitle(title);
            book.setCategoryId(categoryId);
            book.setAuthor(author);
            book.setAboutAuthor(aboutAuthor);
            book.setPrice(price);
            book.setStockQty(stockQty);
            book.setDiscount(discount);
            book.setDescription(description);
            book.setStatus(status != null && !status.isEmpty() ? status : "Active");

            if (image != null && !image.isEmpty()) {
                String imageUrl = cloudinaryService.uploadImage(image);
                book.setImage(imageUrl);
            }
            if (pdf != null && !pdf.isEmpty()) {
                String pdfUrl = cloudinaryService.uploadPdf(pdf);
                book.setPdf(pdfUrl);
            }

            Book savedBook = bookService.addBook(book);
            return ResponseEntity.ok(savedBook);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (IOException e) {
            return ResponseEntity.status(500).body("File upload failed: " + e.getMessage());
        }
    }

    @PutMapping("/updatebook/{id}")
    public ResponseEntity<?> updateBook(
            @PathVariable("id") String id,
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "categoryId", required = false) String categoryId,
            @RequestParam(value = "author", required = false) String author,
            @RequestParam(value = "aboutAuthor", required = false) String aboutAuthor,
            @RequestParam(value = "price", required = false) Double price,
            @RequestParam(value = "stockQty", required = false) Integer stockQty,
            @RequestParam(value = "image", required = false) MultipartFile image,
            @RequestParam(value = "pdf", required = false) MultipartFile pdf,
            @RequestParam(value = "discount", required = false) Double discount,
            @RequestParam(value = "description", required = false) String description,
            @RequestParam(value = "status", required = false) String status) throws IOException {
        try {
            Book book = new Book();
            book.setTitle(title);
            book.setCategoryId(categoryId);
            book.setAuthor(author);
            book.setAboutAuthor(aboutAuthor);
            book.setPrice(price);
            book.setStockQty(stockQty);
            book.setDiscount(discount);
            book.setDescription(description);
            book.setStatus(status);
            if (image != null && !image.isEmpty()) {
                String imageUrl = cloudinaryService.uploadImage(image);
                book.setImage(imageUrl);
            }
            if (pdf != null && !pdf.isEmpty()) {
                String pdfUrl = cloudinaryService.uploadPdf(pdf);
                book.setPdf(pdfUrl);
            }

            Book updatedBook = bookService.updateBook(id, book);
            return ResponseEntity.ok(updatedBook);
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        } catch (IOException e) {
            return ResponseEntity.status(500).body("File upload failed: " + e.getMessage());
        }
    }

    @DeleteMapping("/deletebook/{id}")
    public ResponseEntity<String> deleteBook(@PathVariable("id") String id) {
        try {
            bookService.deleteBook(id);
            return ResponseEntity.ok("Book with ID " + id + " has been deleted.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    @GetMapping("/getbookbyid/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable("id") String id) {
        try {
            Book book = bookService.getBookById(id);
            return ResponseEntity.ok(book);
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(null);
        }
    }

    @GetMapping("/getallbooks")
    public ResponseEntity<List<Book>> getAllBooks() {
        return ResponseEntity.ok(bookService.getAllBooks());
    }

    @GetMapping("/getbooksbycategoryid/{categoryId}")
    public ResponseEntity<?> getBooksByCategoryId(@PathVariable("categoryId") String categoryId) {
        try {
            List<Book> books = bookService.getBooksByCategoryId(categoryId);
            return ResponseEntity.ok(books);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/getpdfbybookid/{bookId}")
    public ResponseEntity<Resource> getPdfByBookId(@PathVariable("bookId") String bookId) {
        try {
            // Fetch book by ID
            Book book = bookService.getBookById(bookId);
            if (book == null || book.getPdf() == null || book.getPdf().isEmpty()) {
                return ResponseEntity.status(404).body(null);
            }

            // Fetch PDF from Cloudinary URL
            URL pdfUrl = new URL(book.getPdf());
            HttpURLConnection connection = (HttpURLConnection) pdfUrl.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();

            if (connection.getResponseCode() != 200) {
                return ResponseEntity.status(404).body(null);
            }

            InputStream inputStream = connection.getInputStream();
            InputStreamResource resource = new InputStreamResource(inputStream);

            // Set headers for PDF response
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=book-" + bookId + ".pdf");
            headers.add(HttpHeaders.CACHE_CONTROL, "no-cache, no-store, must-revalidate");
            headers.add(HttpHeaders.PRAGMA, "no-cache");
            headers.add(HttpHeaders.EXPIRES, "0");

            return ResponseEntity.ok()
                    .headers(headers)
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(resource);
        } catch (Exception e) {
            return ResponseEntity.status(500).body(null);
        }
    }
}
