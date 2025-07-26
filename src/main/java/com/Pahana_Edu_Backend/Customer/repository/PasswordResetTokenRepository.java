package com.Pahana_Edu_Backend.Customer.repository;

import com.Pahana_Edu_Backend.Customer.entity.PasswordResetToken;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface PasswordResetTokenRepository extends MongoRepository<PasswordResetToken, String> {
    Optional<PasswordResetToken> findByToken(String token);
    void deleteByUserName(String userName);
}