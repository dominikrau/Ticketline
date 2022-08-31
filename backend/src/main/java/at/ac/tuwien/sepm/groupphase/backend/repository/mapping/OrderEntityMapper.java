package at.ac.tuwien.sepm.groupphase.backend.repository.mapping;

import at.ac.tuwien.sepm.groupphase.backend.domain.model.Order;
import at.ac.tuwien.sepm.groupphase.backend.repository.entity.OrderEntity;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

import java.util.List;

@Mapper(uses = {TicketEntityMapper.class, ShowEntityMapper.class, UserEntityMapper.class})
public interface OrderEntityMapper {
    /**
     * Maps the given Order Entity to a Domain representation of the Order
     *
     * @param orderEntity the Order Entity to be mapped
     * @return the mapped Order
     */
    @Named("order")
    Order toDomain(OrderEntity orderEntity);

    /**
     * Maps the given Orders Entities in a List to a List of Domain representations of the Orders
     *
     * @param orderEntities the List of Order Entities to be mapped
     * @return the mapped Order List
     */
    @IterableMapping(qualifiedByName = "order")
    List<Order> toDomain(List<OrderEntity> orderEntities);


    /**
     * Maps the given Order to a Repository Entity representation of the Order
     *
     * @param order the Order to be mapped
     * @return the mapped Order Entity
     */
    @Named("orderEntity")
    OrderEntity toEntity(Order order);

    /**
     * Maps the given Orders in a List to a List of Repository Entity representations of the Orders
     *
     * @param orders the List of Orders to be mapped
     * @return the mapped Order Entity List
     */
    @IterableMapping(qualifiedByName = "orderEntity")
    List<OrderEntity> toEntity(List<Order> orders);
}
