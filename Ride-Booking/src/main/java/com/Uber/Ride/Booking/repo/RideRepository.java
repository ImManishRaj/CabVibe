package com.Uber.Ride.Booking.repo;

import com.Uber.Ride.Booking.model.Ride;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RideRepository extends JpaRepository<Ride,Long> {
}
