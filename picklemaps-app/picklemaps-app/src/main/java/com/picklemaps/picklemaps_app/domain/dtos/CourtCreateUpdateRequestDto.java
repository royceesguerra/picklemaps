package com.picklemaps.picklemaps_app.domain.dtos;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CourtCreateUpdateRequestDto {

    @NotBlank(message = "Court name is required")
    private String name;

    @NotBlank(message = "Court type is required")
    private String courtType;

    @NotBlank(message = "Surface type is required")
    private String surfaceType;

    @NotBlank(message = "Contact information is required")
    private String contactInformation;

    @Valid private AddressDto address;

    @Valid private OperatingHoursDto operatingHours;

    @Size(min = 1, message = "At least one photo ID is required")
    private List<String> photoIds;
}
