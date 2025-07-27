package com.picklemaps.picklemaps_app.controllers;

import com.picklemaps.picklemaps_app.domain.CourtCreateUpdateRequest;
import com.picklemaps.picklemaps_app.domain.dtos.CourtCreateUpdateRequestDto;
import com.picklemaps.picklemaps_app.domain.dtos.CourtDto;
import com.picklemaps.picklemaps_app.domain.dtos.CourtSummaryDto;
import com.picklemaps.picklemaps_app.domain.entities.Court;
import com.picklemaps.picklemaps_app.mappers.CourtMapper;
import com.picklemaps.picklemaps_app.services.CourtService;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/courts")
@RequiredArgsConstructor
public class CourtController {

    private final CourtService courtService;

    private final CourtMapper courtMapper;

    @PostMapping
    public ResponseEntity<CourtDto> createCourt(
            @Valid @RequestBody CourtCreateUpdateRequestDto request) {

        CourtCreateUpdateRequest courtCreateUpdateRequest =
                courtMapper.toCourtCreateUpdateRequest(request);

        Court court = courtService.createCourt(courtCreateUpdateRequest);
        CourtDto createdCourtDto = courtMapper.toCourtDto(court);

        return ResponseEntity.ok(createdCourtDto);
    }

    @GetMapping
    public Page<CourtSummaryDto> searchCourts(
            @RequestParam(required = false) String q,
            @RequestParam(required = false) Float minRating,
            @RequestParam(required = false) Float latitude,
            @RequestParam(required = false) Float longitude,
            @RequestParam(required = false) Float radius,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int size) {

        Page<Court> searchResults =
                courtService.searchCourts(
                        q, minRating, latitude, longitude, radius, PageRequest.of(page - 1, size));

        return searchResults.map(courtMapper::toSummaryDto);
    }

    @GetMapping(path = "/{court_id}")
    public ResponseEntity<CourtDto> getCourt(@PathVariable("court_id") String courtId) {
        return courtService
                .getCourt(courtId)
                .map(court -> ResponseEntity.ok(courtMapper.toCourtDto(court)))
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping(path = "/{court_id}")
    public ResponseEntity<CourtDto> updateCourt(
            @PathVariable("court_id") String courtId,
            @Valid @RequestBody CourtCreateUpdateRequestDto requestDto) {

        CourtCreateUpdateRequest request = courtMapper.toCourtCreateUpdateRequest(requestDto);

        Court updatedCourt = courtService.updateCourt(courtId, request);

        return ResponseEntity.ok(courtMapper.toCourtDto(updatedCourt));
    }

    @DeleteMapping(path = "/{court_id}")
    public ResponseEntity<Void> deleteCourt(@PathVariable("court_id") String courtId) {

        courtService.deleteRestaurant(courtId);

        return ResponseEntity.noContent().build();
    }
}
