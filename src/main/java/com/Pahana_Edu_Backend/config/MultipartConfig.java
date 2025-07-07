package com.Pahana_Edu_Backend.config;

import jakarta.servlet.MultipartConfigElement;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.unit.DataSize;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Configuration
public class MultipartConfig {

    private static final Logger logger = LoggerFactory.getLogger(MultipartConfig.class);

    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        factory.setMaxFileSize(DataSize.ofMegabytes(50)); // Max size per file
        factory.setMaxRequestSize(DataSize.ofMegabytes(100)); // Max total request size
        logger.info("Applying MultipartConfig: maxFileSize=50MB, maxRequestSize=100MB");
        return factory.createMultipartConfig();
    }
}