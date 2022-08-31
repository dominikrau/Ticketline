package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Setter;
import lombok.Value;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.Instant;
import java.util.List;

@Value
@Builder(builderClassName = "Builder", toBuilder = true)
public class SeatingChartDto {

    Long id;
    Instant createdAt;
    @NotBlank
    @Size(max = 255)
    String name;
    @NotNull
    @Valid
    List<SectorDto> sectors;
    HallDto hall;
    @Valid
    @NotNull
    StageDto stage;

    @JsonCreator
    public SeatingChartDto(
        @JsonProperty("seatingChartId") Long id,
        @JsonProperty("createdAt") Instant createdAt,
        @NotBlank @JsonProperty("name") String name,
        @NotNull @JsonProperty("sectors") List<SectorDto> sectors,
        @JsonProperty("hall") HallDto hall,
        @Valid @NotNull @JsonProperty("stage") StageDto stage
    ) {
        this.id = id;
        this.createdAt = createdAt;
        this.name = name;
        this.sectors = sectors;
        this.hall = hall;
        this.stage = stage;
    }
}
