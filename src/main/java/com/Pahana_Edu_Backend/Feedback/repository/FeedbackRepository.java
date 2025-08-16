package com.Pahana_Edu_Backend.Feedback.repository;

import com.Pahana_Edu_Backend.Feedback.entity.Feedback;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface FeedbackRepository extends MongoRepository<Feedback, String> {
   
}
