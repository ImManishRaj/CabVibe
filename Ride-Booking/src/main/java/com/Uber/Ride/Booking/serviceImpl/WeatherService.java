package com.Uber.Ride.Booking.service;

import com.Uber.Ride.Booking.Feign.UserClient;
import com.Uber.Ride.Booking.config.KafkaProducerService;
import com.Uber.Ride.Booking.dto.WeatherData;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Duration;
import java.util.concurrent.CompletableFuture;

@Service
@Slf4j
@RequiredArgsConstructor
public class WeatherService {

    @Value("${weather.api.key}")
    private String apiKey;

   @Value("${weather.service.url}")
   private String BASE_URL;


   @Autowired
   private KafkaProducerService kafkaProducerService;


    private final WebClient webClient;  // Marked as final so Lombok generates the constructor

    public WeatherData getWeatherData(double latitude, double longitude,Long userId) {
        String additionalUrl = String.format("?key=%s&q=%s,%s&aqi=no", apiKey, latitude, longitude);

        WeatherData weatherData = webClient.get()
                .uri(BASE_URL + additionalUrl)
                .retrieve()
                .bodyToMono(WeatherData.class)
                .block();  // This is blocking the thread

        log.info("Weather Data : {}",weatherData);
        kafkaProducerService.sendDataToKafka(weatherData,userId);
        log.info("Data has been send to kafka");
        return weatherData;

    }
}