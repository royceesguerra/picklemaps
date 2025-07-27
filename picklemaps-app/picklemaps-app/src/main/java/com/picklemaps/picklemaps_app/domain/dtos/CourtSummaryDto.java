package com.picklemaps.picklemaps_app.domain.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CourtSummaryDto {

    private String id;

    private String name;

    private String courtType;

    private String surfaceType;

    private Float averageRating;

    private Integer totalReviews;

    private AddressDto address;

    private List<PhotoDto> photos;
}
