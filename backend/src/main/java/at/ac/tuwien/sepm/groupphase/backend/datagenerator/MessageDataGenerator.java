package at.ac.tuwien.sepm.groupphase.backend.datagenerator;

import at.ac.tuwien.sepm.groupphase.backend.domain.model.Message;
import at.ac.tuwien.sepm.groupphase.backend.repository.MessageRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.ImageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;

@Slf4j
@Profile("generateData")
@Component
public class MessageDataGenerator {

    @Autowired
    ImageService imageService;

    private static final int NUMBER_OF_MESSAGES_TO_GENERATE = 15;
    private static final String TEST_NEWS_TEXT = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Quisque ultrices enim vehicula est imperdiet, at placerat odio dapibus. Praesent tellus ex, euismod in ipsum eget, lacinia tristique augue. Curabitur vehicula ligula at sapien feugiat, non placerat ligula ornare. Ut vitae consequat lacus. Donec consequat ex hendrerit neque luctus malesuada. Etiam sed ullamcorper sem. Phasellus at porta lacus, sed maximus felis. Donec viverra nisl magna, sed sagittis nisi egestas a.";
    private static final String[] MESSAGE_TITLES = {
        "Hansi Hinterseher announces new NÖ Tour",
        "Kanye's new scandal",
        "Salzburger Festspiele happening",
        "Open Air Cinemas to be opened",
        "Disney popular as always",

        "Nova Rock 2020 roster announced",
        "A new DJs union",
        "Mariah Carey turning 50",
        "Why K-Pop is so successful",
        "New HC Strache Rap",

        "Comeback of the 80s",
        "Vienna Scientists Symposium to be postponed",
        "Pitbull's new single",
        "N64 tournament in Graz",
        "Cats in Linz"
    };

    private static final String[] MESSAGE_SUMMARIES = {
        "The Hans touring again.",
        "Kanye West Tour cancelled",
        "Salzburger Festspiele will be held despite of Covid-19",
        "Open Air Cinemas to be opened in July in Vienna, Graz and Innsbruck",
        "No decline in popularity for the animation company after many decades in the business",

        "Lordi and Metallica among others",
        "DJs want better wages and conditions",
        "The diva turns half a century",
        "Funky hair and thick beats",
        "The master of shamelessness at it again",

        "Bring out the neon lights",
        "January 2021 planned for the VSS",
        "\"Baila con Fuego\" hitting the charts!",
        "Speedruns and other categories announced",
        "Cats. Again. And again."
    };

    private static final String[] IMAGE_URLS = {
        "http://localhost:8080/testdata/hansi-hinterseher.jpg",
        "http://localhost:8080/testdata/kanye-west.jpg",
        "http://localhost:8080/testdata/salzburger-festspiele.jpg",
        "http://localhost:8080/testdata/open-air-kino.jpg",
        "http://localhost:8080/testdata/disney.jpg",

        "http://localhost:8080/testdata/nova-rock-2020.jpg",
        "http://localhost:8080/testdata/dj-union.jpg",
        "http://localhost:8080/testdata/mariah-carey.jpg",
        "http://localhost:8080/testdata/k-pop.jpg",
        "http://localhost:8080/testdata/hc-strache.jpg",

        "http://localhost:8080/testdata/80s.jpg",
        "http://localhost:8080/testdata/vienna-scientists-symposium.jpg",
        "http://localhost:8080/testdata/pitbull.jpg",
        "http://localhost:8080/testdata/n64.jpg",
        "http://localhost:8080/testdata/cats.jpg",
    };

    private final MessageRepository messageRepository;

    public MessageDataGenerator(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    @PostConstruct
    private void generateMessage() {
        if (!messageRepository.findAllMessages().isEmpty()) {
            log.debug("message already generated");
        } else {
            log.debug("generating {} message entries", NUMBER_OF_MESSAGES_TO_GENERATE);

            for (int i = 0; i < NUMBER_OF_MESSAGES_TO_GENERATE; i++) {
                Message message = Message.builder()
                    .title(MESSAGE_TITLES[i])
                    .summary(MESSAGE_SUMMARIES[i])
                    .text(TEST_NEWS_TEXT + " " + i)
                    .publishedAt(LocalDateTime.now().minusMonths(i))
                    .imageUrl(IMAGE_URLS[i])
                    .build();
                log.debug("saving message {}", message);
                messageRepository.saveMessage(message);
            }
        }
    }

}
