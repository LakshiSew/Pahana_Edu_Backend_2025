package com.Pahana_Edu_Backend.BookSuggestions.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.Pahana_Edu_Backend.BookSuggestions.entity.Suggestions;
import com.Pahana_Edu_Backend.BookSuggestions.service.SuggestionsService;


@RestController
@CrossOrigin

public class SuggestionsController {

    @Autowired
    private SuggestionsService suggestionsService;

    // Create suggestion
    @PostMapping("/auth/createsuggestion")
    public ResponseEntity<?> addSuggestion(@RequestBody Suggestions suggestion) {
        try {
            Suggestions savedSuggestion = suggestionsService.addSuggestion(suggestion);
            return ResponseEntity.ok(savedSuggestion);
        } catch (Exception e) {
            return ResponseEntity.status(400).body("Failed to create suggestion: " + e.getMessage());
        }
    }

    // Get all suggestions
    @GetMapping("/getsuggestions")
    public ResponseEntity<List<Suggestions>> getAllSuggestions() {
        return ResponseEntity.ok(suggestionsService.getAllSuggestions());
    }

    // Mark suggestion as read
    @PutMapping("/marksuggestion/{suggestionId}")
    public ResponseEntity<?> markSuggestionAsRead(@PathVariable String suggestionId) {
        try {
            Suggestions updatedSuggestion = suggestionsService.markAsRead(suggestionId);
            return ResponseEntity.ok(updatedSuggestion);
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }
}