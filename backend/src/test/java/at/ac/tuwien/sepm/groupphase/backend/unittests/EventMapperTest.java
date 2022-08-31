package at.ac.tuwien.sepm.groupphase.backend.unittests;

import at.ac.tuwien.sepm.groupphase.backend.basetest.TestData;
import at.ac.tuwien.sepm.groupphase.backend.domain.model.Event;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.*;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.EventMapper;
import at.ac.tuwien.sepm.groupphase.backend.repository.EventJpaRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.mapping.EventEntityMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertAll;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
public class EventMapperTest implements TestData {

    @Autowired
    private EventMapper eventMapper;

    @Autowired
    EventEntityMapper eventEntityMapper;

    @Autowired
    EventJpaRepository eventJpaRepository;

    @Test
    void toDomainEvent() {
        EventDto eventDto = buildEventDto();
        Event mapped = eventMapper.eventDtoToEvent(eventDto);
        eventEquals(eventDto, mapped);
    }

    void eventEquals(EventDto dto, Event domain) {
        assertAll(
            () -> assertEquals(dto.getName(), domain.getName()),
            () -> assertEquals(dto.getDescription(),domain.getDescription()),
            () -> assertEquals(dto.getArtists().get(0).getFirstName(),domain.getArtists().get(0).getFirstName())
        );
    }



    EventDto buildEventDto() {
        ArtistDto artistDto = ArtistDto.builder()
            .firstName(TEST_ARTIST_FIRST_NAME)
            .lastName(TEST_ARTIST_LAST_NAME)
            .pseudonym(TEST_ARTIST_PSEUDONYM)
            .createdAt(TEST_ARTIST_CREATED_AT)
            .build();
        List<ArtistDto> artists = new ArrayList<>();
        artists.add(artistDto);

        AddressDto addressDto = AddressDto.builder()
            .city("TEST")
            .country("Georgien")
            .street("TEST")
            .postalCode("saf")
            .build();

        VenueDto venueDto = VenueDto.builder()
            .name("TESTvenue")
            .address(addressDto)
            .build();

        ShowDto showDto = ShowDto.builder()
            .endTime(TEST_SHOW_CREATED_AT)
            .startTime(TEST_SHOW_START_TIME)
            .venue(venueDto)
            .build();

        List<ShowDto> shows = new ArrayList<>();
        shows.add(showDto);

        return EventDto.builder()
            .description("TEst")
            .eventType(TEST_EVENT_TYPE_STRING)
            .imageUrl("asdf")
            .name("ASDF")
            .artists(artists)
            .build();

    }
}
