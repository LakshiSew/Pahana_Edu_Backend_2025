package com.Pahana_Edu_Backend.Book.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import com.Pahana_Edu_Backend.Book.entity.Book;

@Repository
public interface BookRepository extends MongoRepository<Book, String> {
    // You can add custom methods if needed, e.g., for querying by categoryId
    List<Book> findByCategoryId(String categoryId);
}
