package com.Pahana_Edu_Backend.BookSuggestions.repository;


import org.springframework.data.mongodb.repository.MongoRepository;

import com.Pahana_Edu_Backend.BookSuggestions.entity.Suggestions;

public interface SuggestionsRepository extends MongoRepository<Suggestions, String> {
}
