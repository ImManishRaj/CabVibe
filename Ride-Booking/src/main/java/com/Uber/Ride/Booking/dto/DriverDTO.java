package com.Uber.Ride.Booking.dto;

import com.Uber.Driver.Service.model.Driver
        .DriverStatus; // Assuming DriverStatus enum exists
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DriverDTO {

    private Long id;
    private String name;
    private String phoneNumber;
    private String licenseNumber;
    private String vehicleDetails;
    private Double latitude;
    private Double longitude;
    private DriverStatus status;
    private Double rating;

    public DriverDTO(Long id, String name, String phoneNumber, String licenseNumber,
                     String vehicleDetails, DriverStatus status, Double rating,
                     Double latitude, Double longitude) {
        this.id = id;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.licenseNumber = licenseNumber;
        this.vehicleDetails = vehicleDetails;
        this.status = status;
        this.rating = rating;
        this.latitude = latitude;
        this.longitude = longitude;
    }


}

