package at.ac.tuwien.sepm.groupphase.backend.repository.mapping;

import at.ac.tuwien.sepm.groupphase.backend.domain.model.Show;
import at.ac.tuwien.sepm.groupphase.backend.repository.PricingJpaRepository;
import at.ac.tuwien.sepm.groupphase.backend.repository.entity.ShowEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ShowEntityMapper {
    private final VenueEntityMapper venueEntityMapper;
    private final EventEntityMapper eventEntityMapper;
    private final PricingEntityMapper pricingEntityMapper;
    private final PricingJpaRepository pricingJpaRepository;
    private final SeatingChartEntityMapper seatingChartEntityMapper;


    /**
     * Maps the given Show Entity to a Domain representation of the Show
     *
     * @param showEntity the Show Entity to be mapped
     * @return the mapped Show
     */
    public Show toDomain(ShowEntity showEntity) {
        return Show.builder()
            .showId(showEntity.getShowId())
            .createdAt(showEntity.getCreatedAt())
            .venue(venueEntityMapper.toDomain(showEntity.getVenue()))
            .startTime(showEntity.getStartTime())
            .endTime(showEntity.getEndTime())
            .event(eventEntityMapper.toDomain(showEntity.getEvent()))
            .pricings(pricingEntityMapper.toDomain(pricingJpaRepository.findAllByShow(showEntity)))
            .seatingChart(seatingChartEntityMapper.toDomain(showEntity.getSeatingChart()))
            .build();
    }

    /**
     * Maps the given Shows Entities in a List to a List of Domain representations of the Shows
     *
     * @param showEntities the List of Show Entities to be mapped
     * @return the mapped Show List
     */
    public List<Show> toDomain(List<ShowEntity> showEntities) {
        if ( showEntities == null ) {
            return Collections.emptyList();
        }

        List<Show> list = new ArrayList<>();

        for ( ShowEntity showEntity : showEntities ) {
            list.add( toDomain(showEntity) );
        }

        return list;
    }



    /**
     * Maps the given Show to a Repository Entity representation of the Show
     *
     * @param show the Show to be mapped
     * @return the mapped Show Entity
     */
    public ShowEntity toEntity(Show show) {
        return ShowEntity.builder()
            .showId(show.getShowId())
            .createdAt(show.getCreatedAt())
            .venue(venueEntityMapper.toEntity(show.getVenue()))
            .startTime(show.getStartTime())
            .endTime(show.getEndTime())
            .event(eventEntityMapper.toEntity(show.getEvent()))
            .seatingChart(seatingChartEntityMapper.toEntity(show.getSeatingChart()))
            .build();
    }

}
