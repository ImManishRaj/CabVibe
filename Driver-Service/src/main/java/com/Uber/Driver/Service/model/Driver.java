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
@Table(name = "drivers")
public class Driver {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "phone_number", nullable = false, unique = true)
    private String phoneNumber;

    @Column(name = "license_number", nullable = false, unique = true)
    private String licenseNumber;

    @Column(name = "vehicle_details", nullable = false)
    private String vehicleDetails;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private DriverStatus status;

    private Double latitude;

    private Double longitude;

    @Column(name = "rating")
    private Double rating;

    public enum DriverStatus {
        ONLINE,
        OFFLINE
    }
}
