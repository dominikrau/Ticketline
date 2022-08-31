package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.domain.model.Seat;
import at.ac.tuwien.sepm.groupphase.backend.repository.entity.SeatEntity;
import at.ac.tuwien.sepm.groupphase.backend.repository.entity.SittingSectorEntity;
import at.ac.tuwien.sepm.groupphase.backend.repository.mapping.SeatEntityMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class SeatRepository {

    private final SeatJpaRepository repository;

    /**
     * Save a single Seat entry in the database
     *
     * @param seat to be saved
     * @param sector to which the seat belongs
     * @return saved Seat entry
     */
    public Seat saveSeat(Seat seat, SittingSectorEntity sector) {
        SeatEntity saved = repository.save(SeatEntityMapper.toEntity(seat, sector));
        return SeatEntityMapper.toDomain(saved);
    }

    /**
     * Get a Seat by it's Id and associated Sector
     * @param id of the seat
     * @param sectorId to which the seat belongs
     * @return the specified seat in the specified sector
     */
    public Seat findSeatByIdAndSector(Long id, Long sectorId) {
        return SeatEntityMapper.toDomain(repository.findSeatEntityByIdAndSectorId(id, sectorId));
    }

    /**
     * Get multiple seats by Id
     * @param ids of the seats to be retrieved
     * @return List of specified seats
     */
    public List<Seat> findByIds(List<Long> ids) {
        return SeatEntityMapper.toDomain(repository.findAllByIdIn(ids));
    }

}
