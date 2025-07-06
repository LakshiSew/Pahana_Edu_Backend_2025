package com.Pahana_Edu_Backend.Book.repository;


import org.springframework.data.mongodb.repository.MongoRepository;

import com.Pahana_Edu_Backend.Book.entity.Book;

public interface BookRepository extends MongoRepository<Book, String> {
}
