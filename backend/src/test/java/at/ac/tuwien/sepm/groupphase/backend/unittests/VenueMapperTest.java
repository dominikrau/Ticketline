package at.ac.tuwien.sepm.groupphase.backend.unittests;

import at.ac.tuwien.sepm.groupphase.backend.domain.model.Address;
import at.ac.tuwien.sepm.groupphase.backend.domain.model.Venue;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.AddressDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.VenueDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.VenueMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static at.ac.tuwien.sepm.groupphase.backend.basetest.TestData.*;
import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertAll;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
class VenueMapperTest {

    @Autowired
    private VenueMapper venueMapper;

    @Test
    void toDomainVenue() {
        VenueDto venueDto = buildVenueDto();
        Venue mapped = venueMapper.venueDtoToVenue(venueDto);
        venueEquals(venueDto, mapped);
    }

    @Test
    void toDto() {
        Venue venue = Venue.builder()
            .name(TEST_VENUE_NAME)
            .address(Address.builder()
                .country(TEST_ADDRESS_COUNTRY)
                .city(TEST_ADDRESS_CITY)
                .postalCode(TEST_ADDRESS_POSTCODE)
                .street(TEST_ADDRESS_STREET)
                .build())
            .build();
        VenueDto mapped = venueMapper.venueToVenueDto(venue);
        assertAll(
            () -> assertEquals(venue.getName(), mapped.getName()),
            () -> assertEquals(venue.getVenueId(),mapped.getVenueId()),
            () -> assertEquals(venue.getAddress().getCountry(),mapped.getAddress().getCountry()),
            () -> assertEquals(venue.getAddress().getCity(),mapped.getAddress().getCity()),
            () -> assertEquals(venue.getAddress().getStreet(),mapped.getAddress().getStreet())
        );
    }

    void venueEquals(VenueDto dto, Venue domain) {
        assertAll(
            () -> assertEquals(dto.getName(), domain.getName()),
            () -> assertEquals(dto.getVenueId(),domain.getVenueId()),
            () -> assertEquals(dto.getAddress().getCountry(),domain.getAddress().getCountry()),
            () -> assertEquals(dto.getAddress().getCity(),domain.getAddress().getCity()),
            () -> assertEquals(dto.getAddress().getStreet(),domain.getAddress().getStreet())
        );
    }

    VenueDto buildVenueDto() {
        return VenueDto.builder()
            .name(TEST_VENUE_NAME)
            .address(AddressDto.builder()
                .country(TEST_ADDRESS_COUNTRY)
                .city(TEST_ADDRESS_CITY)
                .street(TEST_ADDRESS_STREET)
                .postalCode(TEST_ADDRESS_POSTCODE)
                .build())
            .build();

    }

}