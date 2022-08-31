package at.ac.tuwien.sepm.groupphase.backend.util;

import at.ac.tuwien.sepm.groupphase.backend.domain.model.*;
import at.ac.tuwien.sepm.groupphase.backend.exception.ValidationException;
import at.ac.tuwien.sepm.groupphase.backend.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.lang.String.format;

@Slf4j
@Component
@RequiredArgsConstructor
public class Validator {

    private final SeatingChartRepository seatingChartRepository;
    private final SeatRepository seatRepository;
    private final TicketRepository ticketRepository;
    private final SectorRepository sectorRepository;
    private final ShowRepository showRepository;
    private final OrderRepository orderRepository;
    private final Clock clock;

    /**
     * validates the purchasing of Reserved Orders
     *
     * @param payment contains the payment information for the purchase
     * @param orderId id of the order, that the tickets belong to
     * @param currentUser is the user that is buying the order
     * @throws ValidationException if not valid
     * @return list of validated tickets
     */
    public List<Ticket> validatePurchaseReserved(Payment payment, Long orderId, ApplicationUser currentUser) {
        if (payment.getMethod() == PaymentMethod.RESERVATION) {
            throw new ValidationException("Trying to Purchase an Order with Payment Method Reserve");
        }
        if (!orderRepository.isOrderReserved(orderId)) {
            throw new ValidationException(format("Trying to buy Reserved Tickets but Order %s is not in Status reserved.", orderId));
        }
        final List<Ticket> tickets = ticketRepository.findTicketsForOrderWithId(orderId);
        if (!tickets.stream().allMatch(ticket -> ticket.getUser().equals(currentUser))) {
            throw new ValidationException("Trying to Buy Tickets that don't belong to You");
        } else if (tickets.stream().anyMatch(ticket -> ticket.getShow().getStartTime().isBefore(LocalDateTime.now(clock)))) {
            throw new ValidationException("Trying to purchase Ticket for Show in the Past");
        }
        return tickets;
    }

    /**
     * validates the intent to purchase an order
     *
     * @param purchaseIntent contains all the information for the order
     * @throws ValidationException if not valid
     * @return a validated PurchaseValidation
     */
    public PurchaseValidation validatePurchaseIntent(final PurchaseIntent purchaseIntent) {
        final Show show = showRepository.findById(purchaseIntent.getShowId());
        if (show.getStartTime().isBefore(LocalDateTime.now(clock))) {
            throw new ValidationException("Trying to purchase Ticket for Show in the Past");
        }
        if (purchaseIntent.getPayment().getMethod() == PaymentMethod.RESERVATION) {
            int count = 0;
            for (StandingPurchaseIntent standingPurchaseIntent: purchaseIntent.getStanding()
                 ) {
                count += standingPurchaseIntent.getAmount();
            }
            count += purchaseIntent.getSeats().size();
            if(count > 20) {
                throw new ValidationException("A maximum of 20 tickets per order can be bought");
            }
        }
        final Collection<StandingSector> sectors = validateStandingPurchaseIntent(purchaseIntent.getStanding(), show);
        final Collection<Seat> seats = validateSeats(purchaseIntent.getSeats(), show);

        return PurchaseValidation.builder()
            .show(show)
            .standingSectors(sectors)
            .seats(seats)
            .build();
    }

    /**
     * validates the cancellation of a Ticket
     *
     * @param ticket that shall be cancelled
     * @param currentUser is the user that is cancelling the ticket
     * @throws ValidationException if not valid
     */
    public void validateCancelTicket(final Ticket ticket, final ApplicationUser currentUser) {
        if (!ticket.getUser().equals(currentUser)) {
            throw new ValidationException("Trying to Cancel Ticket which does not belong to you");
        } else if (ticket.getShow().getStartTime().isBefore(LocalDateTime.now(clock))) {
            throw new ValidationException("Trying to cancel Ticket with Show in the Past");
        }
    }

