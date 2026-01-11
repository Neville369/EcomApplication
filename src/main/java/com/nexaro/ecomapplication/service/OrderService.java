package com.nexaro.ecomapplication.service;

import com.nexaro.ecomapplication.dto.OrderItemDTO;
import com.nexaro.ecomapplication.dto.OrderResponse;
import com.nexaro.ecomapplication.model.*;
import com.nexaro.ecomapplication.repository.OrderRepository;
import com.nexaro.ecomapplication.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {
    private final CartService cartService;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;

    public Optional<OrderResponse> createOrder(String userid) {

        //Validate for cart Items
        List<CartItem> cartItems = cartService.getCart(userid);
        if(cartItems.isEmpty()){
            return Optional.empty();
        }

        //Validate for user
        Optional<User> userOptional = userRepository.findById(Long.valueOf(userid));
        if(cartItems.isEmpty()){
            return Optional.empty();
        }
        User user = userOptional.get();

        //calculate total price
        BigDecimal totalPrice = cartItems.stream()
                .map(CartItem::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        //create order
        Order order = new Order();
        order.setUser(user);
        order.setStatus(OrderStatus.CONFIRMED);
        order.setTotalAmount(totalPrice);
        List<OrderItem> orderItems = cartItems.stream()
                .map(item -> new OrderItem(
                        null,
                        item.getProduct(),
                        item.getQuantity(),
                        item.getPrice(),
                        order
                ))
                .toList();
        order.setItems(orderItems);
        Order savedOrder = orderRepository.save(order);

        //clear the cart after placing order
        cartService.clearCart(userid);
        return Optional.of(mapToOrderResponse(savedOrder));
    }

    private OrderResponse mapToOrderResponse(Order order) {
    return new OrderResponse(
            order.getId(),
            order.getTotalAmount(),
            order.getStatus(),
            order.getItems()
                    .stream()
                    .map(orderItem -> new OrderItemDTO(
                            orderItem.getId(),
                            orderItem.getProduct().getId(),
                            orderItem.getQuantity(),
                            orderItem.getPrice(),
                            orderItem.getPrice().multiply(new BigDecimal(orderItem.getQuantity()))
                    ))
                    .toList(),
            order.getCreatedAt()
    );
    }
}
