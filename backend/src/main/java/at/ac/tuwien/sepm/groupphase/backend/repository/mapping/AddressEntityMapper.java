package at.ac.tuwien.sepm.groupphase.backend.repository.mapping;

import at.ac.tuwien.sepm.groupphase.backend.domain.model.Address;
import at.ac.tuwien.sepm.groupphase.backend.repository.entity.AddressEntity;
import org.mapstruct.Mapper;

@Mapper
public interface AddressEntityMapper {
    /**
     * Maps the given Address Entity to a Domain representation of the Address
     *
     * @param addressEntity the Address Entity to be mapped
     * @return the mapped Address
     */
    Address toDomain(AddressEntity addressEntity);

    /**
     * Maps the given Address to a Repository Entity representation of the Address
     *
     * @param address the Address to be mapped
     * @return the mapped Address Entity
     */
    AddressEntity toEntity(Address address);

}
