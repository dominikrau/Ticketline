package at.ac.tuwien.sepm.groupphase.backend.unittests;

import at.ac.tuwien.sepm.groupphase.backend.basetest.TestData;
import at.ac.tuwien.sepm.groupphase.backend.domain.model.Show;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.ShowDto;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper.ShowMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;


@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles("test")
public class ShowMappingTest implements TestData {

    private final Show show = Show.builder()
        .showId(ID)
        .createdAt(TEST_SHOW_CREATED_AT)
        .startTime(TEST_SHOW_START_TIME)
        .endTime(TEST_SHOW_END_TIME)
        .build();
    @Autowired
    private ShowMapper showMapper;

    @Test
    public void givenNothing_whenMapShowDtoToEntity_thenEntityHasAllProperties() {
        ShowDto showDto = showMapper.showToShowDto(show);
        assertAll(
            () -> assertEquals(ID, showDto.getShowId()),
            () -> assertEquals(TEST_SHOW_CREATED_AT, showDto.getCreatedAt()),
            () -> assertEquals(TEST_SHOW_START_TIME, showDto.getStartTime()),
            () -> assertEquals(TEST_SHOW_END_TIME, showDto.getEndTime())
        );
    }

    @Test
    public void givenNothing_whenMapListWithTwoShowEntitiesToSimpleDto_thenGetListWithSizeTwoAndAllProperties() {
        List<Show> shows = new ArrayList<>();
        shows.add(show);
        shows.add(show);

        List<ShowDto> showDtos = showMapper.showToShowDto(shows);
        assertEquals(2, showDtos.size());
        ShowDto showDto = showDtos.get(0);
        assertAll(
            () -> assertEquals(ID, showDto.getShowId()),
            () -> assertEquals(TEST_SHOW_CREATED_AT, showDto.getCreatedAt()),
            () -> assertEquals(TEST_SHOW_START_TIME, showDto.getStartTime()),
            () -> assertEquals(TEST_SHOW_END_TIME, showDto.getEndTime())
        );
    }


}
