package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.domain.model.Stage;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.StageDto;
import org.mapstruct.Mapper;

@Mapper
public interface StageMapper {

    /**
     * Maps the given Stage DTO to a Domain representation of the Stage
     *
     * @param stageDto the Stage DTO to be mapped
     * @return the mapped Stage
     */
    Stage toDomain(StageDto stageDto);

    /**
     * Maps the given Stage to a Data Transfer Object representation of the Stage
     *
     * @param stage the Stage to be mapped
     * @return the mapped Stage DTO
     */
    StageDto toDto(Stage stage);
}
