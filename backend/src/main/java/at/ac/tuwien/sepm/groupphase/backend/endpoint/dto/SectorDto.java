package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Value;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.List;

@Value
@Builder(builderClassName = "Builder", toBuilder = true)
public class SectorDto {

    Long id;
    @NotBlank
    @Size(max = 255)
    String name;
    @NotBlank
    @Pattern(regexp = "^#[0-9a-fA-F]{6}$", message = "color has to be a hexcolorcode (# followed by 6 hexnumber)")
    String color;
    @NotBlank
    @Pattern(regexp = "^(?:sitting|standing)$", message = "sector must be from type sitting or standing")
    String type;
    List< @Valid SeatDto> seats;
    @Min(value = 0)
    Integer x;
    @Min(value = 0)
    Integer y;
    @Min(value = 0)
    Integer width;
    @Min(value = 0)
    Integer height;
    @Min(value = 0)
    Integer capacity;
    Integer available;

    @JsonCreator

    public SectorDto(
        @JsonProperty("sectorId") Long id,
        @NotBlank @JsonProperty("name") String name,
        @NotBlank @JsonProperty("color") String color,
        @NotBlank @JsonProperty("type") String type,
        @JsonProperty("seats") List<SeatDto> seats,
        @JsonProperty("x") Integer x,
        @JsonProperty("y") Integer y,
        @JsonProperty("width") Integer width,
        @JsonProperty("height") Integer height,
        @JsonProperty("capacity") Integer capacity,
        @JsonProperty("available") Integer available
    ) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.type = type;
        this.seats = seats;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.capacity = capacity;
        this.available = available;
    }
}
