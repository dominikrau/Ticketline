package at.ac.tuwien.sepm.groupphase.backend.endpoint.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomPage<T> {

    private List<T> content;
    private int numberOfPages;
    private long total;
    private int size;
    private int current;

    public static <T> CustomPage<T> of(Page<T> page) {
        return new CustomPage<>(
            page.getContent(),
            page.getTotalPages(),
            page.getTotalElements(),
            page.getSize(),
            page.getNumber()
        );
    }

}
