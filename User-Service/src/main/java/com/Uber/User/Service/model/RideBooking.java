package com.Uber.User.Service.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name="Booking")
public class RideBooking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rideId;
    @Column(name = "pickup_locn", nullable = false)
    private String pickupLocation;
    @Column(name = "drop_locn", nullable = false)
    private String dropLocation;
}
