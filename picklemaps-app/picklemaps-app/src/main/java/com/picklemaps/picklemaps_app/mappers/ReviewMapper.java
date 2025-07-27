package com.picklemaps.picklemaps_app.mappers;

import com.picklemaps.picklemaps_app.domain.ReviewCreateUpdateRequest;
import com.picklemaps.picklemaps_app.domain.dtos.ReviewCreateUpdateRequestDto;
import com.picklemaps.picklemaps_app.domain.dtos.ReviewDto;
import com.picklemaps.picklemaps_app.domain.entities.Review;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ReviewMapper {

    ReviewCreateUpdateRequest toReviewCreateUpdateRequest(ReviewCreateUpdateRequestDto dto);

    ReviewDto toDto(Review review);
}
