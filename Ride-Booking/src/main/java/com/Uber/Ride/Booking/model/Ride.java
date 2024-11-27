package com.Uber.Ride.Booking.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class Ride {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "driver_id")
    private Long driverId;

    // Separate columns for pickup location (latitude and longitude)
    @Column(name = "pickup_latitude", nullable = false)
    private Double pickupLatitude;

    @Column(name = "pickup_longitude", nullable = false)
    private Double pickupLongitude;


    @Column(name = "dropoff_latitude", nullable = false)
    private Double dropoffLatitude;

    @Column(name = "dropoff_longitude", nullable = false)
    private Double dropoffLongitude;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private RideStatus status;

    @Column(name = "fare")
    private Double fare;

    @Column(name = "booking_time")
    private LocalDateTime scheduledTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "ride_type")
    private RideType rideType;
}
