package com.Uber.Ride.Booking.config;

import com.Uber.Ride.Booking.dto.WeatherData;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

@Service
@Slf4j
@EnableKafka
public class KafkaConsumerService {

    private final Map<Long, WeatherData> userWeatherData = new ConcurrentHashMap<>();

    @KafkaListener(topics = "weather-data-topic", groupId = "weather-consumer-group")
    public void consumeWeatherData(String weatherDataJson, @Header("kafka_receivedMessageKey") String userId) {
        try {

            WeatherData weatherData = new ObjectMapper().readValue(weatherDataJson, WeatherData.class);


            long userIdLong = Long.parseLong(userId);

            // Store weather data in the cache with userId as the key
            userWeatherData.put(userIdLong, weatherData);

            log.info("Received weather data for user {}: {}", userIdLong, weatherData);
        } catch (Exception e) {
            log.error("Failed to consume weather data for user {}: ", userId, e);
        }
    }

    public WeatherData getWeatherDataForUser(Long userId) {
        return userWeatherData.get(userId);
    }

    public boolean isBadWeather(Long userId) {

        for (int i = 0; i < 5; i++) {
            WeatherData weatherData = userWeatherData.get(userId);
            if (weatherData != null) {
                return weatherData.getCurrent().getCondition().getText().toLowerCase().contains("rain") ||
                        weatherData.getCurrent().getHumidityPercentage() > 90;
            }
            try {

                //To Eliminate the Race Condition
                Thread.sleep(100);
            } catch (InterruptedException e) {
                break;
            }
        }
        return false;
    }

}
