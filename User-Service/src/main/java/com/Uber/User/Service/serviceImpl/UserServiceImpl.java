package com.Uber.User.Service.serviceImpl;

import com.Uber.User.Service.model.AuthUser;
import com.Uber.User.Service.model.User;
import com.Uber.User.Service.repository.UserRepo;
import com.Uber.User.Service.service.UserService;
import com.Uber.User.Service.util.JwtUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepo userRepository;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtils jwtUtils;

    private static final String TOPIC = "user-signup-topic";
    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);
    private static final int KAFKA_TIMEOUT_SECONDS = 10;

    @Override
    public User addUser(User user) {
        if (isEmailAndPhoneExist(user.getEmail(), user.getPhone())) {
            throw new IllegalArgumentException("Email or phone number already exist.");
        }

       user.setPassword(passwordEncoder.encode(user.getPassword()));
        log.info("Password Encoding has been done");

        if (user.isEmailProcessed()) {
            log.info("Email for user {} has already been processed.", user.getEmail());
            return user;
        }

        User savedUser = userRepository.save(user);
        log.info("User saved to database: {}", user.getEmail());

        // 2. Send to Kafka for email processing
        try {
            SendResult<String, String> result = sendUserToKafka(user)
                    .get(KAFKA_TIMEOUT_SECONDS, TimeUnit.SECONDS);
            verifyKafkaInsertion(result, user);
            // Update the user to mark the email as processed
            savedUser.setEmailProcessed(true);
            userRepository.save(savedUser);

        } catch (TimeoutException te) {
            log.error("Kafka insertion timed out after {} seconds for user: {}",
                    KAFKA_TIMEOUT_SECONDS, user.getEmail());
        } catch (Exception e) {
            log.error("Error during Kafka verification for user: {}",
                    user.getEmail(), e);
        }

        return savedUser;
    }

  /*  @Override
    public List<User> addUser(List<User> users) {
        List<User> savedUsers = new ArrayList<>();

        for (User user : users) {
            // Check if email or phone already exists for each user
            if (isEmailAndPhoneExist(user.getEmail(), user.getPhone())) {
                throw new IllegalArgumentException("Email or phone number already exists for user: " + user.getEmail());
            }

            user.setPassword(passwordEncoder.encode());
            // Save the user in the DB
            User savedUser = userRepository.save(user);
            log.info("User saved to database: {}", user.getEmail());

            // Send to Kafka for email processing (you can choose to send this in bulk if your Kafka setup supports it)
            try {
                SendResult<String, String> result = sendUserToKafka(user)
                        .get(KAFKA_TIMEOUT_SECONDS, TimeUnit.SECONDS);
                verifyKafkaInsertion(result, user);
            } catch (TimeoutException te) {
                log.error("Kafka insertion timed out after {} seconds for user: {}", KAFKA_TIMEOUT_SECONDS, user.getEmail());
            } catch (Exception e) {
                log.error("Error during Kafka verification for user: {}", user.getEmail(), e);
            }

            savedUsers.add(savedUser);
        }

        return savedUsers;
    }
*/
    @Override
    public String login(AuthUser authUser) {
        User found=userRepository.findByEmail(authUser.getEmail());
        if (found!=null && passwordEncoder.matches(authUser.getPassword(), found.getPassword()))
        {
            return jwtUtils.generateToken(authUser.getEmail());
        }
        return "Invalid Credentials";
    }


    @Async
    public CompletableFuture<SendResult<String, String>> sendUserToKafka(User user) {
        String message = user.getEmail();
        log.info("Attempting to send user data to Kafka for email: {}", user.getEmail());

        return kafkaTemplate.send(TOPIC, user.getEmail(), message)
                .thenApply(result -> {
                    log.info("User data successfully sent to Kafka: Topic={}, Partition={}, Offset={}, Email={}",
                            TOPIC,
                            result.getRecordMetadata().partition(),
                            result.getRecordMetadata().offset(),
                            user.getEmail());
                    return result;
                })
                .exceptionally(ex -> {
                    log.error("Failed to send user data to Kafka: Email={}, Error={}",
                            user.getEmail(),
                            ex.getMessage());
                    throw new CompletionException(ex);
                });
    }

    /**
     * Verifies the Kafka insertion by checking the metadata
     *
     * @param result The SendResult from Kafka
     * @param user   The user being inserted
     */
    private void verifyKafkaInsertion(SendResult<String, String> result, User user) {
        if (result != null && result.getRecordMetadata() != null) {
            log.info("Kafka insertion verified: " +
                            "Topic={}, Partition={}, Offset={}, Email={}, " +
                            "Timestamp={}",
                    TOPIC,
                    result.getRecordMetadata().partition(),
                    result.getRecordMetadata().offset(),
                    user.getEmail(),
                    result.getRecordMetadata().timestamp());
        } else {
            log.warn("Kafka insertion result was null or incomplete for user: {}",
                    user.getEmail());
        }
    }

    /**
     * Check if the email or phone number already exists in the database.
     *
     * @param email User's email.
     * @param phone User's phone number.
     * @return True if either email or phone number exists, false otherwise.
     */
    @Override
    public boolean isEmailAndPhoneExist(String email, String phone) {
        User userExample = new User();
        userExample.setEmail(email);
        userExample.setPhone(phone);

        ExampleMatcher matcher = ExampleMatcher.matching()
                .withIgnoreCase() // Ignore case sensitivity for matching
                .withIgnorePaths("id", "name", "password");  // Ignore other fields like id, name, password

        Example<User> example = Example.of(userExample, matcher);

        return userRepository.exists(example);  // Returns true if any user exists with the provided email or phone
    }

    /**
     * Finds a user by their ID.
     *
     * @param id User ID.
     * @return Optional containing the user if found.
     */
    @Override
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    /**
     * Finds a user by their email.
     *
     * @param email User email.
     * @return Optional containing the user if found.
     */
    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