    /**
     * validates the StandingPurchaseIntents (amount and standing sector in an order)
     *
     * @param purchaseIntents list of StandingPurchaseIntent contains information for the standing places in an order
     * @param show the show that the tickets shall be bought for
     * @throws ValidationException if not valid
     * @return list of validated Standing Sectors that the tickets will be bought for
     */
    private Collection<StandingSector> validateStandingPurchaseIntent(final List<StandingPurchaseIntent> purchaseIntents, final Show show) {
        Map<Long, StandingSector> sectors = sectorRepository.findByIds(purchaseIntents.stream()
            .map(StandingPurchaseIntent::getId).collect(Collectors.toList()))
            .stream()
            .map(sector -> {
                if (!show.getSeatingChart().getSectors().contains(sector)) {
                    throw new ValidationException(format(
                        "Sector %s does not exist for Show %s",
                        sector.getId(),
                        show.getShowId()
                    ));
                } else if (!(sector instanceof StandingSector)) {
                    throw new ValidationException("Trying to purchase Standing Ticket for non Standing Sector");
                }
                return (StandingSector) sector;
            }).collect(Collectors.toMap(Sector::getId, Function.identity()));

        purchaseIntents.forEach(intent -> {
            if (intent.getAmount() > ticketRepository.placesRemaining(sectors.get(intent.getId()), show.getShowId())) {
                throw new ValidationException("Not enough Standing Places Remaining");
            }
        });
        return sectors.values();
    }

    /**
     * validates the Seats in an order
     *
     * @param ids list of ids of seats, that are part of the order
     * @param show the show that the tickets shall be bought for
     * @throws ValidationException if not valid
     * @return list of validated seats that the tickets will be bought for
     */
    private Collection<Seat> validateSeats(List<Long> ids, Show show) {
        List<Seat> seats = seatRepository.findByIds(ids);
        List<Long> sectorIdsInShow = show.getSeatingChart().getSectors().stream()
            .map(Sector::getId)
            .collect(Collectors.toList());
        seats.forEach(seat -> {
            if (!sectorIdsInShow.contains(seat.getSector().getId())) {
                throw new ValidationException(format(
                    "Seats %s does not exist for Show %s",
                    seat.getId(),
                    show.getShowId()
                ));
            } else if (!ticketRepository.isSeatAvailable(seat.getId(), show.getShowId())) {
                throw new ValidationException(format("Seat %s is already taken", seat.getId()));
            }
        });
        return seats;
    }

    /**
     * validates a new show
     *
     * @param show that will be validated
     * @throws ValidationException if not valid
     */
    public void validateNewShow(Show show) {
        validateShowsPricing(show);
        validateShowHall(show);
        if (show.getStartTime().isBefore(LocalDateTime.now())) {
            throw new ValidationException("Show must start in future");
        }
        if (show.getStartTime().isAfter(show.getEndTime())) {
            throw new ValidationException("Show start time must be before end time");
        }
    }

    /**
     * validates pricing for a show (pricing must exist/only exist for all sectors in a show)
     *
     * @param show that the pricing is validated for
     * @throws ValidationException if not valid
     */
    private void validateShowsPricing(Show show) {
        SeatingChart seatingChart = seatingChartRepository.findById(show.getSeatingChart().getId());
        List<Sector> sectors = seatingChart.getSectors();
        boolean exists;
        for (Pricing pricing : show.getPricings()
        ) {
            exists = false;
            for (Sector sector : sectors
            ) {
                Sector pricedSector = pricing.getSector();
                if (pricedSector.getId().equals(sector.getId())) {
                    exists = true;
                    sectors.remove(sector);
                    break;
                }
            }
            if (!exists) {
                throw new ValidationException("Price can't be set for non existing sector");
            }
        }
        if (!sectors.isEmpty()) {
            throw new ValidationException("Price is not set for all sectors");
        }

    }

    /**
     * validates the hall of a show (if it's free for the selected time)
     *
     * @param show that will be validated
     * @throws ValidationException if not valid
     */
    private void validateShowHall(Show show) {
        if (!showRepository.findOverlappingShows(show.getSeatingChart().getHall().getId(), show.getStartTime(), show.getEndTime()).isEmpty()) {
            throw new ValidationException("Hall is already in use during selected time");
        }
    }

    /**
     * validates the age of a new user (must be older than 7 years)
     *
     * @param user that will be validated
     * @throws ValidationException if not valid
     */
    public void validateNewUser(ApplicationUser user) {

        if (user.getDateOfBirth().isAfter(LocalDate.now().minusYears(7))) {
            throw new ValidationException("You are too young!");
        }
    }

