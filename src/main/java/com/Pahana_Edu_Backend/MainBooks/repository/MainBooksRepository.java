package com.Pahana_Edu_Backend.MainBooks.repository;



import com.Pahana_Edu_Backend.MainBooks.entity.MainBooks;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MainBooksRepository extends MongoRepository<MainBooks, String> {
    List<MainBooks> findByCategoryId(String categoryId);
}
