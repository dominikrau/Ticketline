package at.ac.tuwien.sepm.groupphase.backend.datagenerator;

import at.ac.tuwien.sepm.groupphase.backend.domain.model.*;
import at.ac.tuwien.sepm.groupphase.backend.repository.*;
import at.ac.tuwien.sepm.groupphase.backend.service.impl.SimpleOrderService;
import at.ac.tuwien.sepm.groupphase.backend.service.impl.SimpleSeatingChartService;
import at.ac.tuwien.sepm.groupphase.backend.service.impl.SimpleShowService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Profile("generateData")
@Component
@Transactional
@RequiredArgsConstructor
public class GeneralDataGenerator {

    private final HallRepository hallRepository;
    private final VenueRepository venueRepository;
    private final SimpleSeatingChartService seatingChartService;
    private final ArtistRepository artistRepository;


    //address
    private static final String[] STREET_NAMES = {"49th street", "Baker Street", "Abbey Road",
        "Albert Street", "New Street", "Henslowe Passage", "Waverley Place", "Wisteria Lane",
        "Evergreen Terrace"};

    private static final String[] CITIES = {"New York", "London",
        "Chicago", "Bristol", "Dublin"};

    private static final String[] COUNTRIES = {"Great Britain", "USA", "Ireland"};

    private String getRandom(String[] strings) {
        return strings[(int) (Math.random()*(strings.length))];
    }

    private Address generateAddress() {
        return Address.builder()
            .country(getRandom(COUNTRIES))
            .postalCode(String.valueOf((int) (Math.random()*100000)))
            .city(getRandom(CITIES))
            .street(getRandom(STREET_NAMES) + " " + (int) (Math.random()*250))
            .build();
    }

    //venue
    private static final String[] VENUE_NAMES = {"City Hall", "Bungalow",
    "Club Pogo", "Concert House", "Stadium", "Music Arena", "Westside", "Parlament",
    "The little Bar", "Frozen Palace", "Red Palace", "Mario's Dreamland",
    "Poetry Club", "Spinning Wheel", "French Place", "New Castle", "Mc Donalds",
    "Fight Club", "British World", "Ottakringer Brauerei", "Festival Area", "Mozart's living Room",
    "Ben and Jerry's", "Coliseum", "Great Wall of China"};


    private void saveVenues() {
        if (!venueRepository.findAllByOrderNameAsc().isEmpty()) {
            log.debug("venues already generated");
        } else {
            for (String venueName : VENUE_NAMES) {
                Venue venue = Venue.builder()
                    .name(venueName)
                    .address(generateAddress())
                    .build();
                venueRepository.saveVenue(venue);
            }
        }

    }



    private void saveHalls() {
        if (!hallRepository.findAllByOrderByNameAsc().isEmpty()) {
            log.debug("halls already generated");
        } else {
            saveVenues();
            List<Venue> venues = venueRepository.findAllByOrderNameAsc();
            for (Venue venue : venues
            ) {
                Hall smallHall = Hall.builder()
                    .name("Small Hall")
                    .venue(venue)
                    .height(25)
                    .width(25)
                    .build();
                Hall savedSmallHall = hallRepository.saveHall(smallHall);
                saveSmallSeatingChart(savedSmallHall);
                Hall bigHall = Hall.builder()
                    .name("Big Hall")
                    .venue(venue)
                    .height(40)
                    .width(30)
                    .build();
                Hall savedBigHall = hallRepository.saveHall(bigHall);
                saveBigSeatingChart(savedBigHall);

            }
        }
    }

    // seatingchart

    private void saveBigSeatingChart(Hall hall) {
        SeatingChart seatingChart = SeatingChart.builder()
            .hall(hall)
            .sectors(generateSectorsForBigSeatingChart())
            .name("Big Seating Chart")
            .stage(generateBigStage())
            .build();
        seatingChartService.createSeatingChart(seatingChart);
    }

    private void saveSmallSeatingChart(Hall hall) {
        SeatingChart seatingChart = SeatingChart.builder()
            .hall(hall)
            .sectors(generateSectorsForSmallSeatingChart())
            .name("Small Seating Chart")
            .stage(generateSmallStage())
            .build();
        seatingChartService.createSeatingChart(seatingChart);
    }

