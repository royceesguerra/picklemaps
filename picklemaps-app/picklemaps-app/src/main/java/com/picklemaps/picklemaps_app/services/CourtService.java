package com.picklemaps.picklemaps_app.services;

import com.picklemaps.picklemaps_app.domain.CourtCreateUpdateRequest;
import com.picklemaps.picklemaps_app.domain.entities.Court;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface CourtService {

    Court createCourt(CourtCreateUpdateRequest request);

    Page<Court> searchCourts(
            String query,
            Float minRating,
            Float latitude,
            Float longitude,
            Float radius,
            Pageable pageable);

    Optional<Court> getCourt(String id);

    Court updateCourt(String id, CourtCreateUpdateRequest courtCreateUpdateRequest);

    void deleteRestaurant(String id);
}
