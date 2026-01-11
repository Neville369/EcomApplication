package com.nexaro.ecomapplication.controller;

import com.nexaro.ecomapplication.dto.OrderResponse;
import com.nexaro.ecomapplication.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(
            @RequestHeader("X-User-ID") String userid){
    return orderService.createOrder(userid)
            .map(orderResponse ->
                    new ResponseEntity<>(orderResponse, HttpStatus.CREATED))
            .orElseGet(()-> ResponseEntity.badRequest().build());
    }
}
