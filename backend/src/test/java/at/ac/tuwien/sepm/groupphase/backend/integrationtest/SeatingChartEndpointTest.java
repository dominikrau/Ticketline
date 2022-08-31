package at.ac.tuwien.sepm.groupphase.backend.integrationtest;

import at.ac.tuwien.sepm.groupphase.backend.basetest.TestData;
import at.ac.tuwien.sepm.groupphase.backend.config.properties.SecurityProperties;
import at.ac.tuwien.sepm.groupphase.backend.domain.model.Hall;
import at.ac.tuwien.sepm.groupphase.backend.domain.model.SeatingChart;
import at.ac.tuwien.sepm.groupphase.backend.domain.model.Venue;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SeatingChartDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.*;
import at.ac.tuwien.sepm.groupphase.backend.repository.HallRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.SeatingChartRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.VenueRepository;
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
public class SeatingChartEndpointTest implements TestData {

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

    private SeatingChart seatingChart = TestData.buildSeatingChart();

    @Test
    public void givenNothing_whenFindAll_thenEmptyList() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(get(SEATINGCHARTS_BASE_URL)
            .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(ADMIN_USER, ADMIN_ROLES)))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());

        List<SeatingChartDto> seatingChartDtos = Arrays.asList(objectMapper.readValue(response.getContentAsString(),
            SeatingChartDto[].class));

        assertEquals(0,seatingChartDtos.size());

    }

    @Test
    public void givenOneSeatingChart_whenFindAll_thenListWithSizeOneAndSeatingChartWithAllProperties()
        throws Exception {
        Venue venue = venueRepository.saveVenue(TestData.buildVenue());
        Hall hall = TestData.buildHall(200,400).toBuilder().venue(venue).build();
        Hall newHall = hallRepository.saveHall(hall);
        SeatingChart newSeatingChart = seatingChart.toBuilder().hall(newHall).build();
        SeatingChart saved  = seatingChartRepository.saveSeatingChart(newSeatingChart);

        MvcResult mvcResult = this.mockMvc.perform(get(SEATINGCHARTS_BASE_URL)
            .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(ADMIN_USER, ADMIN_ROLES)))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.OK.value(), response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());

        List<SeatingChartDto> seatingChartDtos = Arrays.asList(objectMapper.readValue(response.getContentAsString(),
            SeatingChartDto[].class));

        assertEquals(1, seatingChartDtos.size());
        SeatingChartDto seatingChartDto = seatingChartDtos.get(0);
        assertAll(
            () -> assertEquals(saved.getId(), seatingChartDto.getId()),
            () -> assertEquals(saved.getName(), seatingChartDto.getName()),
            () -> assertEquals(saved.getHall(), hallMapper.hallDtoToHall(seatingChartDto.getHall()))
        );
    }

    @Test
    public void givenOneSeatingChart_whenFindByNonExistingId_then404() throws Exception {
        Venue venue = venueRepository.saveVenue(TestData.buildVenue());
        Hall hall = TestData.buildHall(200,400).toBuilder().venue(venue).build();
        Hall newHall = hallRepository.saveHall(hall);
        SeatingChart newSeatingChart = seatingChart.toBuilder().hall(newHall).build();
        SeatingChart saved  = seatingChartRepository.saveSeatingChart(newSeatingChart);

        MvcResult mvcResult = this.mockMvc.perform(get(SEATINGCHARTS_BASE_URL + "/{id}", -1)
            .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(ADMIN_USER, ADMIN_ROLES)))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getStatus());
    }

    /*
    @Test
    public void givenNothing_whenPost_thenMessageWithAllSetPropertiesPlusIdAndPublishedDate() throws Exception {

        SeatingChartDto seatingChartDto = seatingChartMapper.toDto(seatingChart);
        String body = objectMapper.writeValueAsString(seatingChartDto);

        MvcResult mvcResult = this.mockMvc.perform(post(MESSAGE_BASE_URI)
            .contentType(MediaType.APPLICATION_JSON)
            .content(body)
            .header(securityProperties.getAuthHeader(), jwtTokenizer.getAuthToken(ADMIN_USER, ADMIN_ROLES)))
            .andDo(print())
            .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();

        assertEquals(HttpStatus.CREATED.value(), response.getStatus());
        assertEquals(MediaType.APPLICATION_JSON_VALUE, response.getContentType());

        DetailedMessageDto messageResponse = objectMapper.readValue(response.getContentAsString(),
            DetailedMessageDto.class);

        assertNotNull(messageResponse.getId());
        assertNotNull(messageResponse.getPublishedAt());
        assertTrue(isNow(messageResponse.getPublishedAt()));
        //Set generated properties to null to make the response comparable with the original input
        messageResponse.setId(null);
        messageResponse.setPublishedAt(null);
        assertEquals(message, messageMapper.detailedMessageDtoToMessage(messageResponse));
    } */




}
