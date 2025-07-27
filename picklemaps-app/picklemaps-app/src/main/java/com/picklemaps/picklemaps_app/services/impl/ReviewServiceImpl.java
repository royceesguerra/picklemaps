package com.picklemaps.picklemaps_app.services.impl;

import com.picklemaps.picklemaps_app.domain.ReviewCreateUpdateRequest;
import com.picklemaps.picklemaps_app.domain.entities.Court;
import com.picklemaps.picklemaps_app.domain.entities.Photo;
import com.picklemaps.picklemaps_app.domain.entities.Review;
import com.picklemaps.picklemaps_app.domain.entities.User;
import com.picklemaps.picklemaps_app.exceptions.CourtNotFoundException;
import com.picklemaps.picklemaps_app.exceptions.ReviewNotAllowedException;
import com.picklemaps.picklemaps_app.repositories.CourtRepository;
import com.picklemaps.picklemaps_app.services.ReviewService;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService {

    private final CourtRepository courtRepository;

    private static Optional<Review> getReviewFromCourt(String reviewId, Court court) {
        return court.getReviews().stream().filter(r -> reviewId.equals(r.getId())).findFirst();
    }

    @Override
    public Review createReview(User author, String courtId, ReviewCreateUpdateRequest review) {
        Court court = getCourtOrThrow(courtId);

        boolean hasExistingReview =
                court.getReviews().stream()
                        .anyMatch(r -> r.getWrittenBy().getId().equals(author.getId()));

        if (hasExistingReview) {
            throw new ReviewNotAllowedException("User has already reviewed this court");
        }

        LocalDateTime now = LocalDateTime.now();

        List<Photo> photos =
                review.getPhotoIds().stream()
                        .map(
                                url -> {
                                    return Photo.builder().url(url).uploadDate(now).build();
                                })
                        .toList();

        String reviewId = UUID.randomUUID().toString();

        Review reviewToCreate =
                Review.builder()
                        .id(reviewId)
                        .content(review.getContent())
                        .rating(review.getRating())
                        .photos(photos)
                        .datePosted(now)
                        .lastEdited(now)
                        .writtenBy(author)
                        .build();

        court.getReviews().add(reviewToCreate);

        updateCourtAverageRating(court);

        Court savedCourt = courtRepository.save(court);

        return getReviewFromCourt(reviewId, savedCourt)
                .orElseThrow(() -> new RuntimeException("Error retrieving created review"));
    }

    @Override
    public Page<Review> listReviews(String courtId, Pageable pageable) {
        Court court = getCourtOrThrow(courtId);

        List<Review> reviews = court.getReviews();

        Sort sort = pageable.getSort();

        if (sort.isSorted()) {
            Sort.Order order = sort.iterator().next();
            String property = order.getProperty();
            boolean isAscending = order.getDirection().isAscending();

            Comparator<Review> comparator =
                    switch (property) {
                        case "rating" -> Comparator.comparing(Review::getRating);
                        default -> Comparator.comparing(Review::getDatePosted);
                    };

            reviews.sort(isAscending ? comparator : comparator.reversed());
        } else {
            reviews.sort(Comparator.comparing(Review::getDatePosted).reversed());
        }

        int start = (int) pageable.getOffset();

        if (start >= reviews.size()) {
            return new PageImpl<>(Collections.emptyList(), pageable, reviews.size());
        }

        int end = Math.min((start + pageable.getPageSize()), reviews.size());

        return new PageImpl<>(reviews.subList(start, end), pageable, reviews.size());
    }

    @Override
    public Optional<Review> getReview(String courtId, String reviewId) {
        Court court = getCourtOrThrow(courtId);

        return getReviewFromCourt(reviewId, court);
    }

    @Override
    public Review updateReview(
            User author, String courtId, String reviewId, ReviewCreateUpdateRequest review) {
        Court court = getCourtOrThrow(courtId);

        String authorId = author.getId();

        Review existingReview =
                getReviewFromCourt(reviewId, court)
                        .orElseThrow(() -> new ReviewNotAllowedException("Review does not exist"));

        if (!authorId.equals(existingReview.getWrittenBy().getId())) {
            throw new ReviewNotAllowedException("Cannot update another user's review");
        }

        if (LocalDateTime.now().isAfter(existingReview.getDatePosted().plusHours(48))) {
            throw new ReviewNotAllowedException("Review can no longer be updated");
        }

        existingReview.setContent(review.getContent());
        existingReview.setRating(review.getRating());
        existingReview.setLastEdited(LocalDateTime.now());

        existingReview.setPhotos(
                review.getPhotoIds().stream()
                        .map(
                                photoId ->
                                        Photo.builder()
                                                .url(photoId)
                                                .uploadDate(LocalDateTime.now())
                                                .build())
                        .toList());

        List<Review> updatedReviews =
                court.getReviews().stream()
                        .filter(r -> !reviewId.equals(r.getId()))
                        .collect(Collectors.toList());
        updatedReviews.add(existingReview);

        court.setReviews(updatedReviews);

        updateCourtAverageRating(court);

        courtRepository.save(court);

        return existingReview;
    }

    @Override
    public void deleteReview(User author, String courtId, String reviewId) {
        Court court = getCourtOrThrow(courtId);

        String authorId = author.getId();

        Review existingReview =
                getReviewFromCourt(reviewId, court)
                        .orElseThrow(() -> new ReviewNotAllowedException("Review does not exist"));

        if (!authorId.equals(existingReview.getWrittenBy().getId())) {
            throw new ReviewNotAllowedException("Cannot delete another user's review");
        }

        List<Review> filteredReviews =
                court.getReviews().stream().filter(r -> !reviewId.equals(r.getId())).toList();

        court.setReviews(filteredReviews);

        updateCourtAverageRating(court);

        courtRepository.save(court);
    }

    private Court getCourtOrThrow(String courtId) {
        return courtRepository
                .findById(courtId)
                .orElseThrow(
                        () -> new CourtNotFoundException("Court with id not found: " + courtId));
    }

    private void updateCourtAverageRating(Court court) {
        List<Review> reviews = court.getReviews();

        if (reviews.isEmpty()) {
            court.setAverageRating(0.0f);
        } else {
            double averageRating =
                    reviews.stream().mapToDouble(Review::getRating).average().orElse(0.0);
            court.setAverageRating((float) averageRating);
        }
    }
}
