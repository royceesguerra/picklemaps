package com.picklemaps.picklemaps_app.manual;

import com.picklemaps.picklemaps_app.domain.CourtCreateUpdateRequest;
import com.picklemaps.picklemaps_app.domain.entities.Address;
import com.picklemaps.picklemaps_app.domain.entities.OperatingHours;
import com.picklemaps.picklemaps_app.domain.entities.Photo;
import com.picklemaps.picklemaps_app.domain.entities.TimeRange;
import com.picklemaps.picklemaps_app.services.CourtService;
import com.picklemaps.picklemaps_app.services.PhotoService;
import com.picklemaps.picklemaps_app.services.impl.RandomNCRPlusGeoLocationService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@ActiveProfiles("test")
@SpringBootTest
public class CourtDataLoaderTest {

    @Autowired private CourtService courtService;

    @Autowired private RandomNCRPlusGeoLocationService geoLocationService;

    @Autowired private PhotoService photoService;

    @Autowired private ResourceLoader resourceLoader;

    @Test
    @Rollback(false)
    public void createSampleCourts() throws Exception {
        List<CourtCreateUpdateRequest> courts = createCourtData();

        courts.forEach(
                court -> {
                    String fileName = court.getPhotoIds().get(0);
                    Resource resource =
                            resourceLoader.getResource("classpath:testdata/" + fileName);
                    MultipartFile multipartFile = null;
                    try {
                        multipartFile =
                                new MockMultipartFile(
                                        "file", // parameter name
                                        fileName, // original filename
                                        MediaType.IMAGE_PNG_VALUE,
                                        resource.getInputStream());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }

                    Photo uploadedPhoto = photoService.uploadPhoto(multipartFile);
                    court.setPhotoIds(List.of(uploadedPhoto.getUrl()));
                    courtService.createCourt(court);

                    System.out.println("Created court: " + court.getName());
                });
    }

