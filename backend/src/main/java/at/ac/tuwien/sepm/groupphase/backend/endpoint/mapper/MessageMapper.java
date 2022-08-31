package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.domain.model.Message;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.DetailedMessageDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.MessageInquiryDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.SimpleMessageDto;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

import java.util.List;

@Mapper
public interface MessageMapper {
    /**
     * Maps the given Detailed Message DTO to a Domain representation of the Message
     * Detailed Message DTO has all properties
     *
     * @param detailedMessageDto the Message DTO to be mapped
     * @return the mapped Message
     */
    Message detailedMessageDtoToMessage(DetailedMessageDto detailedMessageDto);

    /**
     * Maps the given Message Inquiry DTO to a Domain representation of the Message
     * Message Inquiry DTO only has following properties: title, summary, text, imageUrl
     *
     * @param messageInquiryDto the Message DTO to be mapped
     * @return the mapped Message
     */
    Message messageInquiryDtoToMessage(MessageInquiryDto messageInquiryDto);


    /**
     * Maps the given Message to a Data Transfer Object representation of the Message
     *
     * @param message the Message to be mapped
     * @return the mapped Message DTO
     */
    DetailedMessageDto messageToDetailedMessageDto(Message message);


    /**
     * Maps the given Message to a Simple Data Transfer Object representation of the Message
     * This is necessary since the SimpleMessageDto misses the text property and the collection mapper can't handle
     * missing fields
     *
     * @param message the Message to be mapped
     * @return the mapped Simple Message DTO
     */
    @Named("simpleMessage")
    SimpleMessageDto messageToSimpleMessageDto(Message message);

    /**
     * Maps the given Messages in a List to a List of Simple Data Transfer Object representations of the Messages
     * This is necessary since the SimpleMessageDto misses the text property and the collection mapper can't handle
     * missing fields
     *
     * @param messages the List of Messages to be mapped
     * @return the mapped Simple Message DTO List
     */
    @IterableMapping(qualifiedByName = "simpleMessage")
    List<SimpleMessageDto> messageToSimpleMessageDto(List<Message> messages);


    /**
     * Maps the given Message to a Inquiry Data Transfer Object representation of the Message
     * This is necessary since the MessageInquiryDto misses the id and publishedAt property and the collection mapper can't handle
     * missing fields
     *
     * @param message the Message to be mapped
     * @return the mapped Message Inquiry DTO
     */
    MessageInquiryDto messageToMessageInquiryDto(Message message);
}

