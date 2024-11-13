package com.Uber.Driver.Service.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "rides")
public class Ride {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "driver_id", nullable = false)
    private Driver driver;



    @Column(nullable = false)
    private Double pickupLongitude;

    @Column(name = "dropoff_location", nullable = false)
    private Double dropoffLocation;

    @Column(name = "pickup_location", nullable = false)
    private Double pickupLatitude;


    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private RideStatus status;

    @Column(name = "fare", nullable = false)
    private Double fare;

    @Column(name = "rating")
    private Double rating;

    // Enum for ride status (Pending, Accepted, Completed)
    public enum RideStatus {
        PENDING,
        ACCEPTED,
        COMPLETED
    }
}