    private List<CourtCreateUpdateRequest> createCourtData() {
        return Arrays.asList(
                createCourt(
                        "Athlete Central",
                        "Indoor",
                        "Hard",
                        "0917 627 8888",
                        createAddress(
                                "36", "Meralco Avenue", "Ugong", "Pasig", "Metro Manila", "1604"),
                        createStandardOperatingHours("08:00", "22:00", "08:00", "22:00"),
                        "athlete-central.jpg"),
                createCourt(
                        "Dink and Shot",
                        "Indoor",
                        "Hard",
                        "0917 123 4567",
                        createAddress(
                                "8", "Cambridge Street", "Cubao", "Quezon City", "Metro Manila", "1109"),
                        createStandardOperatingHours("09:00", "21:00", "09:00", "21:00"),
                        "dink-and-shot.jpg"),
                createCourt(
                        "Quest Adventure Camp",
                        "Outdoor",
                        "Concrete",
                        "0908 863 9765",
                        createAddress(
                                "1", "Ma. Lourdes Street", "Sitio Buhangin", "Antipolo", "Rizal", "1870"),
                        createStandardOperatingHours("07:00", "18:00", "07:00", "18:00"),
                        "quest.png"),
                createCourt(
                        "Neopolitan Brittany Pickleball Club",
                        "Indoor",
                        "Wood",
                        "0917 555 1212",
                        createAddress("Neopolitan Ave", "Neopolitan Brittany Subd.", "Pasong Putik Proper", "Quezon City", "Metro Manila", "1118"),
                        createStandardOperatingHours("07:00", "22:00", "07:00", "22:00"),
                        "neopolitan-brittany.jpg"
                ),
                createCourt(
                        "The Pickle Yard",
                        "Indoor",
                        "Hard",
                        "0998 123 4567",
                        createAddress("Governor's Drive", "AON Warehouse", "Sampaloc Uno", "Dasmariñas", "Cavite", "4114"),
                        createStandardOperatingHours("10:00", "22:00", "10:00", "22:00"),
                        "pickle-yard.jpg"
                ),
                createCourt(
                        "Activate One Ayala",
                        "Indoor",
                        "Hard",
                        "0961 888 9988",
                        createAddress("One Ayala Center", "Ayala Ave cor EDSA", "San Lorenzo", "Makati", "Metro Manila", "1200"),
                        createStandardOperatingHours("09:00", "21:00", "09:00", "21:00"),
                        "activate-one.jpg"
                ),
                createCourt(
                        "Zone Sports Center",
                        "Indoor",
                        "Wood",
                        "0922 345 6789",
                        createAddress("7224 Sen. Gil Puyat Ave", "cor Malugay St.", "Bel-Air", "Makati", "Metro Manila", "1209"),
                        createStandardOperatingHours("09:30", "13:00", "09:30", "13:00"),
                        "zone-sports-center.jpg"
                ),
                createCourt(
                        "Pickleball Hub Tiendesitas",
                        "Indoor/Outdoor",
                        "Hard",
                        "0906 234 5678",
                        createAddress("Level 2 Tiendesitas", "Ortigas East", "Ugong", "Pasig", "Metro Manila", "1604"),
                        createStandardOperatingHours("10:00", "21:00", "10:00", "21:00"),
                        "pickleball-hub.jpg"
                ),
                createCourt(
                        "Pickleball Hub Extension",
                        "Indoor/Outdoor",
                        "Hard",
                        "0915 444 3333",
                        createAddress("Tiendesitas Extension", "Ortigas East", "Ugong", "Pasig", "Metro Manila", "1604"),
                        createStandardOperatingHours("10:00", "21:00", "10:00", "21:00"),
                        "pickleball-hub-extension.jpg"
                ),
                createCourt(
                        "Paloo SM City Marikina",
                        "Outdoor",
                        "Hard",
                        "0932 765 4321",
                        createAddress("SM City Marikina Roofdeck", "Marcos Highway", "Calumpang", "Marikina", "Metro Manila", "1801"),
                        createStandardOperatingHours("16:00", "22:00", "16:00", "22:00"),
                        "paloo-sm-marikina.jpg"
                ),
                createCourt(
                        "Paloo SM City Sta. Mesa",
                        "Outdoor",
                        "Hard",
                        "0918 246 8100",
                        createAddress("SM City Sta. Mesa Roofdeck", "Ramon Magsaysay Blvd", "Sta. Mesa", "Quezon City", "Metro Manila", "1113"),
                        createStandardOperatingHours("16:00", "22:00", "16:00", "22:00"),
                        "paloo-sm-sta-mesa.jpg"
                )
        );
    }

    private CourtCreateUpdateRequest createCourt(
            String name,
            String courtType,
            String surfaceType,
            String contactInformation,
            Address address,
            OperatingHours operatingHours,
            String photoId) {
        return CourtCreateUpdateRequest.builder()
                .name(name)
                .courtType(courtType)
                .surfaceType(surfaceType)
                .contactInformation(contactInformation)
                .address(address)
                .operatingHours(operatingHours)
                .photoIds(List.of(photoId))
                .build();
    }

    private Address createAddress(
            String streetNumber,
            String streetName,
            String barangay,
            String city,
            String province,
            String postalCode) {
        Address address = new Address();
        address.setStreetNumber(streetNumber);
        address.setStreetName(streetName);
        address.setBarangay(barangay);
        address.setCity(city);
        address.setProvince(province);
        address.setPostalCode(postalCode);
        return address;
    }

    private OperatingHours createStandardOperatingHours(
            String weekdayOpen, String weekdayClose, String weekendOpen, String weekendClose) {
        TimeRange weekday = new TimeRange();
        weekday.setOpenTime(weekdayOpen);
        weekday.setCloseTime(weekdayClose);

        TimeRange weekend = new TimeRange();
        weekend.setOpenTime(weekendOpen);
        weekend.setCloseTime(weekendClose);

        OperatingHours hours = new OperatingHours();
        hours.setMonday(weekday);
        hours.setTuesday(weekday);
        hours.setWednesday(weekday);
        hours.setThursday(weekday);
        hours.setFriday(weekday);
        hours.setSaturday(weekend);
        hours.setSunday(weekend);

        return hours;
    }
}
