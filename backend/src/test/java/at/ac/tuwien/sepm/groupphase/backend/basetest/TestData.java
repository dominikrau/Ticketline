package at.ac.tuwien.sepm.groupphase.backend.basetest;


import at.ac.tuwien.sepm.groupphase.backend.domain.model.*;
import at.ac.tuwien.sepm.groupphase.backend.repository.entity.AddressEntity;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public interface TestData {

    Long ID = 1L;
    String TEST_NEWS_TITLE = "Title";
    String TEST_NEWS_SUMMARY = "Summary";
    String TEST_NEWS_TEXT = "TestMessageText";
    LocalDateTime TEST_NEWS_PUBLISHED_AT =
        LocalDateTime.of(2019, 11, 13, 12, 15, 0, 0);
    String TEST_NEWS_IMAGEURL = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAIAAAACCAYAAABytg0kAAAAEklEQVR42mNkYPhfzwAEjDAGAB6FAv/WXPi2AAAAAElFTkSuQmCC";


    String BASE_URI = "/api/v1";
    String MESSAGE_BASE_URI = BASE_URI + "/messages";
    String USER_PROFILE_BASE_URI = BASE_URI + "/users/profile";
    String PASSWORD_UPDATE_URI = BASE_URI + "/users/password/update";
    String REGISTRATION_BASE_URL = BASE_URI + "/registration";
    String EVENTS_BASE_URL = BASE_URI + "/events";
    String SEARCH_BASE_URL = EVENTS_BASE_URL + "/search";
    String SEATINGCHARTS_BASE_URL = BASE_URI + "/seatingCharts";
    String HALL_BASE_URL = BASE_URI + "/halls";
    String SHOW_BASE_URL = BASE_URI + "/shows";

    String ADMIN_USER = "admin@email.com";
    List<String> ADMIN_ROLES = new ArrayList<>() {
        {
            add("ROLE_ADMIN");
            add("ROLE_USER");
        }
    };
    String DEFAULT_USER = "admin@email.com";
    List<String> USER_ROLES = new ArrayList<>() {
        {
            add("ROLE_USER");
        }
    };
    String DEFAULT_USER_PASSWORD = "password";

    String TEST_VENUE_NAME = "Sherlock's Office";

    String TEST_ADDRESS_COUNTRY = "Great Britain";
    String TEST_ADDRESS_POSTCODE = "NW1 6XE";
    String TEST_ADDRESS_CITY = "London";
    String TEST_ADDRESS_STREET = "Baker Street 212b";
    LocalDateTime TEST_ADDRESS_CREATED_AT =
        LocalDateTime.of(2019, 11, 13, 12, 15, 0, 0);

    String TEST_ARTIST_FIRST_NAME = "Armando Christian";
    String TEST_ARTIST_LAST_NAME = "Pérez";
    String TEST_ARTIST_PSEUDONYM = "Pitbull";
    String TEST_ARTIST_ROSSI = "Rossi";
    LocalDateTime TEST_ARTIST_CREATED_AT_LOCAL =
        LocalDateTime.of(2019, 11, 13, 12, 15, 0, 0);
    Instant TEST_ARTIST_CREATED_AT = Instant.now();
    String TEST_USER_EMAIL = "test@test.at";


    // Show
    LocalDateTime TEST_SHOW_CREATED_AT = LocalDateTime.of(2021, 10, 10, 10, 10, 10, 10);
    LocalDateTime TEST_SHOW_START_TIME = LocalDateTime.of(2021, 11, 11, 10, 10, 10, 10);
    LocalDateTime TEST_SHOW_END_TIME = LocalDateTime.of(2021, 11, 11, 12, 0, 10, 10);


    static ApplicationUser buildApplicationUserWith(String email) {
        return ApplicationUser.builder()
            .firstName("Testus")
            .lastName("Maximus")
            .gender(Gender.OTHER)
            .dateOfBirth(LocalDate.of(1999, 1, 1))
            .emailAddress(email)
            .password(DEFAULT_USER_PASSWORD)
            .role("ROLE_ADMIN")
            .address(Address.builder()
                .country("Austria")
                .city("Vienna")
                .postalCode("1040")
                .street("Karlsplatz 3")
                .build())
            .build();
    }

    static AddressEntity buildAddressEntity() {
        return AddressEntity.builder()
            .country("Austria")
            .city("Vienna")
            .postalCode("1040")
            .street("Karlsplatz 3")
            .build();
    }

    static Venue buildVenue() {
        return Venue.builder()
            .name(TEST_VENUE_NAME)
            .address(Address.builder()
                .country(TEST_ADDRESS_COUNTRY)
                .city(TEST_ADDRESS_CITY)
                .postalCode(TEST_ADDRESS_POSTCODE)
                .street(TEST_ADDRESS_STREET)
                .build())
            .build();
    }

    static Hall buildHall(Integer width, Integer height) {
        return Hall.builder()
            .name("Hall A")
            .width(width)
            .height(height)
            .venue(buildVenue())
            .build();
    }

    static SeatingChart buildSeatingChart() {
        return SeatingChart.builder()
            .name("Plan a")
            .hall(buildHall(200, 400))
            .stage(buildStage(50, 0, 100, 20))
            .sectors(buildSectors())
            .build();
    }

    static List<Sector> buildSectors() {
        List<Sector> sectors = new ArrayList<>();
        sectors.add(buildSittingSector(30, 30, 140, 40));
        sectors.add(buildStandingSector(10, 250, 180, 100, 300));
        return sectors;
    }

    static Seat buildSeat(Integer x, Integer y) {
        return Seat.builder()
            .x(x)
            .y(y)
            .build();
    }

    static List<Seat> buildSeats(Integer x, Integer y, Integer width, Integer heigth) {
        List<Seat> seats = new ArrayList<>();
        for (int i = x; i < x + width; i++) {
            for (int j = y; j < y + heigth; j++) {
                seats.add(buildSeat(i, j));
            }

        }
        return seats;
    }

    static SittingSector buildSittingSector(Integer x, Integer y, Integer width, Integer heigth) {
        return SittingSector.builder()
            .name("Category 1")
            .color("#0099ab")
            .seats(buildSeats(x, y, width, heigth))
            .build();
    }

    static StandingSector buildStandingSector(Integer x, Integer y, Integer width, Integer height, Integer capacity) {
        return StandingSector.builder()
            .name("Standing Sector")
            .color("#ff7788")
            .x(x)
            .y(y)
            .width(width)
            .height(height)
            .capacity(capacity)
            .build();
    }

    static Stage buildStage(Integer x, Integer y, Integer widht, Integer height) {
        return Stage.builder()
            .x(x)
            .y(y)
            .width(widht)
            .height(height)
            .build();
    }

    String TEST_EVENT_NAME = "KOLA MIT ICE TOUR";
    String TEST_EVENT_DESCRIPTION = "Sheesh";
    EventType TEST_EVENT_TYPE = EventType.CULTURE;
    String TEST_EVENT_TYPE_STRING = "CULTURE";
    String TEST_EVENT_IMAGEURL = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAIAAAACCAYAAABytg0kAAAAEklEQVR42mNkYPhfzwAEjDAGAB6FAv/WXPi2AAAAAElFTkSuQmCC";
    String IMAGE_UPLOAD_DIRECTORY = "./public/uploads/images/";
}
