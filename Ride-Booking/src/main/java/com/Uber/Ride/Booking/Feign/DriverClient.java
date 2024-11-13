package com.Uber.Ride.Booking.Feign;

import com.Uber.Ride.Booking.dto.DriverDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@FeignClient(name = "driver-service")  // Service discovery with Eureka
public interface DriverClient {
    @GetMapping("/driver/drivers/available")
    List<DriverDTO> getAvailableDrivers();

    @GetMapping("/drivers/{driverId}")
    DriverDTO getDriverById(@PathVariable("driverId") Long driverId);
}














