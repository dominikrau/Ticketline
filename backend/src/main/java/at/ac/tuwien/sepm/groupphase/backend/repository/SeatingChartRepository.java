package at.ac.tuwien.sepm.groupphase.backend.repository;


import at.ac.tuwien.sepm.groupphase.backend.domain.model.SeatingChart;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.repository.entity.SeatingChartEntity;
import at.ac.tuwien.sepm.groupphase.backend.repository.mapping.SeatingChartEntityMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static java.lang.String.format;

@Service
@RequiredArgsConstructor
public class SeatingChartRepository {

    private final SeatingChartJpaRepository repository;
    private final SeatingChartEntityMapper mapper;

    /**
     * Save a single Seating Chart entry in the database
     *
     * @param seatingChart to save
     * @return saved event Seating Chart
     */
    public SeatingChart saveSeatingChart(SeatingChart seatingChart) {
        SeatingChartEntity saved = repository.save(mapper.toEntity(seatingChart));
        return mapper.toDomain(saved);
    }

    /**
     * Find all Seating Chart entries in the database ordered by name (ascending).
     *
     * @return ordered list of all Seating Chart entries
     */
    public List<SeatingChart> findAll() {
        return repository.findAll()
            .stream().map(mapper::toDomain).collect(Collectors.toList());
    }

    /**
     * Find all Seating Chart entries in the database associated with the provided Hall id.
     *
     * @param hallId the id of the Hall whose Seating Charts shall be retrieved
     * @return Seating Chart entries associated with the specified Hall
     */
    public List<SeatingChart> findAllByHallId(Long hallId) {
        return repository.findAllByHall_Id(hallId)
            .stream().map(mapper::toDomain).collect(Collectors.toList());
    }

    /**
     * Find a single Seating Chart entry in the database by id.
     *
     * @param id the id of the Seating Chart entry
     * @return Seating Chart entry with corresponding id
     */
    public SeatingChart findById(Long id) {
        return repository.findById(id)
            .map(mapper::toDomain)
            .orElseThrow(() -> new NotFoundException(
                format("No SeatingChart with given Id \"%s\"  found", id)
            ));
    }
}
