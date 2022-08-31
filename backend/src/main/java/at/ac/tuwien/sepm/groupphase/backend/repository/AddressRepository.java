package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.repository.entity.AddressEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AddressRepository extends JpaRepository<AddressEntity, Long> {
    /**
     * Find all address entries ordered by created at date (descending).
     *
     * @return ordered list of all address entries
     */
    List<AddressEntity> findAllByOrderByCreatedAtDesc();

}
