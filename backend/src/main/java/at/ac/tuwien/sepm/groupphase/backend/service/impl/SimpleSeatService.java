package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.domain.model.Seat;
import at.ac.tuwien.sepm.groupphase.backend.domain.model.SeatingChart;
import at.ac.tuwien.sepm.groupphase.backend.domain.model.SittingSector;
import at.ac.tuwien.sepm.groupphase.backend.repository.SeatRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.entity.SittingSectorEntity;
import at.ac.tuwien.sepm.groupphase.backend.repository.mapping.SeatingChartEntityMapper;
import at.ac.tuwien.sepm.groupphase.backend.repository.mapping.SectorEntityMapper;
import at.ac.tuwien.sepm.groupphase.backend.service.SeatService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SimpleSeatService implements SeatService {

    private final SeatRepository seatRepository;
    private final SeatingChartEntityMapper seatingChartEntityMapper;
    private final SectorEntityMapper sectorEntityMapper;

    @Override
    public Seat createSeat(Seat seat, SittingSector sector, SeatingChart seatingChart) {
        return seatRepository.saveSeat(seat, (SittingSectorEntity) sectorEntityMapper.toEntity(sector, seatingChartEntityMapper.toEntity(seatingChart)));
    }

    @Override
    public List<Seat> createSeat(List<Seat> seats, SittingSector sector, SeatingChart seatingChart) {
        if(seats == null) {
            return Collections.emptyList();
        }
        log.debug("Create list of seats with number of seats: {} for seating chart with id {}", seats.size(), seatingChart.getId());

        List<Seat> list = new ArrayList<>();

        for (Seat seat : seats) {
            list.add(createSeat(seat, sector, seatingChart));
        }

        return list;
    }

}
