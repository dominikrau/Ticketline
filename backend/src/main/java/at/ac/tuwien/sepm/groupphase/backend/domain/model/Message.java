package at.ac.tuwien.sepm.groupphase.backend.domain.model;

import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;
import java.util.List;

@Value
@Builder(builderClassName = "Builder", toBuilder = true)
public class Message {
    Long id;
    String title;
    String summary;
    String text;
    LocalDateTime publishedAt;
    String imageUrl;
    List<ApplicationUser> users;
}
