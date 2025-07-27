package com.picklemaps.picklemaps_app.services;

import com.picklemaps.picklemaps_app.domain.GeoLocation;
import com.picklemaps.picklemaps_app.domain.entities.Address;

public interface GeoLocationService {

    GeoLocation getLocate(Address address);
}
