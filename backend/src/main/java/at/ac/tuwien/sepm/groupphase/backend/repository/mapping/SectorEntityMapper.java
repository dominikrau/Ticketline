package at.ac.tuwien.sepm.groupphase.backend.repository.mapping;


import at.ac.tuwien.sepm.groupphase.backend.domain.model.Sector;
import at.ac.tuwien.sepm.groupphase.backend.domain.model.SittingSector;
import at.ac.tuwien.sepm.groupphase.backend.domain.model.StandingSector;
import at.ac.tuwien.sepm.groupphase.backend.repository.SeatJpaRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.entity.SeatingChartEntity;
import at.ac.tuwien.sepm.groupphase.backend.repository.entity.SectorEntity;
import at.ac.tuwien.sepm.groupphase.backend.repository.entity.SittingSectorEntity;
import at.ac.tuwien.sepm.groupphase.backend.repository.entity.StandingSectorEntity;
import lombok.RequiredArgsConstructor;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
@RequiredArgsConstructor
public class SectorEntityMapper {

    private final SeatJpaRepository seatJpaRepository;

    /**
     * Maps the given Sector Entity to a Domain representation of the Sector
     *
     * @param sectorEntity the Sector Entity to be mapped
     * @return the mapped Sector
     */
    public Sector toDomain(SectorEntity sectorEntity) {
        if (sectorEntity instanceof SittingSectorEntity)
            return toSittingSectorDomain((SittingSectorEntity) sectorEntity);
        else if (sectorEntity instanceof StandingSectorEntity)
            return toStandingSectorDomain((StandingSectorEntity) sectorEntity);
        return null;
    }


    /**
     * Maps the given Sector Entities in a List to a List of Domain representations of the Sectors
     *
     * @param sectors the List of Sector Entities to be mapped
     * @return the mapped Sector List
     */
    public List<Sector> toDomain(List<SectorEntity> sectors) {
        if ( sectors == null ) {
            return Collections.emptyList();
        }

        List<Sector> list = new ArrayList<>();

        for ( SectorEntity sector : sectors ) {
            list.add( toDomain(sector) );
        }

        return list;
    }


    /**
     * Maps the given Sector to a Repository Entity representation of the Sector
     *
     * @param sector the Sector to be mapped
     * @param seatingChart the Sector belongs to
     * @return the mapped Sector Entity
     */
    @Named("sectorEntity")
    public SectorEntity toEntity(Sector sector, SeatingChartEntity seatingChart) {
        if (sector instanceof SittingSector)
            return toSittingSectorEntity((SittingSector) sector, seatingChart);
        else if (sector instanceof StandingSector)
            return toStandingSectorEntity((StandingSector) sector, seatingChart);
        return null;
    }



    /**
     * Maps the given Sitting Sector Entity to a Domain representation of the Sitting Sector
     *
     * @param sittingSectorEntity the Sitting Sector Entity to be mapped
     * @return the mapped Sitting Sector
     */
    private SittingSector toSittingSectorDomain(SittingSectorEntity sittingSectorEntity) {
        return SittingSector.builder()
            .id(sittingSectorEntity.getId())
            .name(sittingSectorEntity.getName())
            .color(sittingSectorEntity.getColor())
            .seats(SeatEntityMapper.toDomain(seatJpaRepository.findAllBySector(sittingSectorEntity)))
            .build();
    }

    /**
     * Maps the given Standing Sector Entity to a Domain representation of the Standing Sector
     *
     * @param standingSectorEntity the Standing Sector Entity to be mapped
     * @return the mapped Standing Sector
     */
    private StandingSector toStandingSectorDomain(StandingSectorEntity standingSectorEntity) {
        return StandingSector.builder()
            .id(standingSectorEntity.getId())
            .name(standingSectorEntity.getName())
            .color(standingSectorEntity.getColor())
            .x(standingSectorEntity.getX())
            .y(standingSectorEntity.getY())
            .width(standingSectorEntity.getWidth())
            .height(standingSectorEntity.getHeight())
            .capacity(standingSectorEntity.getCapacity())
            .build();
    }

    /**
     * Maps the given Sitting Sector Entity to a Domain representation of the Sitting Sector
     * but without Seat attributes
     *
     * @param sittingSectorEntity the Sitting Sector Entity to be mapped
     * @return the mapped Sitting Sector
     */
    public static SittingSector toSittingSectorDomainWithoutSeats(SittingSectorEntity sittingSectorEntity) {
        return SittingSector.builder()
            .id(sittingSectorEntity.getId())
            .name(sittingSectorEntity.getName())
            .color(sittingSectorEntity.getColor())
            .build();
    }


    /**
     * Maps the given Sitting Sector to a Repository Entity representation of the Sitting Sector
     *
     * @param sittingSector the Sitting Sector to be mapped
     * @param seatingChart the Sitting Sector belongs to
     * @return the mapped Sitting Sector Entity
     */
    private SittingSectorEntity toSittingSectorEntity(SittingSector sittingSector, SeatingChartEntity seatingChart) {
        return SittingSectorEntity.builder()
            .id(sittingSector.getId())
            .name(sittingSector.getName())
            .color(sittingSector.getColor())
            .seatingChart(seatingChart)
            .build();
    }


    /**
     * Maps the given Standing Sector to a Repository Entity representation of the Standing Sector
     *
     * @param standingSector the Standing Sector to be mapped
     * @param seatingChart the Standing Sector belongs to
     * @return the mapped Standing Sector Entity
     */
    private StandingSectorEntity toStandingSectorEntity(StandingSector standingSector, SeatingChartEntity seatingChart) {
        return StandingSectorEntity.builder()
            .id(standingSector.getId())
            .name(standingSector.getName())
            .color(standingSector.getColor())
            .seatingChart(seatingChart)
            .x(standingSector.getX())
            .y(standingSector.getY())
            .height(standingSector.getHeight())
            .width(standingSector.getWidth())
            .capacity(standingSector.getCapacity())
            .build();
    }

}

