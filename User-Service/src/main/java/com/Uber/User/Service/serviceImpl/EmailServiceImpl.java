package com.Uber.User.Service.serviceImpl;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;

import java.util.Date;

@Service
public class EmailServiceImpl {
    private static final Logger logger = LoggerFactory.getLogger(EmailServiceImpl.class);

    @Autowired
    private JavaMailSender emailSender;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Retryable(
            value = {Exception.class},
            maxAttempts = 3,
            backoff = @Backoff(delay = 1000)
    )
    public void sendEmailNotification(String toEmail) throws MessagingException {
        try {
            String htmlContent=getSignupEmailTemplate(toEmail, String.valueOf(new Date()),"null");
            MimeMessage mimeMessage=emailSender.createMimeMessage();
            MimeMessageHelper helper=new MimeMessageHelper(mimeMessage,true);
            helper.setTo(toEmail);
            helper.setSubject("Welcome to CabVibe");
            helper.setText(htmlContent,true);

            logger.info("Attempting to send email to: {}", toEmail);
            emailSender.send(mimeMessage);
            logger.info("Email sent successfully to: {}", toEmail);

        } catch (Exception e) {
            logger.error("Failed to send email to: {}. Error: {}", toEmail, e.getMessage());
            throw e;
        }
    }

    private String getSignupEmailTemplate(String username, String signupDate, String activationLink) {
        // HTML email template for user signup confirmation with placeholders replaced by dynamic values
        return "<!DOCTYPE html>" +
                "<html lang=\"en\">" +
                "<head>" +
                "    <meta charset=\"UTF-8\">" +
                "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">" +
                "    <style>" +
                "        body {" +
                "            font-family: Arial, sans-serif;" +
                "            margin: 0;" +
                "            padding: 0;" +
                "            background-color: #f4f7fa;" +
                "        }" +
                "        .email-wrapper {" +
                "            width: 100%;" +
                "            max-width: 600px;" +
                "            margin: 0 auto;" +
                "            background-color: #ffffff;" +
                "            border-radius: 10px;" +
                "            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);" +
                "            overflow: hidden;" +
                "        }" +
                "        .email-header {" +
                "            background-color: #28a745;" +
                "            padding: 20px;" +
                "            text-align: center;" +
                "            color: white;" +
                "            font-size: 24px;" +
                "            font-weight: bold;" +
                "        }" +
                "        .email-content {" +
                "            padding: 30px;" +
                "        }" +
                "        .user-details {" +
                "            margin: 20px 0;" +
                "            padding: 15px;" +
                "            border: 1px solid #e0e0e0;" +
                "            border-radius: 5px;" +
                "            background-color: #fafafa;" +
                "        }" +
                "        .user-details p {" +
                "            margin: 5px 0;" +
                "        }" +
                "        .button {" +
                "            display: inline-block;" +
                "            padding: 12px 25px;" +
                "            background-color: #28a745;" +
                "            color: white;" +
                "            text-decoration: none;" +
                "            font-size: 16px;" +
                "            border-radius: 5px;" +
                "            margin-top: 20px;" +
                "        }" +
                "        .footer {" +
                "            text-align: center;" +
                "            padding: 20px;" +
                "            font-size: 12px;" +
                "            color: #777777;" +
                "        }" +
                "        @media only screen and (max-width: 600px) {" +
                "            .email-wrapper {" +
                "                width: 100%;" +
                "                padding: 15px;" +
                "            }" +
                "            .email-content {" +
                "                padding: 20px;" +
                "            }" +
                "            .user-details {" +
                "                padding: 12px;" +
                "            }" +
                "        }" +
                "    </style>" +
                "</head>" +
                "<body>" +
                "    <div class=\"email-wrapper\">" +
                "        <div class=\"email-header\">" +
                "            Welcome to Our Service!" +
                "        </div>" +
                "        <div class=\"email-content\">" +
                "            <p>Hello " + username + ",</p>" +
                "            <p>Thank you for signing up! We are excited to have you as part of our community. Here are the details of your account:</p>" +
                "            <div class=\"user-details\">" +
                "                <p><strong>Username:</strong> " + username + "</p>" +
                "                <p><strong>Signup Date:</strong> " + signupDate + "</p>" +
                "            </div>" +
                "            <p>To get started, please activate your account by clicking the button below:</p>" +
                "            <a href=\"" + activationLink + "\" class=\"button\">Activate Account</a>" +
                "            <p>If you didn't sign up for an account, please ignore this email or contact our support team.</p>" +
                "        </div>" +
                "        <div class=\"footer\">" +
                "            <p>Thank you for joining us!</p>" +
                "            <p>&copy; {{year}} Your Company. All Rights Reserved.</p>" +
                "        </div>" +
                "    </div>" +
                "</body>" +
                "</html>";
    }

}