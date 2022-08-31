package at.ac.tuwien.sepm.groupphase.backend.unittests;

import at.ac.tuwien.sepm.groupphase.backend.basetest.TestData;
import at.ac.tuwien.sepm.groupphase.backend.domain.model.Message;
import at.ac.tuwien.sepm.groupphase.backend.repository.MessageRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@Slf4j
@SpringBootTest
@Transactional
public class MessageRepositoryTest implements TestData {

    @Autowired
    MessageRepository messageRepository;

    @Test
    public void givenNothing_whenSaveMessage_thenFindListWithOneElementAndFindMessageById() {
        Message message = Message.builder()
            .title(TEST_NEWS_TITLE)
            .summary(TEST_NEWS_SUMMARY)
            .text(TEST_NEWS_TEXT)
            .publishedAt(TEST_NEWS_PUBLISHED_AT)
            .imageUrl(TEST_EVENT_IMAGEURL)
            .build();

        int currentCount = messageRepository.findAllMessages().size();

        Long id = messageRepository.saveMessage(message).getId();

        assertAll(
            () -> assertEquals(currentCount + 1, messageRepository.findAllMessages().size()),
            () -> assertNotNull(messageRepository.findById(id))
        );
    }

}
