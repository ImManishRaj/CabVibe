package com.Uber.Ride.Booking.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RideRequest {
    private Long userId;
    private Double pickupLatitude;
    private Double pickupLongitude;
    private Double dropoffLatitude;
    private Double dropoffLongitude;
}

