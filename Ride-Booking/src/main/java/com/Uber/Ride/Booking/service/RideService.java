package com.Uber.Ride.Booking.service;

import com.Uber.Ride.Booking.model.Ride;

import java.util.Optional;

public interface RideService {

    Ride bookInstantRide(Long userId, double pickupLatitude, double pickupLongitude,
                         double dropoffLatitude, double dropoffLongitude);

    Ride getRideById(Long rideId);

}
