package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.domain.model.SeatingChart;

import java.util.List;

public interface SeatingChartService {

    /**
     * Create a single seatingChart entry
     *
     * @param seatingChart to publish
     * @return created seatingChart entry
     */
    SeatingChart createSeatingChart(SeatingChart seatingChart);

    /**
     * Find all seatingChart entries
     *
     * @return list of all seatingChart entries
     */
    List<SeatingChart> findAll();

    /**
     * Find all seatingChart entries for a hall
     *
     * @param hallId id of the hall that the seatingCharts shall be found for
     * @return list of all seatingChart entries with given hallId
     */
    List<SeatingChart> findAllByHallId(Long hallId);

    /**
     * Find a single seatingChart entry by id.
     *
     * @param id the id of the seatingChart entry
     * @return the seatingChart entry
     */
    SeatingChart findById(Long id);


}
