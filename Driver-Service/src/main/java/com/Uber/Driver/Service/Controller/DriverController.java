package com.Uber.Driver.Service.Controller;

import com.Uber.Driver.Service.model.Driver;
import com.Uber.Driver.Service.model.DriverDTO;
import com.Uber.Driver.Service.repository.DriverRepository;
import com.Uber.Driver.Service.service.DriverServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/driver")
public class DriverController {


    @Autowired
    private DriverServices driverServices;

    @Autowired
    private DriverRepository driverRepository;

    @PostMapping("/signup")
    public Driver saveDriver(@RequestBody Driver driver)
    {
        return driverServices.SaveDriver(driver);
    }

    @GetMapping("/drivers/available")
    public List<DriverDTO> getAvailableDrivers() {
        // Fetching drivers with the status ONLINE
        List<Driver> availableDrivers = driverRepository.findByStatus(Driver.DriverStatus.ONLINE);

        availableDrivers.forEach(driver -> {
            System.out.println("Driver ID: " + driver.getId() +
                    ", Latitude: " + driver.getLatitude() +
                    ", Longitude: " + driver.getLongitude());
        });

        return availableDrivers.stream()
                .map(driver -> new DriverDTO(
                        driver.getId(),
                        driver.getName(),
                        driver.getPhoneNumber(),
                        driver.getLicenseNumber(),
                        driver.getVehicleDetails(),
                        driver.getStatus(),
                        driver.getRating(),
                        driver.getLatitude(),    // Added latitude
                        driver.getLongitude()))  // Added longitude
                .collect(Collectors.toList());
    }

}
