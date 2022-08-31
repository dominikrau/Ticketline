package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Value
@Builder(builderClassName = "Builder", toBuilder = true)
public class MessageInquiryDto {

    @NotNull(message = "Title must not be null")
    @Size(max = 100)
    String title;

    @NotNull(message = "Summary must not be null")
    @Size(max = 500)
    String summary;

    @NotNull(message = "Text must not be null")
    @Size(max = 10000)
    String text;

    @NotNull(message = "imageUrl must not be null")
    String imageUrl;

    @JsonCreator
    public MessageInquiryDto(
        @NotNull @JsonProperty("title") String title,
        @NotNull @JsonProperty("summary") String summary,
        @NotNull @JsonProperty("text") String text,
        @NotNull @JsonProperty("imageUrl") String imageUrl) {
        this.title = title;
        this.summary = summary;
        this.text = text;
        this.imageUrl = imageUrl;
    }
}