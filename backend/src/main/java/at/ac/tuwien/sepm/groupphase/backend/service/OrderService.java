package at.ac.tuwien.sepm.groupphase.backend.service;

import at.ac.tuwien.sepm.groupphase.backend.domain.model.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.domain.model.Order;
import at.ac.tuwien.sepm.groupphase.backend.domain.model.Payment;
import at.ac.tuwien.sepm.groupphase.backend.domain.model.PurchaseIntent;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface OrderService {

    /**
     * Find all order entries ordered by name (ascending).
     *
     * @return ordered list of all order entries
     */
    List<Order> findAll();

    /**
     * Find a single order entry by id.
     *
     * @param id the id of the order entry
     * @return the order entry
     */
    Order findById(Long id);


    /**
     * Get a page of orders for a current user
     *
     * @param pageable containing the page number and the page size
     * @return Page of orders for current user
     */
    Page<Order> pageOrdersOfUser(Pageable pageable);

    /**
     * Creates an Order for the given Purchase Intent and creates
     * Tickets out of the given Seats and Standing Areas
     *
     * @param purchaseIntent The Intent to Purchase Tickets for the given Seats
     * @return The Order which was created
     */
    Order placeOrder(PurchaseIntent purchaseIntent);

    /**
     * Creates an Order for the given Purchase Intent, associates it with
     * the provided User and creates Tickets out of the given Seats and Standing Areas
     *
     * @param purchaseIntent The Intent to Purchase Tickets for the given Seats
     * @param user to whom the Ticket belongs
     * @return The Order which was created
     */
    Order placeOrder(PurchaseIntent purchaseIntent, ApplicationUser user);

    /**
     * Finishes the buying process of reserved Tickets.
     *
     * @param id of the Order to be finished
     * @param payment containing the payment information to be updated
     */
    void purchaseReservedOrder(Long id, Payment payment);

    /**
     * Delegates the creation of the PDFs for the Receipt of the specified Order
     * to the repository layer
     *
     * @param id of the Order, whose Receipt PDFs shall be created
     * @return the created Receipt PDFs
     */
    byte[] createOrderReceipt(Long id);
}
