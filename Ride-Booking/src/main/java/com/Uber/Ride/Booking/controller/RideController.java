package com.Uber.Ride.Booking.controller;

import com.Uber.Ride.Booking.model.Ride;
import com.Uber.Ride.Booking.service.RideService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/rides")
public class RideController {

    private final RideService rideService;

    @Autowired
    public RideController(RideService rideService) {
        this.rideService = rideService;
    }

    @PostMapping("/book")
    public ResponseEntity<Ride> bookRide(@RequestParam Long userId,
                                         @RequestParam double pickupLatitude,
                                         @RequestParam double pickupLongitude,
                                         @RequestParam double dropoffLatitude,
                                         @RequestParam double dropoffLongitude) {
        Ride ride = rideService.bookInstantRide(userId, pickupLatitude, pickupLongitude,
                dropoffLatitude, dropoffLongitude);
        return ResponseEntity.ok(ride);
    }

    @GetMapping("/{rideId}")
    public ResponseEntity<Ride> getRide(@PathVariable Long rideId) {
        Ride ride = rideService.getRideById(rideId);
        return ResponseEntity.ok(ride);
    }
}