package com.Uber.Ride.Booking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.kafka.annotation.EnableKafka;

@SpringBootApplication(scanBasePackages = "com.Uber.Ride.Booking")
@EnableFeignClients
@EnableKafka
public class RideBookingApplication {

	public static void main(String[] args) {
		int[] arr=new int[5];

		SpringApplication.run(RideBookingApplication.class, args);
	}

}
