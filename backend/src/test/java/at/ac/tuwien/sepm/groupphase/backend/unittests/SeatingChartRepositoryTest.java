package at.ac.tuwien.sepm.groupphase.backend.unittests;


import at.ac.tuwien.sepm.groupphase.backend.basetest.TestData;
import at.ac.tuwien.sepm.groupphase.backend.domain.model.Hall;
import at.ac.tuwien.sepm.groupphase.backend.domain.model.SeatingChart;
import at.ac.tuwien.sepm.groupphase.backend.domain.model.Venue;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.repository.HallRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.SeatingChartRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.VenueRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


@SpringBootTest
@Transactional
@ActiveProfiles("test")
public class SeatingChartRepositoryTest {

    @Autowired
    private SeatingChartRepository seatingChartRepository;

    @Autowired
    private HallRepository hallRepository;

    @Autowired
    private VenueRepository venueRepository;

    private final SeatingChart seatingChart = TestData.buildSeatingChart();

    @Test
    void findSeatingChartById_returnsSeatingChartWithSavedId() {
        Venue venue = venueRepository.saveVenue(TestData.buildVenue());
        Hall hall = TestData.buildHall(200,400).toBuilder().venue(venue).build();
        Hall newHall = hallRepository.saveHall(hall);
        SeatingChart newSeatingChart = seatingChart.toBuilder().hall(newHall).build();
        SeatingChart saved = seatingChartRepository.saveSeatingChart(newSeatingChart);
        SeatingChart found = seatingChartRepository.findById(saved.getId());
        assertEquals(saved, found);
    }

    @Test
    void findSeatingChartById_notExisting_shouldThrowNotFoundException() {
        assertEquals(0,seatingChartRepository.findAll().size());
        assertThrows(NotFoundException.class, () -> seatingChartRepository.findById(1L));
    }

    @Test
    public void givenNothing_whenSaveVenue_FindAllReturnsOneElement() {
        assertEquals(0,seatingChartRepository.findAll().size());
        Venue venue = venueRepository.saveVenue(TestData.buildVenue());
        Hall hall = TestData.buildHall(200,400).toBuilder().venue(venue).build();
        Hall newHall = hallRepository.saveHall(hall);
        SeatingChart newSeatingChart = seatingChart.toBuilder().hall(newHall).build();
        SeatingChart saved = seatingChartRepository.saveSeatingChart(newSeatingChart);
        assertEquals(1, seatingChartRepository.findAll().size());
    }

    @Test
    public void givenNothin_whenSaveSeatingChartsWithHallId_FindSeatingChartsByIdReturnsNotAllElements() {
        assertEquals(0,seatingChartRepository.findAll().size());
        Venue venue = venueRepository.saveVenue(TestData.buildVenue());
        Hall hall = TestData.buildHall(200,400).toBuilder().venue(venue).build();
        Hall hall1 = hallRepository.saveHall(hall);
        Hall hall2 = hallRepository.saveHall(hall);
        SeatingChart newSeatingChart1 = seatingChart.toBuilder().hall(hall1).build();
        SeatingChart newSeatingChart2 = seatingChart.toBuilder().hall(hall1).build();
        SeatingChart newSeatingChart3 = seatingChart.toBuilder().hall(hall2).build();
        seatingChartRepository.saveSeatingChart(newSeatingChart1);
        seatingChartRepository.saveSeatingChart(newSeatingChart2);
        seatingChartRepository.saveSeatingChart(newSeatingChart3);
        assertEquals(2,seatingChartRepository.findAllByHallId(hall1.getId()).size());
        assertEquals(1,seatingChartRepository.findAllByHallId(hall2.getId()).size());

    }


}
