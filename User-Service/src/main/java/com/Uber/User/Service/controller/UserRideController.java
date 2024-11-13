/*
package com.Uber.User.Service.controller;

import com.Uber.User.Service.model.RideBooking;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/rides")
public class UserRideController {

    @Autowired
    private RestTemplate restTemplate;  // Used for calling other microservices

    private static final String RIDE_SERVICE_URL = "http://ride-service/rides/book/";

    // Book an instant ride
    @PostMapping("/instant")
    public ResponseEntity<RideBooking> bookInstantRide(@RequestBody RideRequest rideRequest) {
        // Send a request to Ride Microservice to book an instant ride
        ResponseEntity<RideBooking> rideResponse = restTemplate.postForEntity(RIDE_SERVICE_URL + "instant", rideRequest, Ride.class);
        return rideResponse;
    }

    // Schedule a ride
    @PostMapping("/scheduled")
    public ResponseEntity<Ride> scheduleRide(@RequestBody RideRequest rideRequest) {
        // Send a request to Ride Microservice to schedule the ride
        ResponseEntity<Ride> rideResponse = restTemplate.postForEntity(RIDE_SERVICE_URL + "scheduled", rideRequest, Ride.class);
        return rideResponse;
    }
}
*/