    //seat
    private Seat generateSeat(int x, int y) {
        return Seat.builder()
            .x(x)
            .y(y)
            .build();
    }

    //seats
    private List<Seat> generateSeats(int x, int y, int width, int height) {
        List<Seat> seats = new ArrayList<>();
        for (int i = x; i < x + width; i++) {
            for (int j = y; j < y + height; j++) {
                seats.add(generateSeat(i,j));
            }
        }
        return seats;
    }

    //sittingSector
    private SittingSector generateSittingSector(int x, int y, int width, int height, String sectorName) {
        return SittingSector.builder()
            .color("#" + Integer.toHexString((int) (Math.random()*(16777216/2))+(16777216/2)))
            .name(sectorName)
            .seats(generateSeats(x,y,width,height))
            .build();
    }

    //standingSector
    private StandingSector generateStandingSector(int x, int y, int width, int height, int capacity, String sectorName) {
        return StandingSector.builder()
            .color("#" + Integer.toHexString((int) (Math.random()*(16777216/2))+(16777216/2)))
            .name(sectorName)
            .x(x)
            .y(y)
            .width(width)
            .height(height)
            .capacity(capacity)
            .build();
    }



    private List<Sector> generateSectorsForBigSeatingChart() {
        List<Sector> sectors = new ArrayList<>();
        Sector sector1 = generateSittingSector(10,8,10,10,"Category A");
        sectors.add(sector1);
        Sector sector2 = generateSittingSector(4,20,10,10,"Category B");
        sectors.add(sector2);
        Sector sector3 = generateSittingSector(16,20,10,10,"Category C");
        sectors.add(sector3);
        Sector sector4 = generateStandingSector(2,35,26,5, 50, "Standing");
        sectors.add(sector4);
        return sectors;
    }

    private List<Sector> generateSectorsForSmallSeatingChart() {
        List<Sector> sectors = new ArrayList<>();
        Sector sector1 = generateSittingSector(2,10,10,8,"Category A");
        sectors.add(sector1);
        Sector sector2 = generateSittingSector(13,10,10,8,"Category B");
        sectors.add(sector2);
        Sector sector3 = generateStandingSector(2,20,21,3, 20, "Standing");
        sectors.add(sector3);
        return sectors;
    }

    //stage
    private Stage generateSmallStage() {
        return Stage.builder()
            .y(2)
            .height(6)
            .width(11)
            .x(7)
            .build();
    }

    private Stage generateBigStage() {
        return Stage.builder()
            .y(0)
            .height(4)
            .width(30)
            .x(0)
            .build();
    }

    //artist
    private static final String[] ARTIST_FIRSTNAMES = {"Justin","Armando", "Omar", "Gerhard",
    "John", "Romy", "Robert", "Julian", "Sebastian", "Michael"};

    private static final String[] ARTIST_LASTNAMES = {"Bieber","Perez", "Ernesto", "Friedle",
    "Depp", "Schneider", "Zimmermann", "Sellmeister", "Meisinger", "Niavarani"};

    private static final String[] ARTIST_PSEUDONYM = {"JB", "Pitbull", "Semino Rossi", "DJ Ötzi",
    "Johnny Depp", "Romylein", "Bob Dylan", "Yung hurn", "Moneyboy", "Michael Niavarani"};

    private Artist generateArtist(int number) {
            return Artist.builder()
                .firstName(ARTIST_FIRSTNAMES[number])
                .lastName(ARTIST_LASTNAMES[number])
                .pseudonym(ARTIST_PSEUDONYM[number])
                .build();

    }



