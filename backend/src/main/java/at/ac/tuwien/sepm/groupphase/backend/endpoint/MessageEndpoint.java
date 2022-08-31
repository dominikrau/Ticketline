package at.ac.tuwien.sepm.groupphase.backend.endpoint;

import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.CustomPage;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.DetailedMessageDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.MessageInquiryDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SimpleMessageDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.MessageMapper;
import at.ac.tuwien.sepm.groupphase.backend.service.MessageService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Authorization;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/api/v1/messages")
@Slf4j
public class MessageEndpoint {

    private final MessageService messageService;
    private final MessageMapper messageMapper;

    @Autowired
    public MessageEndpoint(MessageService messageService, MessageMapper messageMapper) {
        this.messageService = messageService;
        this.messageMapper = messageMapper;
    }


    /**
     * Accepts the GET-Request and proceeds to search for the unread Messages of the current User
     *
     * @param page number of the Page to be retrieved
     * @param size of the Page to be retrieved
     * @return A Page of unread Message DTOs
     */
    @GetMapping
    @ApiOperation(value = "Get list of unread messages without details", authorizations = {@Authorization(value = "apiKey")})
    public CustomPage<SimpleMessageDto> findAllUnread(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size
    ) {
        log.info("GET /api/v1/messages");
        return CustomPage.of(messageService.pageFindUnreadMessages(PageRequest.of(page, size))
            .map(messageMapper::messageToSimpleMessageDto));
    }

    /**
     * Accepts the GET-Request and proceeds to search for the read Messages of the current User
     *
     * @param page number of the Page to be retrieved
     * @param size of the Page to be retrieved
     * @return A Page of read Message DTOs
     */
    @GetMapping(value = "/read")
    @ApiOperation(value = "Get list of read messages without details", authorizations = {@Authorization(value = "apiKey")})
    public CustomPage<SimpleMessageDto> findAllRead(
        @RequestParam("page") int page,
        @RequestParam("size") int size
    ) {
        log.info("GET /api/v1/messages/read");
        return CustomPage.of(messageService.pageFindReadMessages(PageRequest.of(page, size))
            .map(messageMapper::messageToSimpleMessageDto));
    }



    /**
     * Accepts the GET-Request and proceeds to retrieve a single Message entry
     *
     * @param id of the Message to be retrieved
     * @return the Message DTO of the specified Message
     */
    @GetMapping(value = "/{id}")
    @ApiOperation(value = "Get detailed information about a specific message",
        authorizations = {@Authorization(value = "apiKey")})
    public DetailedMessageDto find(@PathVariable Long id) {
        log.info("GET /api/v1/messages/{}", id);
        return messageMapper.messageToDetailedMessageDto(messageService.findOne(id));
    }

    /**
     * Accepts the GET-Request and proceeds to retrieve a single Message entry while marking the message as read
     *
     * @param id of the Message to be retrieved and marked as read
     * @return the Message DTO of the specified Message
     */
    @GetMapping(value = "/read/{id}")
    @ApiOperation(value = "Get detailed information about a specific message and mark it as read",
        authorizations = {@Authorization(value = "apiKey")})
    public DetailedMessageDto readMessage(@PathVariable Long id) {
        log.info("GET /api/v1/messages/{} and mark as read", id);
        return messageMapper.messageToDetailedMessageDto(messageService.readOneById(id));
    }


    /**
     * Accepts the POST-Request and proceeds to create a new Message entry
     *
     * @param messageDto of the Message to be created
     * @return the Message DTO of the newly created Message (including ID and createdAt)
     */
    @Secured("ROLE_ADMIN")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    @ApiOperation(value = "Publish a new message", authorizations = {@Authorization(value = "apiKey")})
    public DetailedMessageDto create(@Valid @RequestBody MessageInquiryDto messageDto) {
        log.info("POST /api/v1/messages body: {}", messageDto);
        return messageMapper.messageToDetailedMessageDto(
            messageService.createMessage(messageMapper.messageInquiryDtoToMessage(messageDto)));
    }
}
