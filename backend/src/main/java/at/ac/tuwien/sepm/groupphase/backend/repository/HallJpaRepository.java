package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.repository.entity.HallEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HallJpaRepository extends JpaRepository<HallEntity, Long>, JpaSpecificationExecutor<HallEntity> {

    /**
     * Find all hall entries ordered by name (ascending).
     *
     * @return ordered list of all hall entries
     */
    List<HallEntity> findAllByOrderByNameAsc();


}