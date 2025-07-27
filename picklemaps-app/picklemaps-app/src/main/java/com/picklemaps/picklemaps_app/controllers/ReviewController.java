package com.picklemaps.picklemaps_app.controllers;

import com.picklemaps.picklemaps_app.domain.ReviewCreateUpdateRequest;
import com.picklemaps.picklemaps_app.domain.dtos.ReviewCreateUpdateRequestDto;
import com.picklemaps.picklemaps_app.domain.dtos.ReviewDto;
import com.picklemaps.picklemaps_app.domain.entities.Review;
import com.picklemaps.picklemaps_app.domain.entities.User;
import com.picklemaps.picklemaps_app.mappers.ReviewMapper;
import com.picklemaps.picklemaps_app.services.ReviewService;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/api/courts/{courtId}/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewMapper reviewMapper;

    private final ReviewService reviewService;

    @PostMapping
    public ResponseEntity<ReviewDto> createReview(
            @PathVariable String courtId,
            @Valid @RequestBody ReviewCreateUpdateRequestDto review,
            @AuthenticationPrincipal Jwt jwt) {

        ReviewCreateUpdateRequest reviewCreateUpdateRequest =
                reviewMapper.toReviewCreateUpdateRequest(review);

        User user = jwtToUser(jwt);

        Review createdReview = reviewService.createReview(user, courtId, reviewCreateUpdateRequest);

        return ResponseEntity.ok(reviewMapper.toDto(createdReview));
    }

    @GetMapping
    public Page<ReviewDto> listReviews(
            @PathVariable String courtId,
            @PageableDefault(
                            size = 20,
                            page = 0,
                            sort = "datePosted",
                            direction = Sort.Direction.DESC)
                    Pageable pageable) {

        return reviewService.listReviews(courtId, pageable).map(reviewMapper::toDto);
    }

    @GetMapping(path = "/{reviewId}")
    public ResponseEntity<ReviewDto> getReview(
            @PathVariable String courtId, @PathVariable String reviewId) {

        return reviewService
                .getReview(courtId, reviewId)
                .map(reviewMapper::toDto)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.noContent().build());
    }

    @PutMapping(path = "/{reviewId}")
    public ResponseEntity<ReviewDto> updateReview(
            @PathVariable String courtId,
            @PathVariable String reviewId,
            @Valid @RequestBody ReviewCreateUpdateRequestDto review,
            @AuthenticationPrincipal Jwt jwt) {

        ReviewCreateUpdateRequest reviewCreateUpdateRequest =
                reviewMapper.toReviewCreateUpdateRequest(review);

        User user = jwtToUser(jwt);

        Review updatedReview =
                reviewService.updateReview(user, courtId, reviewId, reviewCreateUpdateRequest);

        return ResponseEntity.ok(reviewMapper.toDto(updatedReview));
    }

    @DeleteMapping(path = "/{reviewId}")
    public ResponseEntity<Void> deleteReview(
            @PathVariable String courtId,
            @PathVariable String reviewId,
            @AuthenticationPrincipal Jwt jwt) {

        User user = jwtToUser(jwt);

        reviewService.deleteReview(user, courtId, reviewId);
        return ResponseEntity.noContent().build();
    }

    private User jwtToUser(Jwt jwt) {
        return User.builder()
                .id(jwt.getSubject())
                .username(jwt.getClaimAsString("preferred_username"))
                .givenName(jwt.getClaimAsString("given_name"))
                .familyName(jwt.getClaimAsString("family_name"))
                .build();
    }
}
