package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.domain.model.ApplicationUser;
import at.ac.tuwien.sepm.groupphase.backend.domain.model.Order;
import at.ac.tuwien.sepm.groupphase.backend.domain.model.PaymentMethod;
import at.ac.tuwien.sepm.groupphase.backend.exception.NotFoundException;
import at.ac.tuwien.sepm.groupphase.backend.repository.entity.OrderEntity;
import at.ac.tuwien.sepm.groupphase.backend.repository.mapping.OrderEntityMapper;
import at.ac.tuwien.sepm.groupphase.backend.repository.mapping.UserEntityMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static java.lang.String.format;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class OrderRepository {
    private final OrderJpaRepository repository;
    private final OrderEntityMapper mapper;
    private final UserEntityMapper userMapper;

    /**
     * Find all order entries in the database ordered by name (ascending).
     *
     * @return ordered list of all order entries
     */
    public List<Order> findAll() {
        return repository.findAllByOrderByCreatedAtDesc()
            .stream().map(mapper::toDomain).collect(Collectors.toList());
    }


    /**
     * Save a single order entry in the database
     *
     * @param order to be saved
     * @return saved order entry
     */
    public Order saveOrder(Order order) {
        OrderEntity saved = repository.save(mapper.toEntity(order));
        return mapper.toDomain(saved);
    }

    /**
     * Update the specified order's payment method
     * @param orderId the Order to be updated
     * @param paymentMethod the new payment method for the order
     */
    public void updatePurchaseMethod(Long orderId, PaymentMethod paymentMethod) {
        repository.updatePaymentMethod(orderId, paymentMethod);
    }

    /**
     * Find a single order entry in the database by id.
     *
     * @param id the id of the order entry
     * @return the order entry
     */
    public Order findById(Long id) {
        return repository.findById(id)
            .map(mapper::toDomain)
            .orElseThrow(() -> new NotFoundException(
                format("No order with given Id \"%s\"  found", id)
            ));
    }

    /**
     * Get a page of orders for the specified user
     *
     * @param user whose orders shall be retrieved
     * @param page containing the page number and the page size
     * @return Page of orders for current user
     */
    public Page<Order> pageFindOrdersOfUser(final ApplicationUser user, final Pageable page) {
        return repository.findAllByUser_IdOrderByCreatedAtDesc(user.getUserId(), page)
            .map(mapper::toDomain);
    }

    /**
     * Checks if the specified order is marked as reserved
     *
     * @param id of the order
     * @return true if order is reserved, otherwise false
     */
    public boolean isOrderReserved(Long id) {
        return repository.existsOrderEntityByPaymentMethodAndOrderId(PaymentMethod.RESERVATION, id);
    }

    /**
     * Get the amount of orders in the database.
     *
     * @return total number of orders
     */
    public long numberOfOrders() {
        return repository.count();
    }


    /**
     * Deletes all orders associated with the specified User
     * @param user whose Orders shall be deleted
     */
    public void deleteOrdersWithUser(ApplicationUser user){
        repository.deleteAllByUser(userMapper.toEntity(user));
    }
}
