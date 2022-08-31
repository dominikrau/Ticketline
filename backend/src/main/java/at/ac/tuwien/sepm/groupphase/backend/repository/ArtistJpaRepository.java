package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.repository.entity.ArtistEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

public interface ArtistJpaRepository extends JpaRepository<ArtistEntity, Long>, JpaSpecificationExecutor<ArtistEntity> {

    /**
     * Find all artist entries ordered by created at date (descending).
     *
     * @return ordered list of all artist entries
     */
    List<ArtistEntity> findAllByOrderByCreatedAtDesc();
}
