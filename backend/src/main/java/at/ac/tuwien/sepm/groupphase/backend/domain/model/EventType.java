package at.ac.tuwien.sepm.groupphase.backend.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum EventType {

    CONCERTS("CONCERTS"),
    CULTURE("CULTURE"),
    SPORTS("SPORTS"),
    MUSICAL("MUSICAL"),
    SHOW("SHOW"),
    CABARET("CABARET"),
    COMEDY("COMEDY"),
    OTHER("OTHER");

    private final String code;

    /**
     * converts the String Code into the EventType from the enumeration
     * @param code the string describing the EventType
     * @return the ENUM describing the EventType
     */
    public static EventType fromCode(String code) {
        return Arrays.stream(values())
            .filter(eventType -> eventType.getCode().equals(code))
            .findFirst()
            .orElse(null);
    }


}
