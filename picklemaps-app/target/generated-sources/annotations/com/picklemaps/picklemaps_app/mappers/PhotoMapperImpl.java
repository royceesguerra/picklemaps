package com.picklemaps.picklemaps_app.mappers;

import com.picklemaps.picklemaps_app.domain.dtos.PhotoDto;
import com.picklemaps.picklemaps_app.domain.entities.Photo;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-07-07T23:28:58+0800",
    comments = "version: 1.6.3, compiler: javac, environment: Java 17.0.9 (Amazon.com Inc.)"
)
@Component
public class PhotoMapperImpl implements PhotoMapper {

    @Override
    public PhotoDto toDto(Photo photo) {
        if ( photo == null ) {
            return null;
        }

        PhotoDto.PhotoDtoBuilder photoDto = PhotoDto.builder();

        photoDto.url( photo.getUrl() );
        photoDto.uploadDate( photo.getUploadDate() );

        return photoDto.build();
    }
}
