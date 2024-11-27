package com.Uber.Ride.Booking.config;

import com.Uber.Ride.Booking.dto.WeatherData;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Data
public class KafkaProducerService {

    private static final Logger log = LoggerFactory.getLogger(KafkaProducerService.class);
    private final KafkaTemplate<String,String> kafkaTemplate;

    private static final String WEATHER_TOPIC = "weather-data-topic";

    public void sendDataToKafka(WeatherData weatherData, Long userId)
    {
        String data = convertWeatherDataToJson(weatherData);
        String key = String.valueOf(userId);
        log.info("Weather data in Producer service for user {}: {}", userId, data);
        kafkaTemplate.send(WEATHER_TOPIC, key, data);  // Sending the data with userId as key
        log.info("Data has been sent to topic: {} for user: {}", WEATHER_TOPIC, userId);
    }


    private String convertWeatherDataToJson(WeatherData weatherData) {
        try {
            return new ObjectMapper().writeValueAsString(weatherData);
        } catch (Exception e) {
            throw new RuntimeException("Failed to convert WeatherData to JSON", e);
        }
    }
}
