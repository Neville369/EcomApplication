package com.nexaro.ecomapplication.repository;

import com.nexaro.ecomapplication.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {

}
