package com.picklemaps.picklemaps_app.services;

import com.picklemaps.picklemaps_app.domain.ReviewCreateUpdateRequest;
import com.picklemaps.picklemaps_app.domain.entities.Review;
import com.picklemaps.picklemaps_app.domain.entities.User;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface ReviewService {

    Review createReview(User author, String courtId, ReviewCreateUpdateRequest review);

    Page<Review> listReviews(String courtId, Pageable pageable);

    Optional<Review> getReview(String courtId, String reviewId);

    Review updateReview(
            User author, String courtId, String reviewId, ReviewCreateUpdateRequest review);

    void deleteReview(User author, String courtId, String reviewId);
}
