package com.picklemaps.picklemaps_app.mappers;

import com.picklemaps.picklemaps_app.domain.CourtCreateUpdateRequest;
import com.picklemaps.picklemaps_app.domain.dtos.AddressDto;
import com.picklemaps.picklemaps_app.domain.dtos.CourtCreateUpdateRequestDto;
import com.picklemaps.picklemaps_app.domain.dtos.CourtDto;
import com.picklemaps.picklemaps_app.domain.dtos.CourtSummaryDto;
import com.picklemaps.picklemaps_app.domain.dtos.GeoPointDto;
import com.picklemaps.picklemaps_app.domain.dtos.OperatingHoursDto;
import com.picklemaps.picklemaps_app.domain.dtos.PhotoDto;
import com.picklemaps.picklemaps_app.domain.dtos.ReviewDto;
import com.picklemaps.picklemaps_app.domain.dtos.TimeRangeDto;
import com.picklemaps.picklemaps_app.domain.dtos.UserDto;
import com.picklemaps.picklemaps_app.domain.entities.Address;
import com.picklemaps.picklemaps_app.domain.entities.Court;
import com.picklemaps.picklemaps_app.domain.entities.OperatingHours;
import com.picklemaps.picklemaps_app.domain.entities.Photo;
import com.picklemaps.picklemaps_app.domain.entities.Review;
import com.picklemaps.picklemaps_app.domain.entities.TimeRange;
import com.picklemaps.picklemaps_app.domain.entities.User;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.processing.Generated;
import org.springframework.data.elasticsearch.core.geo.GeoPoint;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-07-07T23:42:11+0800",
    comments = "version: 1.6.3, compiler: javac, environment: Java 17.0.9 (Amazon.com Inc.)"
)
@Component
public class CourtMapperImpl implements CourtMapper {

    @Override
    public CourtCreateUpdateRequest toCourtCreateUpdateRequest(CourtCreateUpdateRequestDto dto) {
        if ( dto == null ) {
            return null;
        }

        CourtCreateUpdateRequest.CourtCreateUpdateRequestBuilder courtCreateUpdateRequest = CourtCreateUpdateRequest.builder();

        courtCreateUpdateRequest.name( dto.getName() );
        courtCreateUpdateRequest.courtType( dto.getCourtType() );
        courtCreateUpdateRequest.surfaceType( dto.getSurfaceType() );
        courtCreateUpdateRequest.contactInformation( dto.getContactInformation() );
        courtCreateUpdateRequest.address( addressDtoToAddress( dto.getAddress() ) );
        courtCreateUpdateRequest.operatingHours( operatingHoursDtoToOperatingHours( dto.getOperatingHours() ) );
        List<String> list = dto.getPhotoIds();
        if ( list != null ) {
            courtCreateUpdateRequest.photoIds( new ArrayList<String>( list ) );
        }

        return courtCreateUpdateRequest.build();
    }

    @Override
    public CourtDto toCourtDto(Court court) {
        if ( court == null ) {
            return null;
        }

        CourtDto.CourtDtoBuilder courtDto = CourtDto.builder();

        courtDto.totalReviews( populateTotalReviews( court.getReviews() ) );
        courtDto.id( court.getId() );
        courtDto.name( court.getName() );
        courtDto.courtType( court.getCourtType() );
        courtDto.surfaceType( court.getSurfaceType() );
        courtDto.contactInformation( court.getContactInformation() );
        courtDto.averageRating( court.getAverageRating() );
        courtDto.geoLocation( toGeoPointDto( court.getGeoLocation() ) );
        courtDto.address( addressToAddressDto( court.getAddress() ) );
        courtDto.operatingHours( operatingHoursToOperatingHoursDto( court.getOperatingHours() ) );
        courtDto.photos( photoListToPhotoDtoList( court.getPhotos() ) );
        courtDto.reviews( reviewListToReviewDtoList( court.getReviews() ) );
        courtDto.createdBy( userToUserDto( court.getCreatedBy() ) );

        return courtDto.build();
    }

    @Override
    public CourtSummaryDto toSummaryDto(Court court) {
        if ( court == null ) {
            return null;
        }

        CourtSummaryDto.CourtSummaryDtoBuilder courtSummaryDto = CourtSummaryDto.builder();

        courtSummaryDto.totalReviews( populateTotalReviews( court.getReviews() ) );
        courtSummaryDto.id( court.getId() );
        courtSummaryDto.name( court.getName() );
        courtSummaryDto.courtType( court.getCourtType() );
        courtSummaryDto.surfaceType( court.getSurfaceType() );
        courtSummaryDto.averageRating( court.getAverageRating() );
        courtSummaryDto.address( addressToAddressDto( court.getAddress() ) );
        courtSummaryDto.photos( photoListToPhotoDtoList( court.getPhotos() ) );

        return courtSummaryDto.build();
    }

