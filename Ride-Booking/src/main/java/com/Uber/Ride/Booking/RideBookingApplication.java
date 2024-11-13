package com.Uber.Ride.Booking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication(scanBasePackages = "com.Uber.Ride.Booking")
@EnableFeignClients
public class RideBookingApplication {

	public static void main(String[] args) {
		SpringApplication.run(RideBookingApplication.class, args);
	}

}
