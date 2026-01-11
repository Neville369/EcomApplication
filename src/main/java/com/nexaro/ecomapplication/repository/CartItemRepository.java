package com.nexaro.ecomapplication.repository;

import com.nexaro.ecomapplication.model.CartItem;
import com.nexaro.ecomapplication.model.Product;
import com.nexaro.ecomapplication.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {

    CartItem findByUserAndProduct(User user, Product product);

    void deleteByUserAndProduct(User user, Product product);

    List<CartItem> findByUser(User user);

    List<CartItem> user(User user);

    void deleteByUser(User user);

}
