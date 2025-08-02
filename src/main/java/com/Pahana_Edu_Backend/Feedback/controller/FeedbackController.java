package com.Pahana_Edu_Backend.Feedback.controller;

import com.Pahana_Edu_Backend.Feedback.entity.Feedback;
import com.Pahana_Edu_Backend.Feedback.service.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import java.util.Optional;
import java.util.Base64;

@RestController
@CrossOrigin
public class FeedbackController {

    @Autowired
    private FeedbackService feedbackService;

    @GetMapping("/auth/getallfeedback")
    public List<Feedback> getAllFeedback() {
        return feedbackService.getAllFeedback();
    }

    @GetMapping("/getfeedback/{id}")
    public ResponseEntity<?> getFeedbackById(@PathVariable String id) {
        Optional<Feedback> feedback = feedbackService.getFeedbackById(id);
        return feedback.isPresent()
                ? ResponseEntity.ok(feedback.get())
                : ResponseEntity.status(404).body("Feedback not found");
    }

   
    @PostMapping("/auth/createfeedback")
    public ResponseEntity<?> createFeedback(
            @RequestParam("quote") String quote,
            @RequestParam("name") String name,
            @RequestParam("email") String email,
            @RequestParam("rating") int rating,
            @RequestParam(value = "image", required = false) MultipartFile image) {
        if (rating < 1 || rating > 5) {
            return ResponseEntity.badRequest().body("Rating must be between 1 and 5");
        }

        if (quote == null || quote.trim().isEmpty() || name == null || name.trim().isEmpty() || email == null || email.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Quote, name, and email are required");
        }

        Feedback feedback = new Feedback();
        feedback.setQuote(quote);
        feedback.setName(name);
        feedback.setEmail(email);
        feedback.setRating(rating);

        if (image != null && !image.isEmpty()) {
            try {
                String base64Image = Base64.getEncoder().encodeToString(image.getBytes());
                feedback.setImage("data:image/" + image.getOriginalFilename().substring(image.getOriginalFilename().lastIndexOf(".") + 1) + ";base64," + base64Image);
            } catch (Exception e) {
                return ResponseEntity.badRequest().body("Failed to process image");
            }
        }

        Feedback createdFeedback = feedbackService.createFeedback(feedback);
        return ResponseEntity.ok(createdFeedback);
    }

    // Delete Feedback by ID
    @DeleteMapping("/deletefeedback/{id}")
    public ResponseEntity<?> deleteFeedback(@PathVariable String id) {
        feedbackService.deleteFeedback(id);
        return ResponseEntity.ok("Feedback deleted successfully");
    }
}