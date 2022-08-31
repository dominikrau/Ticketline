package at.ac.tuwien.sepm.groupphase.backend.service.impl;


import at.ac.tuwien.sepm.groupphase.backend.domain.model.SeatingChart;
import at.ac.tuwien.sepm.groupphase.backend.domain.model.Sector;
import at.ac.tuwien.sepm.groupphase.backend.repository.SeatingChartRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.SeatingChartService;
import at.ac.tuwien.sepm.groupphase.backend.util.Validator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SimpleSeatingChartService implements SeatingChartService {

    private final SeatingChartRepository seatingChartRepository;
    private final SimpleSectorService sectorService;
    private final Validator validator;

    @Override
    public SeatingChart createSeatingChart(SeatingChart newSeatingChart) {

        log.debug("Create seating chart {}", newSeatingChart);
        validator.validateSeatingChart(newSeatingChart);
        SeatingChart saved = seatingChartRepository.saveSeatingChart(newSeatingChart);
        List<Sector> sectors = sectorService.createSector(newSeatingChart.getSectors(), saved);
        saved = saved.toBuilder().sectors(sectors).build();

        return saved;
    }

    @Override
    public List<SeatingChart> findAll() {
        log.debug("Find all SeatingCharts");
        return seatingChartRepository.findAll();
    }

    @Override
    public List<SeatingChart> findAllByHallId(Long hallId) {
        log.debug("Find all seating charts by hallId {}", hallId);
        return seatingChartRepository.findAllByHallId(hallId);
    }

    @Override
    public SeatingChart findById(Long id) {
        log.debug("Find seating chart with id {}", id);
        return seatingChartRepository.findById(id);}
}

