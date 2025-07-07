package com.Pahana_Edu_Backend.Book.repository;


import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.Pahana_Edu_Backend.Book.entity.Book;

public interface BookRepository extends MongoRepository<Book, String> {

        List<Book> findByCategory(String category);

}
