package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.repository.entity.ApplicationUserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
interface UserJpaRepository extends JpaRepository<ApplicationUserEntity, Long>, JpaSpecificationExecutor<ApplicationUserEntity>,
    PagingAndSortingRepository<ApplicationUserEntity, Long>
{

    /**
     * Get User by email.
     * @param emailAddress of the user
     * @return application user with specified email
     */
    Optional<ApplicationUserEntity> findByEmailAddress(String emailAddress);

    /**
     * Find all ApplicationUser entries.
     *
     * @return list of all users
     */
    List<ApplicationUserEntity> findAll();

    /**
     * Search for a specific Page of ApplicationUsers with parameters.
     * @param firstName of the user
     * @param lastName of the user
     * @param blocked if user is blocked
     * @param pageable contains page information (page number + size)
     * @return Page of found users
     */
    Page<ApplicationUserEntity> findAllByFirstNameContainingIgnoreCaseAndLastNameContainingIgnoreCaseAndBlocked(
        String firstName, String lastName, boolean blocked, Pageable pageable);

    /**
     * Get ApplicationUser by id.
     * @param id of the user
     * @return application user
     */
    Optional<ApplicationUserEntity> findById(Long id);

    /**
     * Deletes Users with specified Id
     *
     * @param id to be deleted
     */
    void deleteAllById(Long id);

}
