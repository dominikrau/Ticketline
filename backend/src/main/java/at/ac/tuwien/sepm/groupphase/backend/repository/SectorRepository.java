package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.domain.model.SeatingChart;
import at.ac.tuwien.sepm.groupphase.backend.domain.model.Sector;
import at.ac.tuwien.sepm.groupphase.backend.repository.entity.SectorEntity;
import at.ac.tuwien.sepm.groupphase.backend.repository.mapping.SeatingChartEntityMapper;
import at.ac.tuwien.sepm.groupphase.backend.repository.mapping.SectorEntityMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class SectorRepository {

    private final SectorJpaRepository repository;
    private final SeatingChartEntityMapper seatingChartEntityMapper;
    private final SectorEntityMapper sectorEntityMapper;

    /**
     * Save a single Sector entry in the database
     *
     * @param sector to be saved
     * @param seatingChart to which the Sector belongs
     * @return saved Sector entry
     */
    public Sector saveSector(Sector sector, SeatingChart seatingChart) {
        SectorEntity saved = repository.save(sectorEntityMapper.toEntity(sector, seatingChartEntityMapper.toEntity(seatingChart)));
        return sectorEntityMapper.toDomain(saved);
    }

    /**
     * Get multiple sectors by Id
     * @param ids of the sectors to be retrieved
     * @return List of specified sectors
     */
    public List<Sector> findByIds(List<Long> ids) {
        return sectorEntityMapper.toDomain(repository.findAllByIdIn(ids));
    }

}
