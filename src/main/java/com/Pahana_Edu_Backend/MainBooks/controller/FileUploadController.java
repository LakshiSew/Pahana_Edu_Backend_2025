package com.Pahana_Edu_Backend.MainBooks.controller;

import com.Pahana_Edu_Backend.Cloudinary.CloudinaryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController

public class FileUploadController {

    private final CloudinaryService cloudinaryService;

    public FileUploadController(CloudinaryService cloudinaryService) {
        this.cloudinaryService = cloudinaryService;
    }

    @PostMapping("/auth/files")
    public ResponseEntity<?> uploadFiles(@RequestParam("files") MultipartFile[] files) {
        Map<String, String> response = new HashMap<>();
        try {
            for (MultipartFile file : files) {
                String contentType = file.getContentType();
                if (contentType == null) {
                    return ResponseEntity.badRequest().body("File type is not recognized");
                }

                if (contentType.equals("application/pdf")) {
                    String pdfUrl = cloudinaryService.uploadPdf(file);
                    response.put("pdfUrl", pdfUrl);
                } else if (contentType.startsWith("image/")) {
                    String imageUrl = cloudinaryService.uploadImage(file);
                    response.put("imageUrl", imageUrl);
                } else {
                    return ResponseEntity.badRequest().body("Only PDF and image files are allowed");
                }
            }
            return ResponseEntity.ok(Map.of("success", true, "urls", response));
        } catch (IOException e) {
            return ResponseEntity.internalServerError()
                .body(Map.of("success", false, "message", "Failed to upload files: " + e.getMessage()));
        }
    }
}