    private void validateSector(SeatingChart seatingChart) {
        List<Sector> sectors = seatingChart.getSectors();
        for (Sector sector : sectors
        ) {
            if (sector instanceof StandingSector) {
                validateStandingSector((StandingSector) sector);
            }
            if (sector instanceof SittingSector) {
                validateSittingSector((SittingSector) sector);

            }
        }
    }

    private void validateSittingSector(SittingSector sector) {
        if (sector.getSeats() == null) {
            throw new ValidationException("Seats must exist for sitting sector");
        }
    }

    private void validateStandingSector(StandingSector sector) {
        if (sector.getX() == null) {
            throw new ValidationException("X of standing sector must be set");
        }
        if (sector.getY() == null) {
            throw new ValidationException("Y of standing sector must be set");
        }
        if (sector.getWidth() == null) {
            throw new ValidationException("Width of standing sector must be set");
        }
        if (sector.getHeight() == null) {
            throw new ValidationException("Height of standing sector must be set");
        }
        if (sector.getCapacity() == null) {
            throw new ValidationException("Capacity of standing sector must be set");
        }
    }


    /**
     * validates if a seating chart is valid (no overlapping places, no places out of bound)
     *
     * @param seatingChart that will be validated
     * @throws ValidationException if not valid
     */
    public void validateSeatingChart(SeatingChart seatingChart) {


        validateSector(seatingChart);
        Hall hall = seatingChart.getHall();
        int[][] seatingArea = new int[hall.getWidth()][hall.getHeight()];
        Stage stage = seatingChart.getStage();
        if (stage.getX() + stage.getWidth() > hall.getWidth() || stage.getY() + stage.getHeight() > hall.getHeight()) {
            throw new ValidationException("Stage is out of area");
        }
        for (int i = stage.getX(); i < stage.getX() + stage.getWidth(); i++) {
            for (int j = stage.getY(); j < stage.getY() + stage.getHeight(); j++) {
                seatingArea[i][j] = 1;
            }
        }
        List<Sector> sectors = seatingChart.getSectors();
        for (Sector sector : sectors
        ) {
            if (sector instanceof StandingSector) {
                if (((StandingSector) sector).getX() + ((StandingSector) sector).getWidth() > hall.getWidth() ||
                    ((StandingSector) sector).getY() + ((StandingSector) sector).getHeight() > hall.getHeight()) {
                    throw new ValidationException("Sector is out of area");
                }
                for (int i = ((StandingSector) sector).getX(); i < ((StandingSector) sector).getX() + ((StandingSector) sector).getWidth(); i++) {
                    for (int j = ((StandingSector) sector).getY(); j < ((StandingSector) sector).getY() + ((StandingSector) sector).getHeight(); j++) {
                        if (seatingArea[i][j] == 1) {
                            throw new ValidationException("Sector is overlapping");
                        }
                        seatingArea[i][j] = 1;
                    }

                }
            } else if (sector instanceof SittingSector) {
                List<Seat> seats = ((SittingSector) sector).getSeats();
                for (Seat seat : seats
                ) {
                    if (seat.getX() > hall.getWidth() || seat.getY() > hall.getHeight()) {
                        throw new ValidationException("Seat is out of area");
                    }
                    if (seatingArea[seat.getX()][seat.getY()] == 1) {
                        throw new ValidationException("Seat is overlapping");
                    }
                    seatingArea[seat.getX()][seat.getY()] = 1;
                }
            }
        }
    }

    /**
     * validates the the pdf-print of tickets (tickets shall only be printed if they are bought)
     *
     * @param orderId id of the order containing the tickets that will be validated
     * @throws ValidationException if not valid
     */
    public List<Ticket> validateTicketPdf(Long orderId) {
        List<Ticket> tickets = ticketRepository.findTicketsForOrderWithId(orderId);
        if (tickets.stream().noneMatch(ticket -> ticket.getStatus() == TicketStatus.BOUGHT)) {
            throw new ValidationException("Trying to Print Tickets which have not been Bought or are Cancelled");
        }
        return tickets;
    }
}
