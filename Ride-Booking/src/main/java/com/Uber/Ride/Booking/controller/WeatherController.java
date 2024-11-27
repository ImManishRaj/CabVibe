package com.Uber.Ride.Booking.controller;

import com.Uber.Ride.Booking.dto.WeatherData;


import com.Uber.Ride.Booking.service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class WeatherController {

    @Autowired
    private WeatherService weatherService;

    /*@PostMapping("/test-weather")
    public String testWeatherData() {
        // Create a sample RideRequest with userId, latitude, and longitude
        RideRequest rideRequest = new RideRequest();
        rideRequest.setUserId(123L); // Sample User ID
        rideRequest.setPickupLatitude(10.0735); // Sample Latitude
        rideRequest.setPickupLongitude(78.7732); // Sample Longitude

        // Trigger the fetchWeatherData method
        weatherService.fetchWeatherAndSendToKafka(rideRequest).subscribe();

        return "Weather data sent to Kafka.";
    }*/

    @GetMapping("/weather_data")
    public WeatherData getData(@RequestParam Long  userId,@RequestParam double lat, @RequestParam double lon)
    {
        return weatherService.getWeatherData(lat,lon,userId);
    }
}
