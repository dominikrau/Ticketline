package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.domain.model.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MessageService {

    /**
     * Find all message entries ordered by published at date (descending).
     *
     * @return ordered list of all message entries
     */
    List<Message> findAll();

    /**
     * Get a page of the current user's unread message entries ordered by published at date (descending).
     *
     * @param pageable information of page (page number and page size)
     * @return Page of unread message entries
     */
    Page<Message> pageFindUnreadMessages(Pageable pageable);

    /**
     * Get a page of the current user's read message entries ordered by published at date (descending).
     *
     * @param pageable information of page (page number and page size)
     * @return Page of read message entries
     */
    Page<Message> pageFindReadMessages(Pageable pageable);

    /**
     * Get a message by it's id and create an entry with the current user's ID and the message's ID in the "read" table.
     *
     * @param id of the message to be fetched and marked as read
     * @return message specified by id
     */
    Message readOneById(Long id);

    /**
     * Find a single message entry by id.
     *
     * @param id the id of the message entry
     * @return the message entry
     */
    Message findOne(Long id);

    /**
     * Publish a single message entry
     *
     * @param message to publish
     * @return published message entry
     */
    Message createMessage(Message message);

}
