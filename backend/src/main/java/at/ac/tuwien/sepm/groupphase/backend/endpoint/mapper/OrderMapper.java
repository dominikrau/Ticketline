package at.ac.tuwien.sepm.groupphase.backend.endpoint.mapper;

import at.ac.tuwien.sepm.groupphase.backend.domain.model.Order;
import at.ac.tuwien.sepm.groupphase.backend.endpoint.dto.OrderDto;
import org.mapstruct.IterableMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Named;

import java.util.List;

@Mapper(uses = {TicketMapper.class, ShowMapper.class})
public interface OrderMapper {

    /**
     * Maps the given Order DTO to a Domain representation of the Order
     *
     * @param orderDto the Order DTO to be mapped
     * @return the mapped Order
     */
    @Named("order")
    Order orderDtoToOrder(OrderDto orderDto);

    /**
     * Maps the given Orders DTOs in a List to a List of Domain representations of the Orders
     *
     * @param orderDtos the List of Order DTOs to be mapped
     * @return the mapped Order List
     */
    @IterableMapping(qualifiedByName = "order")
    List<Order> orderDtoToOrder(List<OrderDto> orderDtos);


    /**
     * Maps the given Order to a Data Transfer Object representation of the Order
     *
     * @param order the Order to be mapped
     * @return the mapped Order DTO
     */
    @Named("order")
    OrderDto orderToOrderDto(Order order);

    /**
     * Maps the given Orders in a List to a List of Data Transfer Object representations of the Orders
     *
     * @param orders the List of Orders to be mapped
     * @return the mapped Order DTO List
     */
    @IterableMapping(qualifiedByName = "order")
    List<OrderDto> orderToOrderDto(List<Order> orders);


}
