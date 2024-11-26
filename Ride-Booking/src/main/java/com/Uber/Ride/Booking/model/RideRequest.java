package com.Uber.Ride.Booking.model;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class RideRequest {
   /* private Long userId;
    private Double pickupLatitude;
    private Double pickupLongitude;
    private Double dropoffLatitude;
    private Double dropoffLongitude;*/


    private Long userId;
    private Double pickupLatitude;
    private Double pickupLongitude;
    private Double dropoffLatitude;
    private Double dropoffLongitude;
    private RideStatus status;
    private Double estimatedPrice;
    private String weatherImpact;
    private String trafficImpact;
    private String eventImpact;
    private LocalDateTime pickupTime;

    public RideRequest(Long userId, double pickupLatitude, double pickupLongitude) {

    }

    public RideRequest() {

    }
}

