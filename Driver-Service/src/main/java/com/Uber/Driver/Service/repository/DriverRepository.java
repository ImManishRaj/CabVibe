package com.Uber.Driver.Service.repository;

import com.Uber.Driver.Service.model.Driver;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DriverRepository extends JpaRepository<Driver, Long> {
    Driver findByEmail(String email);
    Driver findByPhoneNumber(String phoneNumber);
    List<Driver> findByStatus(Driver.DriverStatus status);
}