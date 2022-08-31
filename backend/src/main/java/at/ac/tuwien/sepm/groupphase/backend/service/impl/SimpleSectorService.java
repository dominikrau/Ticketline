package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.domain.model.Seat;
import at.ac.tuwien.sepm.groupphase.backend.domain.model.SeatingChart;
import at.ac.tuwien.sepm.groupphase.backend.domain.model.Sector;
import at.ac.tuwien.sepm.groupphase.backend.domain.model.SittingSector;
import at.ac.tuwien.sepm.groupphase.backend.repository.SectorRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.SectorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SimpleSectorService implements SectorService {

    private final SectorRepository sectorRepository;
    private final SimpleSeatService seatService;

    @Override
    public Sector createSector(Sector sector, SeatingChart seatingChart) {
        log.debug("Create sector");
        Sector saved = sectorRepository.saveSector(sector, seatingChart);
        if (sector instanceof SittingSector) {
            List<Seat> seats = seatService.createSeat(((SittingSector) sector).getSeats(), (SittingSector) saved, seatingChart);
            ((SittingSector) saved).setSeats(seats);
        }
        return saved;
    }

    @Override
    public List<Sector> createSector(List<Sector> sectors, SeatingChart seatingChart) {
        if(sectors == null) {
            return Collections.emptyList();
        }

        List<Sector> list = new ArrayList<>();

        for (Sector sector : sectors) {
            list.add(createSector(sector, seatingChart));
        }

        return list;
    }

}
