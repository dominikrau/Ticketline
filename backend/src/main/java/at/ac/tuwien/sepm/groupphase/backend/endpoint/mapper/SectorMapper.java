package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.domain.model.Sector;
import at.ac.tuwien.sepm.groupphase.backend.domain.model.SittingSector;
import at.ac.tuwien.sepm.groupphase.backend.domain.model.StandingSector;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SectorDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.Named;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class SectorMapper {

    private final SeatMapper seatMapper;

    /**
     * Maps the given Sector DTO to a Domain representation of the Sector
     *
     * @param sectorDto the Sector DTO to be mapped
     * @return the mapped Sector
     */
    @Named("toSectorDomainInterface")
    public Sector toDomain(SectorDto sectorDto) {
        if(sectorDto.getType().equals("standing")) {
            return toStandingSectorDomain(sectorDto);
        } else if(sectorDto.getType().equals("sitting")) {
            return toSittingSectorDomain(sectorDto);
        } else {
            return null;
        }
    }

    /**
     * Maps the given Sectors DTOs in a List to a List of Domain representations of the Sectors
     *
     * @param sectorDtos the List of Sector DTOs to be mapped
     * @return the mapped Sector List
     */
    @Named("toSectorsDomainInterface")
    List<Sector> toDomain(List<SectorDto> sectorDtos) {
        if (sectorDtos == null) {
            return Collections.emptyList();
        }

        List<Sector> list = new ArrayList<>();

        for (SectorDto sectorDto : sectorDtos ) {
            list.add(toDomain(sectorDto));
        }

        return list;
    }



    /**
     * Maps the given Sector to a Data Transfer Object representation of the Sector
     *
     * @param sector the Sector to be mapped
     * @return the mapped Sector DTO
     */
    public SectorDto toDto(Sector sector) {
        if(sector instanceof StandingSector) {
            return standingSectorToDto((StandingSector) sector, "standing");
        } else if(sector instanceof SittingSector) {
            return sittingSectorToDto((SittingSector) sector, "sitting");
        } else {
            return null;
        }
    }

    /**
     * Maps the given Sectors in a List to a List of Data Transfer Object representations of the Sectors
     *
     * @param sectors the List of Sectors to be mapped
     * @return the mapped Sector DTO List
     */
    @Named("toSectorsDtoInterface")
    List<SectorDto> toDto(List<Sector> sectors) {
        if (sectors == null) {
            return Collections.emptyList();
        }

        List<SectorDto> list = new ArrayList<>();

        for (Sector sector : sectors) {
            list.add(toDto(sector));
        }

        return list;
    }


    /**
     * Maps the given Sector DTO to a Domain representation of the Standing Sector
     *
     * @param sectorDto the Sector DTO to be mapped
     * @return the mapped Standing Sector
     */
    public StandingSector toStandingSectorDomain(SectorDto sectorDto) {
        return StandingSector.builder()
            .id(sectorDto.getId())
            .name(sectorDto.getName())
            .color(sectorDto.getColor())
            .x(sectorDto.getX())
            .y(sectorDto.getY())
            .width(sectorDto.getWidth())
            .height(sectorDto.getHeight())
            .capacity(sectorDto.getCapacity())
            .build();
    }

    /**
     * Maps the given Standing Sector to a Data Transfer Object representation of the Sector
     *
     * @param standingSector the Standing Sector to be mapped
     * @param type of the Sector
     * @return the mapped Sector DTO
     */
    public SectorDto standingSectorToDto(StandingSector standingSector, String type) {
        return SectorDto.builder()
            .id(standingSector.getId())
            .name(standingSector.getName())
            .color(standingSector.getColor())
            .type(type)
            .x(standingSector.getX())
            .y(standingSector.getY())
            .width(standingSector.getWidth())
            .height(standingSector.getHeight())
            .capacity(standingSector.getCapacity())
            .available(standingSector.getAvailable())
            .build();
    }


    /**
     * Maps the given Sector DTO to a Domain representation of the Sitting Sector
     *
     * @param sectorDto the Sector DTO to be mapped
     * @return the mapped Sitting Sector
     */
    public SittingSector toSittingSectorDomain(SectorDto sectorDto) {
        return SittingSector.builder()
            .id(sectorDto.getId())
            .name(sectorDto.getName())
            .color(sectorDto.getColor())
            .seats(seatMapper.toDomain(sectorDto.getSeats()))
            .build();
    }

    /**
     * Maps the given Sitting Sector to a Data Transfer Object representation of the Sector
     *
     * @param sittingSector the Sitting Sector to be mapped
     * @param type of the Sector
     * @return the mapped Sector DTO
     */
    public SectorDto sittingSectorToDto(SittingSector sittingSector, String type) {
        return SectorDto.builder()
            .id(sittingSector.getId())
            .name(sittingSector.getName())
            .color(sittingSector.getColor())
            .type(type)
            .seats(seatMapper.toDto(sittingSector.getSeats()))
            .build();
    }

}