    //event
    private static final String[] EVENT_NAMES = {
        "Semino Rossi the most beautiful man",
        "The Voice of Germany semi-final",
        "Two Times Timmy Trumpet Tour",
        "Four Times Timmy Trumpet Tour",
        "Disney Live in Concert",

        "Nova Rock 2020",
        "Frequency Festival 2020",
        "The BIG FAT Festival Berlin",
        "Epic Movie Sounds Live For 3 Days",
        "Max Strichinger about life",

        "Young Engineers Talk 2020",
        "Vienna Scientists Symposium",
        "Event 10",
        "Epic Game Sounds Live For 24 Hours",
        "Virtual Zoom Sessions Live",

        "RTL Schlager Party",
        "Ticketline Presentation",
        "Weird Acts all Night",
        "Bibis Beauty Saloon live",
        "Gamescom",

        "Shadow Gladiator Fight",
        "LI Superbowl",
        "Best of James Bond",
        "Mainframe for Youngsters",
        "Computer Science Workshop"
    };
    private static final EventType[] EVENT_TYPES = new EventType[]{EventType.SPORTS, EventType.OTHER,
    EventType.COMEDY, EventType.CULTURE, EventType.MUSICAL, EventType.CABARET, EventType.CONCERTS, EventType.SHOW};

    private final EventRepository eventRepository;

    private void saveEvents() {
        if (!eventRepository.findAllEvents().isEmpty()) {
            log.debug("events already generated");
        } else {
            for (int i = 0; i < 8; i++) {
                for (String eventName : EVENT_NAMES
                ) {
                    Artist artist = artistRepository.save(generateArtist((int) (Math.random() * ARTIST_FIRSTNAMES.length)));
                    EventType eventType = EVENT_TYPES[(int) (Math.random() * EVENT_TYPES.length)];
                    Event event = Event.builder()
                        .artist(artist)
                        .eventType(eventType)
                        .name(eventName + i)
                        .description(eventName + i + " is a great " + eventType + " Event, where the wonderful " + artist.getPseudonym() +
                            " (" + artist.getLastName() + ", " + artist.getFirstName() + ") will be on stage")
                        .imageUrl("http://localhost:8080/testdata/" + eventName.toLowerCase().replace(" ", "-")
                            + ".jpg")
                        .build();
                    eventRepository.saveEvent(event);
                }
            }
        }
    }

    //pricing
    private Pricing generatePricing(Sector sector, Double price) {
        return Pricing.builder()
            .price(price)
            .sector(sector)
            .build();
    }

    private List<Pricing> generatePricings(SeatingChart seatingChart) {
        List<Sector> sectors = seatingChart.getSectors();
        List<Pricing> pricings = new ArrayList<>();
        Double price = 60.00;
        for (Sector sector: sectors
             ) {
            Pricing pricing = generatePricing(sector, price);
            pricings.add(pricing);
            price -= 10;
        }
        return pricings;
    }

    //shows
    private static final LocalDateTime TEST_SHOW_START_TIME = LocalDateTime.now().plusHours(10).withMinute(30).withSecond(0).withNano(0);
    private final SimpleShowService showService;
    private final ShowRepository showRepository;

    //@PostConstruct
    private void saveShows() {
        if (showRepository.numberOfShows() > 0) {
            log.debug("shows already generated");
        } else {
            saveHalls();
            saveEvents();
            List<SeatingChart> seatingCharts = seatingChartService.findAll();
            List<Event> events = eventRepository.findAllEvents();
            for (int i = 0; i < 1000; i++) {
                SeatingChart seatingChart = seatingCharts.get((int) (Math.random() * seatingCharts.size()));
                Event event = events.get((int) (Math.random() * events.size()));
                Show show = Show.builder()
                    .seatingChart(seatingChart)
                    .startTime(TEST_SHOW_START_TIME.plusHours(8L * i))
                    .endTime(TEST_SHOW_START_TIME.plusHours(8L * i + 2L).plusMinutes(30))
                    .event(event)
                    .venue(seatingChart.getHall().getVenue())
                    .pricings(generatePricings(seatingChart))
                    .build();
                showService.createShow(show, event.getId());
            }
        }
    }

    //user
    private static final String[] USER_FIRSTNAMES = {"Günther", "John", "Herbert", "Max", "Dominik",
    "Sebastian", "Eduardo", "Leon", "Marie", "Constanze", "Christina", "Jerry", "Jaqueline", "Sophie",
    "Elena", "Jasmin", "Sarah", "Elfi"};
    private static final String[] USER_LASTNAMES = {"Müller", "Wagner", "Gangl","Friedrich", "Clemens",
    "Naratu", "Vasic", "Tischler", "Schneider", "O'Conner", "Strampl", "Tommer",};