    @Override
    public GeoPointDto toGeoPointDto(GeoPoint geoPoint) {
        if ( geoPoint == null ) {
            return null;
        }

        GeoPointDto.GeoPointDtoBuilder geoPointDto = GeoPointDto.builder();

        geoPointDto.latitude( geoPoint.getLat() );
        geoPointDto.longitude( geoPoint.getLon() );

        return geoPointDto.build();
    }

    protected Address addressDtoToAddress(AddressDto addressDto) {
        if ( addressDto == null ) {
            return null;
        }

        Address.AddressBuilder address = Address.builder();

        address.streetNumber( addressDto.getStreetNumber() );
        address.streetName( addressDto.getStreetName() );
        address.barangay( addressDto.getBarangay() );
        address.city( addressDto.getCity() );
        address.province( addressDto.getProvince() );
        address.postalCode( addressDto.getPostalCode() );

        return address.build();
    }

    protected TimeRange timeRangeDtoToTimeRange(TimeRangeDto timeRangeDto) {
        if ( timeRangeDto == null ) {
            return null;
        }

        TimeRange.TimeRangeBuilder timeRange = TimeRange.builder();

        timeRange.openTime( timeRangeDto.getOpenTime() );
        timeRange.closeTime( timeRangeDto.getCloseTime() );

        return timeRange.build();
    }

    protected OperatingHours operatingHoursDtoToOperatingHours(OperatingHoursDto operatingHoursDto) {
        if ( operatingHoursDto == null ) {
            return null;
        }

        OperatingHours.OperatingHoursBuilder operatingHours = OperatingHours.builder();

        operatingHours.monday( timeRangeDtoToTimeRange( operatingHoursDto.getMonday() ) );
        operatingHours.tuesday( timeRangeDtoToTimeRange( operatingHoursDto.getTuesday() ) );
        operatingHours.wednesday( timeRangeDtoToTimeRange( operatingHoursDto.getWednesday() ) );
        operatingHours.thursday( timeRangeDtoToTimeRange( operatingHoursDto.getThursday() ) );
        operatingHours.friday( timeRangeDtoToTimeRange( operatingHoursDto.getFriday() ) );
        operatingHours.saturday( timeRangeDtoToTimeRange( operatingHoursDto.getSaturday() ) );
        operatingHours.sunday( timeRangeDtoToTimeRange( operatingHoursDto.getSunday() ) );

        return operatingHours.build();
    }

    protected AddressDto addressToAddressDto(Address address) {
        if ( address == null ) {
            return null;
        }

        AddressDto.AddressDtoBuilder addressDto = AddressDto.builder();

        addressDto.streetNumber( address.getStreetNumber() );
        addressDto.streetName( address.getStreetName() );
        addressDto.barangay( address.getBarangay() );
        addressDto.city( address.getCity() );
        addressDto.province( address.getProvince() );
        addressDto.postalCode( address.getPostalCode() );

        return addressDto.build();
    }

    protected TimeRangeDto timeRangeToTimeRangeDto(TimeRange timeRange) {
        if ( timeRange == null ) {
            return null;
        }

        TimeRangeDto.TimeRangeDtoBuilder timeRangeDto = TimeRangeDto.builder();

        timeRangeDto.openTime( timeRange.getOpenTime() );
        timeRangeDto.closeTime( timeRange.getCloseTime() );

        return timeRangeDto.build();
    }

    protected OperatingHoursDto operatingHoursToOperatingHoursDto(OperatingHours operatingHours) {
        if ( operatingHours == null ) {
            return null;
        }

        OperatingHoursDto.OperatingHoursDtoBuilder operatingHoursDto = OperatingHoursDto.builder();

        operatingHoursDto.monday( timeRangeToTimeRangeDto( operatingHours.getMonday() ) );
        operatingHoursDto.tuesday( timeRangeToTimeRangeDto( operatingHours.getTuesday() ) );
        operatingHoursDto.wednesday( timeRangeToTimeRangeDto( operatingHours.getWednesday() ) );
        operatingHoursDto.thursday( timeRangeToTimeRangeDto( operatingHours.getThursday() ) );
        operatingHoursDto.friday( timeRangeToTimeRangeDto( operatingHours.getFriday() ) );
        operatingHoursDto.saturday( timeRangeToTimeRangeDto( operatingHours.getSaturday() ) );
        operatingHoursDto.sunday( timeRangeToTimeRangeDto( operatingHours.getSunday() ) );

        return operatingHoursDto.build();
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

    protected ReviewDto reviewToReviewDto(Review review) {
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

    protected List<ReviewDto> reviewListToReviewDtoList(List<Review> list) {
        if ( list == null ) {
            return null;
        }

        List<ReviewDto> list1 = new ArrayList<ReviewDto>( list.size() );
        for ( Review review : list ) {
            list1.add( reviewToReviewDto( review ) );
        }

        return list1;
    }
}
