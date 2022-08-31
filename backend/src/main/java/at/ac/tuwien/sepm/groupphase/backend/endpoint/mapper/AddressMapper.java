package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.domain.model.Address;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.AddressDto;
import org.mapstruct.Mapper;

@Mapper
public interface AddressMapper {

    /**
     * Maps the given Address Dto to a Domain representation of the Address
     *
     * @param addressDto the address DTO to be mapped
     * @return the mapped Address
     */
    Address toDomain(AddressDto addressDto);

    /**
     * Maps the given Address to a Data Transfer Object representation of the Address
     *
     * @param address the address to be mapped
     * @return the mapped Address DTO
     */
    AddressDto toDto(Address address);

}
