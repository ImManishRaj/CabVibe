package com.Uber.Ride.Booking.model;

public enum RideStatus {
    REQUESTED,       // Ride has been requested but not yet assigned
    IN_PROGRESS,     // Ride is in progress
    COMPLETED,       // Ride has been completed
    CANCELLED,       // Ride was cancelled by the user or driver
    PENDING_PAYMENT,// Ride is completed but payment is still pending
    PENDING,
    DELAYED
}