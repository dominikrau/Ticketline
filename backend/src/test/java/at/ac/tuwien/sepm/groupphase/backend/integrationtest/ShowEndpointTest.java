package at.ac.tuwien.sepm.groupphase.backend.integrationtest;

import at.ac.tuwien.sepm.groupphase.backend.basetest.TestData;
import at.ac.tuwien.sepm.groupphase.backend.config.properties.SecurityProperties;
import at.ac.tuwien.sepm.groupphase.backend.domain.model.*;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SeatingChartDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ShowDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.*;
import at.ac.tuwien.sepm.groupphase.backend.repository.*;
import at.ac.tuwien.sepm.groupphase.backend.security.JwtTokenizer;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
@Transactional
public class ShowEndpointTest implements TestData {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SeatingChartRepository seatingChartRepository;

    @Autowired
    private HallRepository hallRepository;

    @Autowired
    private VenueRepository venueRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private SeatingChartMapper seatingChartMapper;

    @Autowired
    private SectorMapper sectorMapper;

    @Autowired
    private HallMapper hallMapper;

    @Autowired
    private JwtTokenizer jwtTokenizer;

    @Autowired
    private SecurityProperties securityProperties;

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private ShowRepository showRepository;

    @Autowired
    private ArtistRepository artistRepository;

    @Autowired
    private ShowMapper showMapper;

    private SeatingChart seatingChart = TestData.buildSeatingChart();


    @Test
    public void givenOneShow_whenFindAll_thenListWithSizeOneAndShowWithAllProperties()
        throws Exception {
        Venue venue = venueRepository.saveVenue(TestData.buildVenue());
        Hall hall = TestData.buildHall(200,400).toBuilder().venue(venue).build();
        Hall newHall = hallRepository.saveHall(hall);
        SeatingChart newSeatingChart = seatingChart.toBuilder().hall(newHall).build();
        SeatingChart saved  = seatingChartRepository.saveSeatingChart(newSeatingChart);
        /*
        Artist artist = artistRepository.save(Artist.builder()
        .firstName("Testname1")
        .lastName("Lastname1")
        .pseudonym("Tester1")
        .build());

         */
        Event event = eventRepository.saveEvent(Event.builder()
        .imageUrl("testImage")
        .description("description1")
        .artist(Artist.builder()
            .firstName("Testname1")
            .lastName("Lastname1")
            .pseudonym("Tester1")
            .build())
        .name("name1")
        .eventType(EventType.SPORTS)
        .build());
        List<Pricing> pricings = new ArrayList<>();
        for (Sector sector: saved.getSectors()
             ) {
            Pricing pricing = Pricing.builder()
                .sector(sector)
                .price(10.00)
                .build();
            pricings.add(pricing);
        }
        Show show = showRepository.saveShow(
            Show.builder()
                .pricings(pricings)
                .venue(venue)
                .event(event)
                .endTime(LocalDateTime.of(2021,1,1,8,10))
                .startTime(LocalDateTime.of(2021,1,1,12,10))
                .seatingChart(saved)
                .build());

        MvcResult mvcResult = this.mockMvc.perform(get(SHOW_BASE_URL)
            .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(ADMIN_USER, ADMIN_ROLES)))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());

        List<ShowDto> showDtos = Arrays.asList(objectMapper.readValue(response.getContentAsString(),
            ShowDto[].class));

        assertEquals(1, showDtos.size());
        ShowDto showDto = showDtos.get(0);
        Show found = showMapper.showDtoToShow(showDto);
        assertAll(
            () -> assertEquals(show.getShowId(), found.getShowId()),
            () -> assertEquals(show.getStartTime(), found.getStartTime()),
            () -> assertEquals(show.getEndTime(), found.getEndTime())
        );
    }
}