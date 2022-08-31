package at.ac.tuwien.sepm.groupphase.backend.service.impl;

import at.ac.tuwien.sepm.groupphase.backend.domain.ReceiptPdfCreator;
import at.ac.tuwien.sepm.groupphase.backend.domain.model.*;
import at.ac.tuwien.sepm.groupphase.backend.repository.OrderRepository;
import at.ac.tuwien.sepm.groupphase.backend.service.OrderService;
import at.ac.tuwien.sepm.groupphase.backend.service.TicketService;
import at.ac.tuwien.sepm.groupphase.backend.service.UserService;
import at.ac.tuwien.sepm.groupphase.backend.util.Validator;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Slf4j
@Service
@RequiredArgsConstructor
public class SimpleOrderService implements OrderService {
    private final OrderRepository orderRepository;
    private final TicketService ticketService;
    private final Validator validator;
    private final UserService userService;

    @Override
    public List<Order> findAll() {
        log.debug("Find all orders");
        return orderRepository.findAll();
    }

    @Override
    public Order findById(Long id) {
        log.debug("Find orders with id {}", id);
        return orderRepository.findById(id);
    }

    @Override
    @Transactional
    public synchronized Order placeOrder(final PurchaseIntent purchaseIntent) {
        return placeOrder(purchaseIntent, userService.getCurrentUser());
    }

    //used for DataGenerator

    @Override
    @Transactional
    public synchronized Order placeOrder(final PurchaseIntent purchaseIntent, ApplicationUser user) {
        final PurchaseValidation purchaseValidation = validator.validatePurchaseIntent(purchaseIntent);
        Show show = purchaseValidation.getShow();
        final Order order = orderRepository.saveOrder(Order.builder()
            .orderDate(Instant.now())
            .paymentMethod(purchaseIntent.getPayment().getMethod())
            .user(user)
            .show(show)
            .build());
        List<Ticket> tickets = Stream.concat(
            createStandingTicketTemplates(
                show.getPricings(),
                purchaseIntent.getStanding(),
                purchaseValidation.getStandingSectors()
            ).stream(),
            createSeatTicketTemplates(
                show.getPricings(),
                purchaseValidation.getSeats()
            ).stream()
        ).map(ticket -> ticket
            .show(show)
            .status(TicketStatus.fromPaymentMethod(purchaseIntent.getPayment().getMethod()))
            .user(user)
            .ticketOrder(order)
            .build()
        ).collect(Collectors.toList());
        ticketService.saveTicket(tickets);
        return order;
    }

    @Override
    @Transactional
    public void purchaseReservedOrder(Long orderId, Payment payment) {
        List<Long> ticketIds = validator.validatePurchaseReserved(payment, orderId, userService.getCurrentUser())
            .stream()
            .map(Ticket::getTicketId)
            .collect(Collectors.toList());
        ticketService.updateTicketStatus(ticketIds, TicketStatus.BOUGHT);
        orderRepository.updatePurchaseMethod(orderId, payment.getMethod());
    }

    @Override
    @SneakyThrows
    public byte[] createOrderReceipt(Long id) {
        Order order = orderRepository.findById(id);
        List<Ticket> tickets = ticketService.findTicketsForOrderWithId(id);
        return new ReceiptPdfCreator(order, tickets).createPdf();
    }

    private List<Ticket.Builder> createStandingTicketTemplates(
        List<Pricing> pricingList,
        List<StandingPurchaseIntent> standingPurchaseIntents,
        Collection<StandingSector> sectors
    ) {
        Map<Long, Double> pricingMap = pricingList.stream()
            .collect(Collectors.toMap(a -> a.getSector().getId(), Pricing::getPrice));
        Map<Long, Sector> sectorsMap = sectors.stream()
            .collect(Collectors.toMap(Sector::getId, Function.identity()));
        return standingPurchaseIntents.stream()
            .flatMap(intent -> IntStream.range(0, intent.getAmount())
                .mapToObj(b -> Ticket.builder()
                    .sector(sectorsMap.get(intent.getId()))
                    .price(pricingMap.get(intent.getId()))
                )
            ).collect(Collectors.toList());
    }

    private List<Ticket.Builder> createSeatTicketTemplates(List<Pricing> pricingList, Collection<Seat> seats) {
        Map<Long, Double> pricingMap = pricingList.stream()
            .collect(Collectors.toMap(a -> a.getSector().getId(), Pricing::getPrice));
        return seats.stream()
            .map(a -> Ticket.builder()
                .seat(a)
                .sector(a.getSector())
                .price(pricingMap.get(a.getSector().getId()))
            ).collect(Collectors.toList());
    }

    @Override
    public Page<Order> pageOrdersOfUser(Pageable pageable) {
        ApplicationUser user = userService.getCurrentUser();
        return orderRepository.pageFindOrdersOfUser(user, pageable);
    }


}
