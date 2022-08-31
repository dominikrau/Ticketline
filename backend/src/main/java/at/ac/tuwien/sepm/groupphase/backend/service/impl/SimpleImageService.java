package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.exception.ValidationException;
import at.ac.tuwien.sepm.groupphase.backend.service.ImageService;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Slf4j
@Service
public class SimpleImageService implements ImageService {

    static final String DIRECTORY = "./public/uploads/images/";
    static final int WIDTH = 800;
    static final int HEIGHT = 600;

    @Override
    public String save(String base64Uri) {
        log.debug("Save Image");
        String output = "";
        String[] uriComponents = base64Uri.split(",");
        String extension;
        switch (uriComponents[0]) {//check image extension
            case "data:image/jpeg;base64":
                extension = "jpeg";
                break;
            case "data:image/png;base64":
                extension = "png";
                break;
            default:
                throw new ValidationException("Image Format is not supported!");
        }
        try {
            Path filePath = Paths.get(DIRECTORY);
            Files.createDirectories(Paths.get(DIRECTORY));
            byte[] imageByte = Base64.decodeBase64(uriComponents[1]);

            BufferedImage image = ImageIO.read(new ByteArrayInputStream(imageByte));


            BufferedImage resized = resize(image, WIDTH, HEIGHT);

            Path created = Files.createTempFile(filePath, "image-", "." + extension);

            ImageIO.write(resized, extension, new File(String.valueOf(created)));
            output = created.getFileName().toString();
        } catch (java.io.IOException e) {
            log.error(e.getMessage(), e);
        }
        return output;
    }

    @Override
    public boolean deleteByFileName(String filename) {
        log.debug("Delete by filename {}", filename);
        try {
            return Files.deleteIfExists(Paths.get(DIRECTORY + filename));
        } catch (IOException e) {
            log.error("error deleting file!", e);
        }
        return false;
    }

    private static BufferedImage resize(BufferedImage img, int width, int height) {
        log.debug("Resize buffered image");
        int imgWidth = img.getWidth();
        int imgHeight = img.getHeight();
        int offsetY = 0;
        int offsetX = 0;

        if (imgWidth * height < width * imgHeight) { // img too tall
            imgHeight = imgWidth * height / width;
            offsetY = (img.getHeight() - imgHeight) / 2;
        } else { // img too wide
            imgWidth = imgHeight * width / height;
            offsetX = (img.getWidth() - imgWidth) / 2;
        }
        BufferedImage resized = img.getSubimage(offsetX, offsetY, imgWidth, imgHeight);

        Image tmp = resized.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        resized = new BufferedImage(width, height, img.getType());
        Graphics2D g2d = resized.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();

        return resized;
    }
}
