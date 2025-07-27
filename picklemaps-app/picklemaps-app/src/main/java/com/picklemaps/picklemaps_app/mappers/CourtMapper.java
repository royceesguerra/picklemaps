package com.picklemaps.picklemaps_app.mappers;

import com.picklemaps.picklemaps_app.domain.CourtCreateUpdateRequest;
import com.picklemaps.picklemaps_app.domain.dtos.CourtCreateUpdateRequestDto;
import com.picklemaps.picklemaps_app.domain.dtos.CourtDto;
import com.picklemaps.picklemaps_app.domain.dtos.CourtSummaryDto;
import com.picklemaps.picklemaps_app.domain.dtos.GeoPointDto;
import com.picklemaps.picklemaps_app.domain.entities.Court;
import com.picklemaps.picklemaps_app.domain.entities.Review;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface CourtMapper {

    CourtCreateUpdateRequest toCourtCreateUpdateRequest(CourtCreateUpdateRequestDto dto);

    @Mapping(source = "reviews", target = "totalReviews", qualifiedByName = "populateTotalReviews")
    CourtDto toCourtDto(Court court);

    @Mapping(source = "reviews", target = "totalReviews", qualifiedByName = "populateTotalReviews")
    CourtSummaryDto toSummaryDto(Court court);

    @Named("populateTotalReviews")
    default Integer populateTotalReviews(List<Review> reviews) {
        return (reviews == null) ? 0 : reviews.size();
    }

    @Mapping(target = "latitude", expression = "java(geoPoint.getLat())")
    @Mapping(target = "longitude", expression = "java(geoPoint.getLon())")
    GeoPointDto toGeoPointDto(GeoPoint geoPoint);
}
