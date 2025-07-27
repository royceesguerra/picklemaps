package com.picklemaps.picklemaps_app.services.impl;

import com.picklemaps.picklemaps_app.domain.GeoLocation;
import com.picklemaps.picklemaps_app.domain.entities.Address;
import com.picklemaps.picklemaps_app.services.GeoLocationService;

import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class RandomNCRPlusGeoLocationService implements GeoLocationService {

    private static final float MIN_LATITUDE = 13.80f;
    private static final float MAX_LATITUDE = 15.25f;
    private static final float MIN_LONGITUDE = 120.85f;
    private static final float MAX_LONGITUDE = 121.25f;

    @Override
    public GeoLocation getLocate(Address address) {

        Random random = new Random();
        double latitude = MIN_LATITUDE + random.nextDouble() * (MAX_LATITUDE - MIN_LATITUDE);
        double longitude = MIN_LONGITUDE + random.nextDouble() * (MAX_LONGITUDE - MIN_LONGITUDE);

        return GeoLocation.builder().latitude(latitude).longitude(longitude).build();
    }
}
