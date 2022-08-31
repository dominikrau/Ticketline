package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.domain.model.Seat;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SeatDto;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

import java.util.List;

@Mapper
public interface SeatMapper {

    /**
     * Maps the given Seat DTO to a Domain representation of the Seat
     *
     * @param seatDto the Seat DTO to be mapped
     * @return the mapped Seat
     */
    @Named("seat")
    Seat toDomain(SeatDto seatDto);

    /**
     * Maps the given Seats DTOs in a List to a List of Domain representations of the Seats
     *
     * @param seatDtos the List of Seat DTOs to be mapped
     * @return the mapped Seat List
     */
    @IterableMapping(qualifiedByName = "seat")
    List<Seat> toDomain(List<SeatDto> seatDtos);


    /**
     * Maps the given Seat to a Data Transfer Object representation of the Seat
     *
     * @param seat the Seat to be mapped
     * @return the mapped Seat DTO
     */
    @Named("seatDto")
    SeatDto toDto(Seat seat);

    /**
     * Maps the given Seats in a List to a List of Data Transfer Object representations of the Seats
     *
     * @param seats the List of Seats to be mapped
     * @return the mapped Seat DTO List
     */
    @IterableMapping(qualifiedByName = "seatDto")
    List<SeatDto> toDto(List<Seat> seats);
}
