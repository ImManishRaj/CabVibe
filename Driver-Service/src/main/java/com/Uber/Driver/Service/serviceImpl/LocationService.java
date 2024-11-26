/*
package com.Uber.Driver.Service.serviceImpl;

import com.google.maps.GeoApiContext;
import com.google.maps.Geolocation;
import com.google.maps.model.GeolocationResult;
import com.Uber.Driver.Service.model.Driver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class LocationService {

    @Value("${maps.api.key}")
    private String API_KEY ;

    private final GeoApiContext context;

    public LocationService() {
        this.context = new GeoApiContext.Builder()
                .apiKey(googleApiKey)
                .build();
    }

    public void updateDriverLocation(Driver driver) {
        try {
            GeolocationResult result = context.geolocation(new GeolocationRequest()).await(30, TimeUnit.SECONDS);

            // Update the driver's location with the results
            driver.setLatitude(result.location.lat);
            driver.setLongitude(result.location.lng);

            // Print out the new location for debugging
            System.out.println("Driver's new location: Latitude = " + result.location.lat + ", Longitude = " + result.location.lng);

        } catch (Exception e) {
            // Handle potential exceptions such as timeouts, API errors, etc.
            e.printStackTrace();
        }
    }
}
*/
