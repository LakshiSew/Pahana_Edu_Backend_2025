package com.Pahana_Edu_Backend.Book.repository;

import com.Pahana_Edu_Backend.Book.entity.Books;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BooksRepository extends MongoRepository<Books, String> {

    // Find books by categoryId
    List<Books> findByCategoryId(String categoryId);
}