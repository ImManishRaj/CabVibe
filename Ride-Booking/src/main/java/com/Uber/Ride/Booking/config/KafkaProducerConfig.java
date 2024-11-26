package com.Uber.Ride.Booking.config;


import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.ProducerFactory;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaProducerConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${spring.kafka.properties.sasl.jaas.config}")
    private String jaasConfig;

    @Bean
    public ProducerFactory<String, String> producerFactory() {
        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        configProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configProps.put(ProducerConfig.RETRIES_CONFIG, 3);
        configProps.put(ProducerConfig.ACKS_CONFIG, "all");

        // Add security configurations for Confluent Cloud
        configProps.put("security.protocol", "SASL_SSL");
        configProps.put("sasl.mechanism", "PLAIN");
        configProps.put("sasl.jaas.config", jaasConfig); // Example: "org.apache.kafka.common.security.plain.PlainLoginModule required username=\"<API_KEY>\" password=\"<API_SECRET>\";"

        return new DefaultKafkaProducerFactory<>(configProps);
    }

    @Bean
    public KafkaTemplate<String, String> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

    // Topic configuration
    @Bean
    public NewTopic userSignupTopic() {
        return TopicBuilder.name("user-signup-topic")
                .partitions(3)
                .replicas(3)
                .build();
    }
}
