package com.Pahana_Edu_Backend.Feedback.serviceImpl;

import com.Pahana_Edu_Backend.Feedback.entity.Feedback;
import com.Pahana_Edu_Backend.Feedback.repository.FeedbackRepository;
import com.Pahana_Edu_Backend.Feedback.service.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FeedbackServiceImpl implements FeedbackService {

    @Autowired
    private FeedbackRepository feedbackRepository;

    @Override
    public List<Feedback> getAllFeedback() {
        return feedbackRepository.findAll();
    }

    @Override
    public Optional<Feedback> getFeedbackById(String id) {
        return feedbackRepository.findById(id);
    }

    @Override
    public Feedback createFeedback(Feedback feedback) {
        return feedbackRepository.save(feedback);
    }

    @Override
    public void deleteFeedback(String id) {
        feedbackRepository.deleteById(id);
    }
}
