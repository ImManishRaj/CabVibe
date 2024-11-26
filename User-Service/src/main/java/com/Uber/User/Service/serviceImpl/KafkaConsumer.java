package com.Uber.User.Service.serviceImpl;

import jakarta.mail.MessagingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumer {

    private final EmailServiceImpl emailService;
    private static final Logger logger = LoggerFactory.getLogger(KafkaConsumer.class);

    @Autowired
    public KafkaConsumer(EmailServiceImpl emailService) {
        this.emailService = emailService;
    }

    @KafkaListener(
            topics = "user-signup-topic",
            groupId = "user-service-group",
            containerFactory = "kafkaListenerContainerFactory"
    )
    public void consumeAndSendEmail(String email) throws MessagingException {
        logger.info("Received email address from Kafka: {}", email);
        try {
            emailService.sendEmailNotification(email);
            logger.info("Welcome email sent successfully to: {}", email);
        } catch (Exception e) {
            logger.error("Failed to send welcome email to: {}. Error: {}", email, e.getMessage(), e);
            throw e; // The error handler in KafkaConsumerConfig will handle retries
        }
    }

}
