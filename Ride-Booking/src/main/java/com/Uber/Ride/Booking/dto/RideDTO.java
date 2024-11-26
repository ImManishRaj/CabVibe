package com.Uber.Ride.Booking.dto;

import com.Uber.Ride.Booking.model.Ride;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RideDTO {

    private Long rideId;
    private Double pickupLatitude;
    private Double pickupLongitude;
    private Double dropoffLongitude;
    private Double dropoffLatitude;
    private String status;
    private String rideType;
    private Double fare;
    private UserDTO userDTO;
    private DriverDTO driverDTO;

    public RideDTO(Ride ride, UserDTO userDTO, DriverDTO driverDTO) {
        this.rideId = ride.getId();
        this.pickupLatitude = ride.getPickupLatitude();
        this.pickupLongitude = ride.getPickupLongitude();
        this.dropoffLatitude = ride.getDropoffLatitude();
        this.dropoffLongitude = ride.getDropoffLongitude(); // Corrected the assignment here
        this.status = ride.getStatus().name();
        this.rideType = ride.getRideType().name();
        this.fare = ride.getFare();
        this.userDTO = userDTO;
        this.driverDTO = driverDTO;
    }
}
