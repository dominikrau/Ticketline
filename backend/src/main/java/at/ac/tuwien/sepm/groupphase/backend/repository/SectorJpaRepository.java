package at.ac.tuwien.sepm.groupphase.backend.repository;


import at.ac.tuwien.sepm.groupphase.backend.repository.entity.SeatingChartEntity;
import at.ac.tuwien.sepm.groupphase.backend.repository.entity.SectorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SectorJpaRepository extends JpaRepository<SectorEntity, Long> {

    /**
     * Find all Sectors in the database associated with the provided Seating Chart.
     *
     * @param seatingChart whose Sectors shall be retrieved
     * @return List of Sectors associated with the specified Seating Chart
     */
    List<SectorEntity> findAllBySeatingChart(SeatingChartEntity seatingChart);

    /**
     * Get multiple sectors by Id
     * @param ids of the sectors to be retrieved
     * @return List of specified sectors
     */
    List<SectorEntity> findAllByIdIn(List<Long> ids);
}
