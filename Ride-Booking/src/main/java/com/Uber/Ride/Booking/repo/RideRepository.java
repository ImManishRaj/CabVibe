package com.Uber.Ride.Booking.repo;

import com.Uber.Ride.Booking.model.Ride;
import org.springframework.beans.PropertyValues;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RideRepository extends JpaRepository<Ride,Long> {
    List<Ride> findByUserId(Long userId);
}
