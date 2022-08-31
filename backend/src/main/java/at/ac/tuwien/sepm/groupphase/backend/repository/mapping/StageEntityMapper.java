package at.ac.tuwien.sepm.groupphase.backend.repository.mapping;

import at.ac.tuwien.sepm.groupphase.backend.domain.model.Stage;
import at.ac.tuwien.sepm.groupphase.backend.repository.entity.StageEntity;
import org.mapstruct.Mapper;

@Mapper
public interface StageEntityMapper {

    /**
     * Maps the given Stage Entity to a Domain representation of the Stage
     *
     * @param stageEntity the Stage Entity to be mapped
     * @return the mapped Stage
     */
    Stage toDomain(StageEntity stageEntity);

    /**
     * Maps the given Stage to a Repository Entity representation of the Stage
     *
     * @param stage the Stage to be mapped
     * @return the mapped Stage Entity
     */
    StageEntity toEntity(Stage stage);

}
