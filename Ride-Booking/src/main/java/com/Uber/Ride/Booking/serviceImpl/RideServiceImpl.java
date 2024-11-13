package com.Uber.Ride.Booking.serviceImpl;

import com.Uber.Ride.Booking.Feign.DriverClient;
import com.Uber.Ride.Booking.Feign.UserClient;
import com.Uber.Ride.Booking.dto.DriverDTO;
import com.Uber.Ride.Booking.dto.UserDTO;
import com.Uber.Ride.Booking.model.Ride;
import com.Uber.Ride.Booking.model.RideStatus;
import com.Uber.Ride.Booking.model.RideType;
import com.Uber.Ride.Booking.repo.RideRepository;
import com.Uber.Ride.Booking.service.RideService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class RideServiceImpl implements RideService {

    private static final Logger logger = LoggerFactory.getLogger(RideServiceImpl.class);
    private static final double EARTH_RADIUS_KM = 6371.0;

    private final RideRepository rideRepository;
    private final UserClient userClient;
    private final DriverClient driverClient;

    @Autowired
    public RideServiceImpl(RideRepository rideRepository, UserClient userClient, DriverClient driverClient) {
        this.rideRepository = rideRepository;
        this.userClient = userClient;
        this.driverClient = driverClient;
    }

    @Override
    public Ride bookInstantRide(Long userId, double pickupLatitude, double pickupLongitude,
                                double dropoffLatitude, double dropoffLongitude) {
        logger.info("Booking ride for user: {}", userId);

        UserDTO userDTO = userClient.getUserById(userId);
        if (userDTO == null) {
            throw new RuntimeException("User not found with id: " + userId);
        }
        logger.info("Fetched User: {}", userDTO);

        List<DriverDTO> availableDrivers = getAvailableDrivers();
        if (availableDrivers.isEmpty()) {
            throw new RuntimeException("No available drivers at the moment.");
        }

        DriverDTO nearestDriver = availableDrivers.stream()
                .filter(driver -> driver.getLatitude() != null && driver.getLongitude() != null)
                .min(Comparator.comparingDouble(driver -> calculateDistance(pickupLatitude, pickupLongitude,
                        driver.getLatitude(), driver.getLongitude())))
                .orElseThrow(() -> new RuntimeException("No available drivers at the moment."));

        logger.info("Nearest driver: {}", nearestDriver.getName());

        Ride ride = new Ride();
        ride.setUserId(userDTO.getId());
        ride.setDriverId(nearestDriver.getId());
        ride.setPickupLatitude(pickupLatitude);
        ride.setPickupLongitude(pickupLongitude);
        ride.setDropoffLatitude(dropoffLatitude);
        ride.setDropoffLongitude(dropoffLongitude);
        ride.setStatus(RideStatus.PENDING);
        ride.setRideType(RideType.INSTANT);

        double fare = calculateFare(pickupLatitude, pickupLongitude, dropoffLatitude, dropoffLongitude);
        ride.setFare(fare);

        logger.info("Ride to be saved: {}", ride);
        Ride savedRide = rideRepository.save(ride);
        logger.info("Ride booked successfully with ID: {}", savedRide.getId());

        return savedRide;
    }

    @Override
    public Ride getRideById(Long rideId) {
        return rideRepository.findById(rideId)
                .orElseThrow(() -> new RuntimeException("Ride not found with id: " + rideId));
    }

    private List<DriverDTO> getAvailableDrivers() {
        try {
            List<DriverDTO> availableDrivers = driverClient.getAvailableDrivers();
            if (availableDrivers != null && !availableDrivers.isEmpty()) {
                logger.info("Fetched Available Drivers: {}", availableDrivers);
                return availableDrivers;
            } else {
                logger.info("No available drivers found.");
                return List.of();
            }
        } catch (Exception e) {
            logger.error("Error while fetching available drivers: {}", e.getMessage(), e);
            throw new RuntimeException("Error while fetching available drivers", e);
        }
    }

    private double calculateFare(double pickupLat, double pickupLon, double dropoffLat, double dropoffLon) {
        double distance = calculateDistance(pickupLat, pickupLon, dropoffLat, dropoffLon);

        double baseFare = 2.5;  // base fare in USD
        double farePerKm = 1.2;  // fare per kilometer
        double surgeMultiplier = 1.0;  // surge multiplier (for simplicity, no surge pricing here)

        return baseFare + (distance * farePerKm) * surgeMultiplier;
    }

    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        double lat1Rad = Math.toRadians(lat1);
        double lon1Rad = Math.toRadians(lon1);
        double lat2Rad = Math.toRadians(lat2);
        double lon2Rad = Math.toRadians(lon2);

        double deltaLat = lat2Rad - lat1Rad;
        double deltaLon = lon2Rad - lon1Rad;

        double a = Math.sin(deltaLat / 2) * Math.sin(deltaLat / 2) +
                Math.cos(lat1Rad) * Math.cos(lat2Rad) *
                        Math.sin(deltaLon / 2) * Math.sin(deltaLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return EARTH_RADIUS_KM * c;
    }
}