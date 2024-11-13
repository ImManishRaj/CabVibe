package com.Uber.Ride.Booking.Feign;

import com.Uber.Ride.Booking.dto.UserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "user-service")  // Service discovery with Eureka
public interface UserClient {
    @GetMapping("/user/findById")
    UserDTO getUserById(@RequestParam("id") Long userId);
}
