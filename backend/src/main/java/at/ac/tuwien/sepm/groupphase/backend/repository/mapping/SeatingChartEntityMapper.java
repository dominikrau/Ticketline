package at.ac.tuwien.sepm.groupphase.backend.repository.mapping;


import at.ac.tuwien.sepm.groupphase.backend.domain.model.SeatingChart;
import at.ac.tuwien.sepm.groupphase.backend.repository.SectorJpaRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.entity.SeatingChartEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SeatingChartEntityMapper {
    private final HallEntityMapper hallEntityMapper;
    private final SectorEntityMapper sectorEntityMapper;
    private final SectorJpaRepository sectorJpaRepository;
    private final StageEntityMapper stageEntityMapper;

    /**
     * Maps the given Seating Chart Entity to a Domain representation of the Seating Chart
     *
     * @param seatingChartEntity the Seating Chart Entity to be mapped
     * @return the mapped Seating Chart
     */
    public SeatingChart toDomain(SeatingChartEntity seatingChartEntity) {
        return SeatingChart.builder()
            .id(seatingChartEntity.getId())
            .createdAt(seatingChartEntity.getCreatedAt())
            .name(seatingChartEntity.getName())
            .hall(hallEntityMapper.toDomain(seatingChartEntity.getHall()))
            .sectors(sectorEntityMapper.toDomain(sectorJpaRepository.findAllBySeatingChart(seatingChartEntity)))
            .stage(stageEntityMapper.toDomain(seatingChartEntity.getStage()))
            .build();
    }


    /**
     * Maps the given Seating Chart to a Repository Entity representation of the Seating Chart
     *
     * @param seatingChart the Seating Chart to be mapped
     * @return the mapped Seating Chart Entity
     */
    public SeatingChartEntity toEntity(SeatingChart seatingChart) {
        return SeatingChartEntity.builder()
            .id(seatingChart.getId())
            .createdAt(seatingChart.getCreatedAt())
            .name(seatingChart.getName())
            .hall(hallEntityMapper.toEntity(seatingChart.getHall()))
            .stage(stageEntityMapper.toEntity(seatingChart.getStage()))
            .build();

    }
}
