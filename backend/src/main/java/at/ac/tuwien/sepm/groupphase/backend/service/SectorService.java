package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.domain.model.SeatingChart;
import at.ac.tuwien.sepm.groupphase.backend.domain.model.Sector;

import java.util.List;

public interface SectorService {

    /**
     * Create a single Sector entry
     *
     * @param sector to be saved
     * @param seatingChart to which the Sector belongs
     * @return saved Sector entry
     */
    Sector createSector(Sector sector, SeatingChart seatingChart);


    /**
     * Create multiple Sector entries
     *
     * @param sectors List of sectors to be saved
     * @param seatingChart to which the sectors belongs
     * @return List of saved Sector entry
     */
    List<Sector> createSector(List<Sector> sectors, SeatingChart seatingChart);
}
