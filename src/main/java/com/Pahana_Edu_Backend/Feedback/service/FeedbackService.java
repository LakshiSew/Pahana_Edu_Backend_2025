package com.Pahana_Edu_Backend.Feedback.service;

import com.Pahana_Edu_Backend.Feedback.entity.Feedback;
import java.util.List;
import java.util.Optional;

public interface FeedbackService {
    List<Feedback> getAllFeedback();
    Optional<Feedback> getFeedbackById(String id);
    Feedback createFeedback(Feedback feedback);
    void deleteFeedback(String id);
}
