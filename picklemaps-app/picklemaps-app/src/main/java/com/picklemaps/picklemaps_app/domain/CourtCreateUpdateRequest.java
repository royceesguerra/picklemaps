package com.picklemaps.picklemaps_app.domain;

import com.picklemaps.picklemaps_app.domain.entities.Address;
import com.picklemaps.picklemaps_app.domain.entities.OperatingHours;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CourtCreateUpdateRequest {

    private String name;

    private String courtType;

    private String surfaceType;

    private String contactInformation;

    private Address address;

    private OperatingHours operatingHours;

    private List<String> photoIds;
}
