package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.domain.model.Seat;
import at.ac.tuwien.sepm.groupphase.backend.domain.model.SeatingChart;
import at.ac.tuwien.sepm.groupphase.backend.domain.model.SittingSector;

import java.util.List;

public interface SeatService {
    /**
     * Create a single Seat entry
     *
     * @param seat to be saved
     * @param sector to which the seat belongs
     * @param seatingChart to which the seat and sector belongs
     * @return saved Seat entry
     */
    Seat createSeat(Seat seat, SittingSector sector, SeatingChart seatingChart);

    /**
     * Create a List of Seat entries
     *
     * @param seats - List of seats to be saved
     * @param sector to which the seats belongs
     * @param seatingChart to which the seats and sector belongs
     * @return saved List of Seat entries
     */
    List<Seat> createSeat(List<Seat> seats, SittingSector sector, SeatingChart seatingChart);
}
