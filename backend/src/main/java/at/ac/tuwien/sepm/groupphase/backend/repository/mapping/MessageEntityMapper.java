package at.ac.tuwien.sepm.groupphase.backend.repository.mapping;

import at.ac.tuwien.sepm.groupphase.backend.domain.model.Message;
import at.ac.tuwien.sepm.groupphase.backend.repository.entity.MessageEntity;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(uses = { UserEntityMapper.class})
public interface MessageEntityMapper {
    /**
     * Maps the given Message Entity to a Domain representation of the Message
     *
     * @param message the Message Entity to be mapped
     * @return the mapped Message
     */
    @Named("message")
    @Mapping(target = "imageUrl",
        expression =  "java(message.getImageUrl().startsWith(\"http\")?message.getImageUrl():\"http://localhost:8080/uploads/images/\" + message.getImageUrl() )")
    Message toDomain(MessageEntity message);

    /**
     * Maps the given Messages Entities in a List to a List of Domain representations of the Messages
     *
     * @param messageEntities the List of Message Entities to be mapped
     * @return the mapped Message List
     */
    @IterableMapping(qualifiedByName = "message")
    List<Message> toDomain(List<MessageEntity> messageEntities);

    /**
     * Maps the given Message to a Repository Entity representation of the Message
     *
     * @param message the Message to be mapped
     * @return the mapped Message Entity
     */
    MessageEntity toEntity(Message message);

}