package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Slf4j
@Value
@Builder(builderClassName = "Builder", toBuilder = true)
public class EventDto {

    Long id;
    @NotBlank
    String name;
    @NotBlank
    String description;
    @NotBlank
    String eventType;
    @NotBlank
    String imageUrl;

    List<ArtistDto> artists;

    @JsonCreator
    public EventDto(
        @JsonProperty("id") Long id,
        @NotBlank @JsonProperty("name") String name,
        @NotBlank @JsonProperty("description") String description,
        @NotBlank @JsonProperty("eventType") String eventType,
        @NotBlank @JsonProperty ("imageUrl") String imageUrl,
        @NotBlank @JsonProperty ("artists") List<ArtistDto> artists
        ) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.eventType = eventType;
        this.imageUrl = imageUrl;
        this.artists = artists;
    }

}
