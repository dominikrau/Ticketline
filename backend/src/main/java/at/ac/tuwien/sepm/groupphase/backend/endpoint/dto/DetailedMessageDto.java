package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Value
@Builder(builderClassName = "Builder", toBuilder = true)
public class DetailedMessageDto {


    Long id;

    LocalDateTime publishedAt;

    @NotNull(message = "Title must not be null")
    @Size(max = 100)
    String title;

    @NotNull(message = "Summary must not be null")
    @Size(max = 500)
    String summary;

    String text;

    @NotNull(message = "imageUrl must not be null")
    String imageUrl;

    @JsonCreator
    public DetailedMessageDto(
        @JsonProperty("id") Long id,
        @JsonProperty("publishedAt") LocalDateTime publishedAt,
        @NotNull @JsonProperty("title") String title,
        @NotNull @JsonProperty("summary") String summary,
        @JsonProperty("text") String text,
        @NotNull @JsonProperty("imageUrl") String imageUrl) {
        this.id = id;
        this.publishedAt = publishedAt;
        this.title = title;
        this.summary = summary;
        this.text = text;
        this.imageUrl = imageUrl;
    }
}