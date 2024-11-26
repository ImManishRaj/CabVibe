package com.Uber.Ride.Booking.serviceImpl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;

@Service
public class WeatherServiceConsumer {



    private static final Logger logger = LoggerFactory.getLogger(WeatherServiceConsumer.class);
    private final ConcurrentHashMap<String, Boolean> weatherCache = new ConcurrentHashMap<>();
    private final ObjectMapper objectMapper = new ObjectMapper();  // Reuse the ObjectMapper instance

    @KafkaListener(topics = "weather-data", groupId = "ride-service-group")
    public void consumeWeatherData(String weatherData, @Header(KafkaHeaders.RECEIVED_KEY) String userId) {
        try {
            logger.info("Received Kafka message - User ID: {}, Weather Data: {}", userId, weatherData);
            processWeatherDataForUser(userId, weatherData);
        } catch (Exception e) {
            logger.error("Error processing weather data for user {}: {}", userId, e.getMessage(), e);
        }
    }

    private void processWeatherDataForUser(String userId, String weatherData) {
        try {
            boolean isBadWeather = hasRain(weatherData) || isHotWeather(weatherData) || isWindy(weatherData);
            weatherCache.put(userId, isBadWeather);
            logger.info("Weather Analysis - User: {}, Bad Weather: {}", userId, isBadWeather);
        } catch (Exception e) {
            logger.error("Error processing weather data for user {}: {}", userId, e.getMessage(), e);
        }
    }

    private boolean hasRain(String weatherData) throws Exception {
        JsonNode rootNode = objectMapper.readTree(weatherData);
        String conditionText = rootNode.path("current").path("condition").path("text").asText();
        return conditionText != null && conditionText.toLowerCase().contains("rain");
    }

    private boolean isHotWeather(String weatherData) throws Exception {
        JsonNode rootNode = objectMapper.readTree(weatherData);
        double temperature = rootNode.path("current").path("temp_c").asDouble();
        return temperature > 45;  // Adjust threshold as needed for "hot" weather
    }

    private boolean isWindy(String weatherData) throws Exception {
        JsonNode rootNode = objectMapper.readTree(weatherData);
        double windSpeed = rootNode.path("current").path("wind_kph").asDouble();
        return windSpeed > 25;  // Adjust threshold for "windy" conditions
    }

    public boolean isBadWeatherForUser(String userId) {
        return weatherCache.getOrDefault(userId, false);
    }
}
