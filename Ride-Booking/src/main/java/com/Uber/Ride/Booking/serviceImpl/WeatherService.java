package com.Uber.Ride.Booking.serviceImpl;

import com.Uber.Ride.Booking.model.RideRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;

@Service
public class WeatherService {

    private static final Logger logger = LoggerFactory.getLogger(WeatherService.class);
    private static final Duration REQUEST_TIMEOUT = Duration.ofSeconds(10);

    @Value("${weather.api.key}")
    private String apiKey;

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final WebClient webClient;

    public WeatherService(WebClient.Builder webClientBuilder, KafkaTemplate<String, String> kafkaTemplate) {
        String BASE_URL = "http://api.weatherapi.com/v1/current.json";
        this.webClient = webClientBuilder.baseUrl(BASE_URL).build();
        this.kafkaTemplate = kafkaTemplate;
    }

    public Mono<Void> fetchWeatherData(RideRequest rideRequest) {
        if (rideRequest == null ||
                rideRequest.getPickupLatitude() == 0 ||
                rideRequest.getPickupLongitude() == 0 ||
                rideRequest.getUserId() == null) {
            logger.error("Invalid ride request: Null coordinates or user ID");
            return Mono.error(new IllegalArgumentException("Invalid ride request details"));
        }

        String latitude = String.valueOf(rideRequest.getPickupLatitude());
        String longitude = String.valueOf(rideRequest.getPickupLongitude());

        logger.info("Fetching weather data for coordinates: {}, {}", latitude, longitude);

        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .queryParam("key", apiKey)
                        .queryParam("q", latitude + "," + longitude)
                        .queryParam("aqi", "no")
                        .build())
                .retrieve()
                .bodyToMono(String.class)
                .timeout(REQUEST_TIMEOUT)
                .publishOn(Schedulers.boundedElastic())
                .flatMap(data -> {
                    logger.info("Weather Data: {}", data);

                    // Send the weather data to Kafka
                    return Mono.fromFuture(() -> kafkaTemplate.send("weather-data", rideRequest.getUserId().toString(), data))
                            .doOnTerminate(() -> logger.info("Kafka send operation complete for User: {}", rideRequest.getUserId()))
                            .then(); // Convert to Mono<Void>
                })
                .doOnError(error -> logger.error("Weather data processing failed: {}", error.getMessage(), error))
                .onErrorResume(error -> {
                    logger.error("Unhandled error in weather data processing", error);
                    return Mono.empty(); // Continue gracefully even if there's an error
                });
    }
}
