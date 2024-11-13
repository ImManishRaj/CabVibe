package com.Uber.Driver.Service.serviceImpl;

import com.Uber.Driver.Service.model.Driver;
import com.Uber.Driver.Service.repository.DriverRepository;
import com.Uber.Driver.Service.service.DriverServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DriverServiceImpl implements DriverServices {

    @Autowired
    private DriverRepository driverRepository;

    @Override
    public Driver SaveDriver(Driver driver) {
        return driverRepository.save(driver);
    }
}
