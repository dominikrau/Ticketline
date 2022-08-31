package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.domain.model.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.domain.model.Message;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.repository.entity.MessageEntity;
import at.ac.tuwien.sepm.groupphase.backend.repository.mapping.MessageEntityMapper;
import at.ac.tuwien.sepm.groupphase.backend.repository.mapping.UserEntityMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.String.format;

@Slf4j
@Repository
@Transactional
@RequiredArgsConstructor
public class MessageRepository {

    private final MessageJpaRepository repository;
    private final MessageEntityMapper messageMapper;
    private final UserEntityMapper userMapper;


    /**
     * Find all message entries ordered by publish date (ascending).
     *
     * @return ordered list of all message entries
     */
    public List<Message> findAllMessages() {
        return repository.findAllByOrderByPublishedAtDesc()
            .stream().map(messageMapper::toDomain).collect(Collectors.toList());
    }

    /**
     * Find specific message entry by ID.
     *
     * @return specified message
     * @throws NotFoundException will be thrown if the message could not be found in the system
     */
    public Message findById(Long id) {
        return repository.findById(id)
            .map(messageMapper::toDomain)
            .orElseThrow(() -> new NotFoundException(
                format("No message with given ID \"%s\" exists", id)
            ));
    }

    /**
     * Save a message entry in the database
     *
     * @return the saved message
     */
    public Message saveMessage(Message message) {
        MessageEntity saved = repository.save(messageMapper.toEntity(message));
        return messageMapper.toDomain(saved);
    }

    /**
     * Get the amount of messages in the database.
     *
     * @return total number of messages
     */
    public long numberOfMessages() {
        return repository.count();
    }


    /**
     * Get a page of the provided user's unread message entries ordered by published at date (descending).
     *
     * @param user whose unread Messages shall be retrieved
     * @param page information of page (page number and page size)
     * @return Page of unread message entries
     */
    public Page<Message> pageFindAllUnreadMessages(final ApplicationUser user, final Pageable page) {
        return repository.findAllByUsersNotContaining(userMapper.toEntity(user), page)
            .map(messageMapper::toDomain);
    }

    /**
     * Get a page of the provided user's read message entries ordered by published at date (descending).
     *
     * @param user whose read Messages shall be retrieved
     * @param page information of page (page number and page size)
     * @return Page of read message entries
     */
    public Page<Message> pageFindAllReadMessages(final ApplicationUser user, final Pageable page) {
        return repository.findAllByUsersContaining(userMapper.toEntity(user), page)
            .map(messageMapper::toDomain);
    }


    /**
     * Deletes all entries in the Read Table associated with the provided User
     *
     * @param user whose entries in the read (messages) table shall be deleted
     */
    public void deleteUserFromReadTable(ApplicationUser user){
        repository.deleteAllByUsersContaining(userMapper.toEntity(user));
    }
}
