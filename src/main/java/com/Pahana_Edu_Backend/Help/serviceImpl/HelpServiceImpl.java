package com.Pahana_Edu_Backend.Help.serviceImpl;

import com.Pahana_Edu_Backend.Help.entity.Help;
import com.Pahana_Edu_Backend.Help.repository.HelpRepository;
import com.Pahana_Edu_Backend.Help.service.HelpService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class HelpServiceImpl implements HelpService {

    @Autowired
    private HelpRepository helpRepository;

    @Autowired
    private JavaMailSender mailSender;

    @Override
    public Help submitHelpMessage(Help help) {
        help.setId(null); // Ensure new ID is generated
        help.setSubmittedAt(LocalDateTime.now());
        help.setReply(null);
        help.setRepliedAt(null);
        return helpRepository.save(help);
    }

    @Override
    public Help replyToHelpMessage(String id, String reply) {
        Optional<Help> optionalHelp = helpRepository.findById(id);
        if (optionalHelp.isEmpty()) {
            throw new RuntimeException("Help message not found");
        }

        Help help = optionalHelp.get();
        help.setReply(reply);
        help.setRepliedAt(LocalDateTime.now());

        // Send email to user
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(help.getEmail());
            helper.setSubject("Pahana Edu: Response to Your Support Request");
            helper.setText(
                    "<!DOCTYPE html>" +
                            "<html>" +
                            "<head>" +
                            "<style>" +
                            "body { font-family: 'Arial', sans-serif; color: #333; background-color: #f4f4f4; margin: 0; padding: 0; }"
                            +
                            ".container { max-width: 600px; margin: 20px auto; background: #fff; border-radius: 8px; box-shadow: 0 2px 4px rgba(0,0,0,0.1); }"
                            +
                            ".header { background: linear-gradient(to right, #FBBF24, #F59E0B); padding: 20px; text-align: center; border-top-left-radius: 8px; border-top-right-radius: 8px; }"
                            +
                            ".header h1 { margin: 0; color: #fff; font-size: 24px; }" +
                            ".content { padding: 20px; }" +
                            ".content p { font-size: 16px; line-height: 1.6; }" +
                            ".message-box { background: #f9f9f9; padding: 15px; border-left: 4px solid #FBBF24; margin: 10px 0; }"
                            +
                            ".reply-box { background: #f0f4f8; padding: 15px; border-left: 4px solid #10B981; margin: 10px 0; }"
                            +
                            ".footer { text-align: center; padding: 15px; font-size: 14px; color: #666; border-top: 1px solid #eee; }"
                            +
                            ".button { display: inline-block; padding: 10px 20px; margin: 10px 0; background: #FBBF24; color: #fff; text-decoration: none; border-radius: 5px; }"
                            +
                            "</style>" +
                            "</head>" +
                            "<body>" +
                            "<div class='container'>" +
                            "<div class='header'>" +
                            "<h1>Pahana Edu Support</h1>" +
                            "</div>" +
                            "<div class='content'>" +
                            "<p>Dear " + help.getFullName() + ",</p>" +
                            "<p>Thank you for reaching out to us. Below is our response to your support request.</p>" +
                            "<div class='message-box'>" +
                            "<p><strong>Your Message:</strong> " + help.getMessage() + "</p>" +
                            "</div>" +
                            "<div class='reply-box'>" +
                            "<p><strong>Our Response:</strong> " + reply + "</p>" +
                            "</div>" +
                            "<p>We hope this resolves your query. If you need further assistance, please don't hesitate to contact us.</p>"
                            +
                            "<a href='mailto:support@mybookshop.lk' class='button'>Contact Us Again</a>" +
                            "</div>" +
                            "<div class='footer'>" +
                            "<p>Best regards,<br>Pahana Edu Team</p>" +
                            "<p>123 Book Street, Nugegoda, Sri Lanka | support@mybookshop.lk | +94 77 123 4567</p>" +
                            "</div>" +
                            "</div>" +
                            "</body>" +
                            "</html>",
                    true);
            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send email: " + e.getMessage());
        }

        return helpRepository.save(help);
    }

    @Override
    public List<Help> getAllHelpMessages() {
        return helpRepository.findAll();
    }

    @Override
    public Help getHelpMessageById(String id) {
        return helpRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Help message not found"));
    }

    @Override
    public List<Help> getHelpMessagesByEmail(String email) {
        return helpRepository.findByEmail(email);
    }
}