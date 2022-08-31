package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.domain.model.SeatingChart;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SeatingChartDto;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(uses =  { HallMapper.class, StageMapper.class, SectorMapper.class })
public interface SeatingChartMapper {

    /**
     * Maps the given Seating Chart DTO to a Domain representation of the Seating Chart
     *
     * @param seatingChartDto the Seating Chart DTO to be mapped
     * @return the mapped Seating Chart
     */
    @Named("seatingChart")
    @Mapping(source = "sectors", target = "sectors", qualifiedByName = "toSectorsDomainInterface")
    SeatingChart toDomain(SeatingChartDto seatingChartDto);

    /**
     * Maps the given Seating Charts DTOs in a List to a List of Domain representations of the Seating Charts
     *
     * @param seatingChartDtos the List of Seating Chart DTOs to be mapped
     * @return the mapped Seating Chart List
     */
    @IterableMapping(qualifiedByName = "seatingChart")
    List<SeatingChart> toDomain(List<SeatingChartDto> seatingChartDtos);


    /**
     * Maps the given Seating Chart to a Data Transfer Object representation of the Seating Chart
     *
     * @param seatingChart the Seating Chart to be mapped
     * @return the mapped Seating Chart DTO
     */
    @Named("seatingChartDto")
    @Mapping(source ="sectors", target = "sectors", qualifiedByName = "toSectorsDtoInterface")
    SeatingChartDto toDto(SeatingChart seatingChart);

    /**
     * Maps the given Seating Charts in a List to a List of Data Transfer Object representations of the Seating Charts
     *
     * @param seatingCharts the List of Seating Charts to be mapped
     * @return the mapped Seating Chart DTO List
     */
    @IterableMapping(qualifiedByName = "seatingChartDto")
    List<SeatingChartDto> toDto(List<SeatingChart> seatingCharts);
}
