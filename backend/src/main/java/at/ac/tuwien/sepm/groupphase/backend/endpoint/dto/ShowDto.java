package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;


@Value
@Builder(builderClassName = "Builder", toBuilder = true)
public class ShowDto {

    Long showId;
    LocalDateTime createdAt;
    @NotNull
    LocalDateTime startTime;
    @NotNull
    LocalDateTime endTime;
    @NotNull
    @Valid
    VenueDto venue;

    EventDto event;
    @NotNull
    @Valid
    List<PricingDto> pricings;
    @NotNull
    SeatingChartDto seatingChart;


    @JsonCreator
    public ShowDto(
        @JsonProperty("showId") Long showId,
        @JsonProperty("createdAt") LocalDateTime createdAt,
        @NotNull @JsonProperty("startTime") LocalDateTime startTime,
        @NotNull @JsonProperty("endTime") LocalDateTime endTime,
        @NotNull @JsonProperty("venue") VenueDto venue,
        @JsonProperty("event") EventDto event,
        @NotNull @Valid @JsonProperty("pricings") List<PricingDto> pricings,
        @NotNull @JsonProperty("seatingChart") SeatingChartDto seatingChart
    ){
        this.showId = showId;
        this.createdAt = createdAt;
        this.startTime = startTime;
        this.endTime = endTime;
        this.venue = venue;
        this.event = event;
        this.pricings = pricings;
        this.seatingChart = seatingChart;
    }

}
