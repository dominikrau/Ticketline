package at.ac.tuwien.sepm.groupphase.backend.repository;


import at.ac.tuwien.sepm.groupphase.backend.repository.entity.SeatingChartEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SeatingChartJpaRepository extends JpaRepository<SeatingChartEntity, Long> {

    /**
     * Find all Seating Chart entries in the database associated with the provided Hall.
     *
     * @param hallId the id of the Hall whose Seating Charts shall be retrieved
     * @return A List of Seating Chart entries associated with the specified Hall
     */
    List<SeatingChartEntity> findAllByHall_Id(Long hallId);
}