    private static final Gender[] USER_GENDERS = {Gender.OTHER, Gender.FEMALE, Gender.MALE};
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final UserDataGenerator userDataGenerator;

    private void saveUser(int uniqueEmailNumber) {
        String firstname = getRandom(USER_FIRSTNAMES);
        String lastname = getRandom(USER_LASTNAMES);

        ApplicationUser user = ApplicationUser.builder()
            .firstName(firstname)
            .lastName(lastname)
            .address(generateAddress())
            .emailAddress(firstname + "." + lastname + "@" + uniqueEmailNumber + ".com")
            .dateOfBirth(LocalDate.of(1970,5,10))
            .gender(USER_GENDERS[(int) (Math.random()*3)])
            .password(passwordEncoder.encode("password1"))
            .role("ROLE_USER")
            .blocked(false)
            .build();
        userRepository.saveUser(user);
    }


    private void saveUsers() {
        if (!userRepository.findAllUsers().isEmpty()) {
            log.debug("users already generated");
        } else {
            userDataGenerator.generateAdminUser();
            for (int i = 0; i < 1000; i++) {
                saveUser(i);
            }
        }
    }

    //order
    private static final PaymentMethod[] PAYMENT_METHODS = {
        PaymentMethod.RESERVATION, PaymentMethod.BANK_TRANSFER,
        PaymentMethod.CREDIT_CARD, PaymentMethod.PAY_PAL
    };
    private final SimpleOrderService orderService;
    private final OrderRepository orderRepository;
    private final TicketRepository ticketRepository;

    private List<Long> selectSeats(Show show) {
        List<Seat> seats = new ArrayList<>();
        List<Sector> sectors = show.getSeatingChart().getSectors();
        for (Sector sector: sectors
             ) {
            if(sector instanceof SittingSector) {
                seats.addAll(((SittingSector) sector).getSeats());

            }
        }
        int count = (int) (Math.random()*5);
        Set<Long> seatIds = new HashSet<>();
        for (int i = 0; i < count; i++) {
            Seat seat = seats.get((int) (Math.random()*seats.size()));
            if(ticketRepository.isSeatAvailable(seat.getId(),show.getShowId())) {
                seatIds.add(seat.getId());

            }
        }

        return new ArrayList<>(seatIds);
    }

    private List<StandingPurchaseIntent> generateStandingPurchaseIntent(Show show) {
        List<StandingPurchaseIntent> list = new ArrayList<>();
        List<Sector> sectors = show.getSeatingChart().getSectors();
        for (Sector sector: sectors
             ) {
            if(sector instanceof StandingSector && ticketRepository.placesRemaining((StandingSector) sector,show.getShowId()) > 3) {
                    StandingPurchaseIntent standingPurchaseIntent = StandingPurchaseIntent.builder()
                        .id(sector.getId())
                        .amount((int) (Math.random() * 3))
                        .build();
                    list.add(standingPurchaseIntent);
            }
        }

        return list;
    }

    private void saveOrder(Show show, ApplicationUser user) {
        PurchaseIntent order = PurchaseIntent.builder()
            .payment(Payment.builder().method(PAYMENT_METHODS[(int) (Math.random()*PAYMENT_METHODS.length)]).build())
            .showId(show.getShowId())
            .seats(selectSeats(show))
            .standing(generateStandingPurchaseIntent(show))
            .build();
        orderService.placeOrder(order,user);
    }

    @PostConstruct
    private void saveOrders() {
        if(orderRepository.numberOfOrders() > 0) {
            log.debug("orders already generated");
        } else {
            log.info("generating data, that can last some minutes");
            saveUsers();
            List<ApplicationUser> users = userRepository.findAllUsers();
            saveShows();
            List<Long> showIds = showRepository.findAllShowIds();
            int count = 500;
            for (int i = 0; i < count; i++) {
                log.debug("order placed " + i + "/" + count);
                saveOrder(showService.findOne(showIds.get((int) (Math.pow(Math.random(),2)*(showIds.size()/10)))), users.get((int) (Math.pow(Math.random(),2) * users.size())));
            }
        }

    }
}
