package com.Pahana_Edu_Backend.BookSuggestions.serviceImpl;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.Pahana_Edu_Backend.BookSuggestions.entity.Suggestions;
import com.Pahana_Edu_Backend.BookSuggestions.repository.SuggestionsRepository;
import com.Pahana_Edu_Backend.BookSuggestions.service.SuggestionsService;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

@Service
public class SuggestionsServiceImpl implements SuggestionsService {

    @Autowired
    private SuggestionsRepository suggestionsRepository;

    @Autowired
    private JavaMailSender mailSender;

    @Override
    public Suggestions addSuggestion(Suggestions suggestion) {
        suggestion.setStatus("PENDING");
        return suggestionsRepository.save(suggestion);
    }

    @Override
    public List<Suggestions> getAllSuggestions() {
        return suggestionsRepository.findAll();
    }

    @Override
    public Suggestions markAsRead(String suggestionId) {
        Optional<Suggestions> existingSuggestion = suggestionsRepository.findById(suggestionId);

        if (existingSuggestion.isPresent()) {
            Suggestions suggestion = existingSuggestion.get();
            suggestion.setStatus("MARK_AS_READ");
            Suggestions updatedSuggestion = suggestionsRepository.save(suggestion);

            // Send confirmation email
            sendConfirmationEmail(suggestion.getEmail(), suggestion.getName(), suggestion.getBookTitle());
            return updatedSuggestion;
        } else {
            throw new RuntimeException("Suggestion not found with id: " + suggestionId);
        }
    }

    private void sendConfirmationEmail(String email, String name, String bookTitle) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(email);
            helper.setSubject("Thank You for Your Book Suggestion!");
            helper.setFrom("no-reply@pahanaedu.com");

            String htmlContent = """
                <!DOCTYPE html>
                <html lang="en">
                <head>
                    <meta charset="UTF-8">
                    <meta name="viewport" content="width=device-width, initial-scale=1.0">
                    <style>
                        body {
                            margin: 0;
                            padding: 0;
                            font-family: 'Arial', sans-serif;
                            background-color: #f4f4f4;
                        }
                        .container {
                            max-width: 600px;
                            margin: 20px auto;
                            background-color: #ffffff;
                            border-radius: 8px;
                            overflow: hidden;
                            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
                        }
                        .header {
                            background: linear-gradient(90deg, #FBBF24, #D97706);
                            padding: 20px;
                            text-align: center;
                        }
                        .header img {
                            max-width: 150px;
                            height: auto;
                        }
                        .content {
                            padding: 30px;
                            color: #333333;
                            line-height: 1.6;
                        }
                        .content h1 {
                            font-size: 24px;
                            color: #1F2937;
                            margin-bottom: 20px;
                        }
                        .content p {
                            font-size: 16px;
                            margin-bottom: 20px;
                        }
                        .button {
                            display: inline-block;
                            padding: 12px 24px;
                            background-color: #FBBF24;
                            color: #000000;
                            text-decoration: none;
                            border-radius: 5px;
                            font-weight: bold;
                            font-size: 16px;
                        }
                        .footer {
                            background-color: #1F2937;
                            color: #ffffff;
                            padding: 20px;
                            text-align: center;
                            font-size: 14px;
                        }
                        .footer a {
                            color: #FBBF24;
                            text-decoration: none;
                        }
                        @media only screen and (max-width: 600px) {
                            .container {
                                margin: 10px;
                            }
                            .header img {
                                max-width: 120px;
                            }
                            .content h1 {
                                font-size: 20px;
                            }
                            .content p {
                                font-size: 14px;
                            }
                            .button {
                                padding: 10px 20px;
                                font-size: 14px;
                            }
                        }
                    </style>
                </head>
                <body>
                    <div class="container">
                        <div class="header">
                            <h2>Pahana Edu</h2>
                        </div>
                        <div class="content">
                            <h1>Thank You, %s!</h1>
                            <p>We have received your suggestion for the book "<strong>%s</strong>." Our team will consider your suggestion, and you can expect to find the book on our site soon.</p>
                            <p>Thank you for helping us improve our platform!</p>
                        </div>
                        <div class="footer">
                            <p>Need help? <a href="mailto:support@pahanaedu.com">Contact Support</a></p>
                            <p>© 2025 Pahana Edu. All rights reserved.</p>
                        </div>
                    </div>
                </body>
                </html>
                """.formatted(name, bookTitle);

            helper.setText(htmlContent, true);
            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send confirmation email: " + e.getMessage());
        }
    }
}