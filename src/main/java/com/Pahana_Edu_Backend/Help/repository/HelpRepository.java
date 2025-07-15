package com.Pahana_Edu_Backend.Help.repository;

import com.Pahana_Edu_Backend.Help.entity.Help;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;

public interface HelpRepository extends MongoRepository<Help, String> {
    List<Help> findByEmail(String email);
}