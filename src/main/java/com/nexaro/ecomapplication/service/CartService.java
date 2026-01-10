package com.nexaro.ecomapplication.service;

import com.nexaro.ecomapplication.dto.CartItemRequest;
import com.nexaro.ecomapplication.model.CartItem;
import com.nexaro.ecomapplication.model.Product;
import com.nexaro.ecomapplication.model.User;
import com.nexaro.ecomapplication.repository.CartItemRepository;
import com.nexaro.ecomapplication.repository.ProductRepository;
import com.nexaro.ecomapplication.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartService {
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final CartItemRepository cartItemRepository;

    public boolean addToCart(String userId, CartItemRequest request) {
        //Look for product
       Optional<Product> productOpt =  productRepository.findById(request.getProductId());
       if (productOpt.isEmpty())
           return false;

       Product product = productOpt.get();
       if (product.getStockQuantity() < request.getQuantity())
           return false;

       Optional<User> userOpt = userRepository.findById(Long.valueOf(userId));
       if (userOpt.isEmpty())
           return false;

       User user = userOpt.get();

       CartItem existingCartItem   = cartItemRepository.findByUserAndProduct(user, product);
       if (existingCartItem != null){
           //update the quantity
           existingCartItem.setQuantity(existingCartItem.getQuantity() + request.getQuantity());
           existingCartItem.setPrice(product.getPrice().multiply(BigDecimal.valueOf(existingCartItem.getQuantity())));
           cartItemRepository.save(existingCartItem);
       }else {
           //Create nre cart item
           CartItem cartItem = new CartItem();
           cartItem.setUser(user);
           cartItem.setProduct(product);
           cartItem.setQuantity(request.getQuantity());
           cartItem.setPrice(product.getPrice().multiply(BigDecimal.valueOf(request.getQuantity())));
           cartItemRepository.save(cartItem);

       }
       return  true;
    }
}
