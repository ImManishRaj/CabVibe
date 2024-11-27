package com.Uber.Ride.Booking.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class WeatherData {
    private Location location;
    private Current current;

    @Data
    public static class Location {
        private String name;
        private String region;
        private String country;
        private double lat;
        private double lon;
    }

    @Data
    public static class Current {
        @JsonProperty("temp_c")
        private double tempCelsius;

        @JsonProperty("temp_f")
        private double tempFahrenheit;

        private Condition condition;

        @JsonProperty("wind_kph")
        private double windSpeed;

        @JsonProperty("humidity")
        private int humidityPercentage;
    }

    @Data
    public static class Condition {
        private String text;
        private String icon;
    }
}