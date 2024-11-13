package com.Uber.Driver.Service.model;

import com.Uber.Driver.Service.model.Driver.DriverStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DriverDTO {
    private Long id;
    private String name;
    private String phoneNumber;
    private String licenseNumber;
    private String vehicleDetails;
    private DriverStatus status;
    private Double rating;
    private Double latitude;  // Added latitude
    private Double longitude; // Added longitude
}
