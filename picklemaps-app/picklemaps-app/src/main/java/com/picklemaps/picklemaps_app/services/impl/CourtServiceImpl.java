package com.picklemaps.picklemaps_app.services.impl;

import com.picklemaps.picklemaps_app.domain.CourtCreateUpdateRequest;
import com.picklemaps.picklemaps_app.domain.GeoLocation;
import com.picklemaps.picklemaps_app.domain.entities.Address;
import com.picklemaps.picklemaps_app.domain.entities.Court;
import com.picklemaps.picklemaps_app.domain.entities.Photo;
import com.picklemaps.picklemaps_app.exceptions.CourtNotFoundException;
import com.picklemaps.picklemaps_app.repositories.CourtRepository;
import com.picklemaps.picklemaps_app.services.CourtService;
import com.picklemaps.picklemaps_app.services.GeoLocationService;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CourtServiceImpl implements CourtService {

    private final CourtRepository courtRepository;
    private final GeoLocationService geoLocationService;

    @Override
    public Court createCourt(CourtCreateUpdateRequest request) {
        Address address = request.getAddress();
        GeoLocation geoLocation = geoLocationService.getLocate(address);

        GeoPoint geoPoint = new GeoPoint(geoLocation.getLatitude(), geoLocation.getLongitude());

        List<String> photoIds = request.getPhotoIds();
        List<Photo> photos =
                photoIds.stream()
                        .map(
                                photoUrl ->
                                        Photo.builder()
                                                .url(photoUrl)
                                                .uploadDate(LocalDateTime.now())
                                                .build())
                        .toList();

        Court court =
                Court.builder()
                        .name(request.getName())
                        .courtType(request.getCourtType())
                        .surfaceType(request.getSurfaceType())
                        .contactInformation(request.getContactInformation())
                        .address(address)
                        .geoLocation(geoPoint)
                        .operatingHours(request.getOperatingHours())
                        .averageRating(0f)
                        .photos(photos)
                        .build();

        return courtRepository.save(court);
    }

    @Override
    public Page<Court> searchCourts(
            String query,
            Float minRating,
            Float latitude,
            Float longitude,
            Float radius,
            Pageable pageable) {

        if (null != minRating && (null == query || query.isEmpty())) {
            return courtRepository.findByAverageRatingGreaterThanEqual(minRating, pageable);
        }

        Float searchMinRating = null == minRating ? 0f : minRating;

        if (null != query && !query.trim().isEmpty()) {
            return courtRepository.findByQueryAndMinRating(query, searchMinRating, pageable);
        }

        if (null != latitude && null != longitude && null != radius) {
            return courtRepository.findByLocationNear(latitude, longitude, radius, pageable);
        }

        return courtRepository.findAll(pageable);
    }

    @Override
    public Optional<Court> getCourt(String id) {
        return courtRepository.findById(id);
    }

    @Override
    public Court updateCourt(String id, CourtCreateUpdateRequest request) {
        Court court =
                getCourt(id)
                        .orElseThrow(
                                () ->
                                        new CourtNotFoundException(
                                                "Court with ID does not exist: " + id));

        GeoLocation newGeoLocation = geoLocationService.getLocate(request.getAddress());

        GeoPoint newGeoPoint =
                new GeoPoint(newGeoLocation.getLatitude(), newGeoLocation.getLongitude());

        List<String> photoIds = request.getPhotoIds();
        List<Photo> photos =
                photoIds.stream()
                        .map(
                                photoUrl ->
                                        Photo.builder()
                                                .url(photoUrl)
                                                .uploadDate(LocalDateTime.now())
                                                .build())
                        .toList();

        court.setName(request.getName());
        court.setCourtType(request.getCourtType());
        court.setSurfaceType(request.getSurfaceType());
        court.setContactInformation(request.getContactInformation());
        court.setAddress(request.getAddress());
        court.setGeoLocation(newGeoPoint);
        court.setOperatingHours(request.getOperatingHours());
        court.setPhotos(photos);

        return courtRepository.save(court);
    }

    @Override
    public void deleteRestaurant(String id) {
        courtRepository.deleteById(id);
    }
}
