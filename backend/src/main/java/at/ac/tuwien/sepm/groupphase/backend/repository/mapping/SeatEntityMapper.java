package at.ac.tuwien.sepm.groupphase.backend.repository.mapping;

import at.ac.tuwien.sepm.groupphase.backend.domain.model.Seat;
import at.ac.tuwien.sepm.groupphase.backend.repository.entity.SeatEntity;
import at.ac.tuwien.sepm.groupphase.backend.repository.entity.SittingSectorEntity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SeatEntityMapper {

    private SeatEntityMapper() {
    }

    /**
     * Maps the given Seat Entity to a Domain representation of the Seat
     *
     * @param seatEntity the Seat Entity to be mapped
     * @return the mapped Seat
     */
    public static Seat toDomain(SeatEntity seatEntity) {
        if(seatEntity == null) {
            return null;
        }
        return Seat.builder()
            .id(seatEntity.getId())
            .x(seatEntity.getX())
            .y(seatEntity.getY())
            .sector(SectorEntityMapper.toSittingSectorDomainWithoutSeats(seatEntity.getSector()))
            .build();
    }

    /**
     * Maps the given Seats Entities in a List to a List of Domain representations of the Seats
     *
     * @param seatEntities the List of Seat Entities to be mapped
     * @return the mapped Seat List
     */
    public static List<Seat> toDomain(List<SeatEntity> seatEntities) {
        if ( seatEntities == null ) {
            return Collections.emptyList();
        }

        List<Seat> list = new ArrayList<>();

        for ( SeatEntity seat : seatEntities ) {
            list.add( toDomain(seat) );
        }

        return list;
    }



    /**
     * Maps the given Seat and Sitting Sector to a Repository Entity representation of the Seat
     *
     * @param seat the Seat to be mapped
     * @param sector the Sitting Sector to be mapped
     * @return the mapped Seat Entity
     */
    public static SeatEntity toEntity(Seat seat, SittingSectorEntity sector) {
        return SeatEntity.builder()
            .id(seat.getId())
            .x(seat.getX())
            .y(seat.getY())
            .sector(sector)
            .build();
    }


}
