package at.ac.tuwien.sepm.groupphase.backend.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@Getter
@AllArgsConstructor
public enum Gender {

    MALE("M"),
    FEMALE("F"),
    OTHER("O");

    private final String code;

    /**
     * converts the String Code into the Gender from the enumeration
     * @param code the string describing the Gender
     * @return the ENUM describing the Gender
     */
    public static Gender fromCode(String code) {
        return Arrays.stream(values())
            .filter(gender -> code.equals(gender.getCode()))
            .findFirst()
            .orElse(null);
    }

}
