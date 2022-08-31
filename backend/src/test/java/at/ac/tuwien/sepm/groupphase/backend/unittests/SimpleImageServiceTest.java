package at.ac.tuwien.sepm.groupphase.backend.unittests;

import at.ac.tuwien.sepm.groupphase.backend.basetest.TestData;
import at.ac.tuwien.sepm.groupphase.backend.service.ImageService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
public class SimpleImageServiceTest implements TestData {

    @Autowired
    ImageService imageService;

    @Test
    public void givenNothing_whenSaveImage_thenFoundImage() {
        String savedImage = imageService.save(TEST_EVENT_IMAGEURL);
        System.out.println(savedImage);
        Path filePath = Paths.get(IMAGE_UPLOAD_DIRECTORY + savedImage);
        assertTrue(Files.exists(filePath));
        this.imageService.deleteByFileName(savedImage);
    }

    @Test
    public void givenNothing_whenSaveAndDeleteImage_thenNotFoundImage() {
        String savedImage = imageService.save(TEST_EVENT_IMAGEURL);
        System.out.println(savedImage);
        Path filePath = Paths.get(IMAGE_UPLOAD_DIRECTORY + savedImage);
        this.imageService.deleteByFileName(savedImage);
        assertFalse(Files.exists(filePath));
    }

}
