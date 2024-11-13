package com.Uber.User.Service.repository;

import com.Uber.User.Service.model.RideBooking;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RideRepository extends JpaRepository<RideBooking,Long> {

}
