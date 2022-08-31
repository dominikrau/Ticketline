package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.domain.model.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.domain.model.Message;
import at.ac.tuwien.sepm.groupphase.backend.repository.MessageRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.ImageService;
import at.ac.tuwien.sepm.groupphase.backend.service.MessageService;
import at.ac.tuwien.sepm.groupphase.backend.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SimpleMessageService implements MessageService {

    private final MessageRepository messageRepository;
    private final UserService userService;
    private final ImageService imageService;

    @Override
    public List<Message> findAll() {
        log.debug("Find all messages");
        return messageRepository.findAllMessages();
    }

    @Override
    public Page<Message> pageFindUnreadMessages(Pageable pageable) {
        ApplicationUser user = userService.getCurrentUser();
        return messageRepository.pageFindAllUnreadMessages(user, pageable);
    }

    @Override
    public Page<Message> pageFindReadMessages(Pageable pageable) {
        ApplicationUser user = userService.getCurrentUser();
        return messageRepository.pageFindAllReadMessages(user, pageable);
    }


    @Override
    public Message findOne(Long id) {
        log.debug("Find message with id {}", id);
        return messageRepository.findById(id);
    }

    @Override
    public Message readOneById(Long id){
        Message message = messageRepository.findById(id);
        ApplicationUser user = userService.getCurrentUser();
        List<ApplicationUser> newUsers = new LinkedList<>();
        newUsers.add(user);
        Message newMessage = message.toBuilder()
            .users(newUsers)
            .build();
        return messageRepository.saveMessage(newMessage);
    }



    @Override
    public Message createMessage(Message message) {
        log.debug("Publish new message {}", message);
        String savedImage = imageService.save(message.getImageUrl());
        return messageRepository.saveMessage(message.toBuilder()
            .publishedAt(LocalDateTime.now())
            .imageUrl(savedImage)
            .build()
        );
    }

}
