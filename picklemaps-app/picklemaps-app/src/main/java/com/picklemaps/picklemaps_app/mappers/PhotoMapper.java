package com.picklemaps.picklemaps_app.mappers;

import com.picklemaps.picklemaps_app.domain.dtos.PhotoDto;
import com.picklemaps.picklemaps_app.domain.entities.Photo;

import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PhotoMapper {

    PhotoDto toDto(Photo photo);
}
