package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.repository.entity.ApplicationUserEntity;
import at.ac.tuwien.sepm.groupphase.backend.repository.entity.MessageEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageJpaRepository extends JpaRepository<MessageEntity, Long>, JpaSpecificationExecutor<MessageEntity> {

    /**
     * Find all message entries ordered by published at date (descending).
     *
     * @return ordered list of all message entries
     */
    List<MessageEntity> findAllByOrderByPublishedAtDesc();

    /**
     * Get a page of the specified user's read message entries.
     *
     * @param user whose read messages should be fetched from the database
     * @param page information of page (page number and page size)
     * @return Page of the specified user's read message entries
     */
    Page<MessageEntity> findAllByUsersContaining(ApplicationUserEntity user, Pageable page);

    /**
     * Get a page of the specified user's unread message entries.
     *
     * @param user whose unread messages should be fetched from the database
     * @param page information of page (page number and page size)
     * @return Page of the specified user's unread message entries
     */
    Page<MessageEntity> findAllByUsersNotContaining(ApplicationUserEntity user, Pageable page);

    /**
     * Delete all messages in the database associated with the provided User
     *
     * @param user whose messages should be deleted
     */
    void deleteAllByUsersContaining(ApplicationUserEntity user);
}
