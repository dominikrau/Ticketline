package at.ac.tuwien.sepm.groupphase.backend.repository;


import at.ac.tuwien.sepm.groupphase.backend.repository.entity.SeatEntity;
import at.ac.tuwien.sepm.groupphase.backend.repository.entity.SectorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SeatJpaRepository extends JpaRepository<SeatEntity, Long> {

    /**
     * Get all seats of the specified Sector
     *
     * @param sectorEntity sector whose seats are to be retrieved
     * @return List of seats of the specified Sector
     */
    List<SeatEntity> findAllBySector(SectorEntity sectorEntity);

    /**
     * Get a Seat by it's Id and associated Sector
     * @param id of the seat
     * @param sectorId to which the seat belongs
     * @return the specified seat in the specified sector
     */
    SeatEntity findSeatEntityByIdAndSectorId(Long id, Long sectorId);

    /**
     * Get multiple seats by Id
     * @param ids of the seats to be retrieved
     * @return List of specified seats
     */
    List<SeatEntity> findAllByIdIn(List<Long> ids);
}
