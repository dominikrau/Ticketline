package at.ac.tuwien.sepm.groupphase.backend.repository;

import at.ac.tuwien.sepm.groupphase.backend.domain.model.PaymentMethod;
import at.ac.tuwien.sepm.groupphase.backend.repository.entity.ApplicationUserEntity;
import at.ac.tuwien.sepm.groupphase.backend.repository.entity.OrderEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderJpaRepository extends JpaRepository<OrderEntity, Long> {

    /**
     * Find all order entries ordered by created at date (descending).
     *
     * @return ordered list of all order entries
     */
    List<OrderEntity> findAllByOrderByCreatedAtDesc();

    /**
     * Checks if the specified order's payment method is the same as the provided payment method
     *
     * @param paymentMethod to be compared to
     * @param orderId of the order to be compared
     * @return true if payment methods are the same, otherwise false
     */
    boolean existsOrderEntityByPaymentMethodAndOrderId(PaymentMethod paymentMethod, Long orderId);


    /**
     *
     * @param id of the user
     * @param page information as Pageable(pageNumber, pageSize)
     * @return Page of orders for the given user id
     */
    Page<OrderEntity> findAllByUser_IdOrderByCreatedAtDesc(Long id, Pageable page);

    /**
     * Deletes all orders associated with the specified User
     * @param user whose Orders shall be deleted
     */
    void deleteAllByUser(ApplicationUserEntity user);

    /**
     * Update the specified order's payment method
     * @param id of the Order to be updated
     * @param paymentMethod the new payment method for the order
     */
    @Modifying
    @Query("UPDATE OrderEntity set paymentMethod = :payment_method where orderId = :id")
    void updatePaymentMethod(@Param("id") Long id, @Param("payment_method") PaymentMethod paymentMethod);

}
