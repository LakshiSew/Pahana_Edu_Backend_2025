package com.Pahana_Edu_Backend.BookSuggestions.service;

import java.util.List;

import com.Pahana_Edu_Backend.BookSuggestions.entity.Suggestions;

public interface SuggestionsService {

    Suggestions addSuggestion(Suggestions suggestion);

    List<Suggestions> getAllSuggestions();

    Suggestions markAsRead(String suggestionId);
}