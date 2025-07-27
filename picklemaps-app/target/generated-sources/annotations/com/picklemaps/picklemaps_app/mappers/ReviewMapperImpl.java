package com.picklemaps.picklemaps_app.mappers;

import com.picklemaps.picklemaps_app.domain.ReviewCreateUpdateRequest;
import com.picklemaps.picklemaps_app.domain.dtos.PhotoDto;
import com.picklemaps.picklemaps_app.domain.dtos.ReviewCreateUpdateRequestDto;
import com.picklemaps.picklemaps_app.domain.dtos.ReviewDto;
import com.picklemaps.picklemaps_app.domain.dtos.UserDto;
import com.picklemaps.picklemaps_app.domain.entities.Photo;
import com.picklemaps.picklemaps_app.domain.entities.Review;
import com.picklemaps.picklemaps_app.domain.entities.User;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-07-07T23:28:58+0800",
    comments = "version: 1.6.3, compiler: javac, environment: Java 17.0.9 (Amazon.com Inc.)"
)
@Component
public class ReviewMapperImpl implements ReviewMapper {

    @Override
    public ReviewCreateUpdateRequest toReviewCreateUpdateRequest(ReviewCreateUpdateRequestDto dto) {
        if ( dto == null ) {
            return null;
        }

        ReviewCreateUpdateRequest.ReviewCreateUpdateRequestBuilder reviewCreateUpdateRequest = ReviewCreateUpdateRequest.builder();

        reviewCreateUpdateRequest.content( dto.getContent() );
        reviewCreateUpdateRequest.rating( dto.getRating() );
        List<String> list = dto.getPhotoIds();
        if ( list != null ) {
            reviewCreateUpdateRequest.photoIds( new ArrayList<String>( list ) );
        }

        return reviewCreateUpdateRequest.build();
    }

    @Override
    public ReviewDto toDto(Review review) {
        if ( review == null ) {
            return null;
        }

        ReviewDto.ReviewDtoBuilder reviewDto = ReviewDto.builder();

        reviewDto.id( review.getId() );
        reviewDto.content( review.getContent() );
        reviewDto.rating( review.getRating() );
        reviewDto.datePosted( review.getDatePosted() );
        reviewDto.lastEdited( review.getLastEdited() );
        reviewDto.photos( photoListToPhotoDtoList( review.getPhotos() ) );
        reviewDto.writtenBy( userToUserDto( review.getWrittenBy() ) );

        return reviewDto.build();
    }

    protected PhotoDto photoToPhotoDto(Photo photo) {
        if ( photo == null ) {
            return null;
        }

        PhotoDto.PhotoDtoBuilder photoDto = PhotoDto.builder();

        photoDto.url( photo.getUrl() );
        photoDto.uploadDate( photo.getUploadDate() );

        return photoDto.build();
    }

    protected List<PhotoDto> photoListToPhotoDtoList(List<Photo> list) {
        if ( list == null ) {
            return null;
        }

        List<PhotoDto> list1 = new ArrayList<PhotoDto>( list.size() );
        for ( Photo photo : list ) {
            list1.add( photoToPhotoDto( photo ) );
        }

        return list1;
    }

    protected UserDto userToUserDto(User user) {
        if ( user == null ) {
            return null;
        }

        UserDto.UserDtoBuilder userDto = UserDto.builder();

        userDto.id( user.getId() );
        userDto.username( user.getUsername() );
        userDto.givenName( user.getGivenName() );
        userDto.familyName( user.getFamilyName() );

        return userDto.build();
    }
}
