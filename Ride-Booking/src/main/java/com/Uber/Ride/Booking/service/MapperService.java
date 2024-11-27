package com.Uber.Ride.Booking.service;

import com.Uber.Ride.Booking.dto.DriverDTO;
import com.Uber.Ride.Booking.dto.RideDTO;
import com.Uber.Ride.Booking.dto.UserDTO;
import com.Uber.Ride.Booking.model.Ride;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

public class MapperService {

    @Autowired
    private ModelMapper modelMapper;

    public RideDTO convertToDTO(Ride ride, UserDTO userDTO, DriverDTO driverDTO) {
        RideDTO rideDTO = modelMapper.map(ride, RideDTO.class);
        rideDTO.setUserDTO(userDTO);
        rideDTO.setDriverDTO(driverDTO);
        return rideDTO;
    }

    public Ride convertToDTO(RideDTO rideDTO)
    {
        return modelMapper.map(rideDTO, Ride.class);
    }
}
