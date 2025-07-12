package com.Pahana_Edu_Backend.Cart.service;

import com.Pahana_Edu_Backend.Cart.entity.Cart;
import java.util.List;

public interface CartService {
    Cart addToCart(String userId, String productId, String productType, Integer quantity);
    List<Cart> getCartByUserId(String userId);
    Cart updateCartItem(String userId, String productId, String productType, Integer quantity);
    void removeFromCart(String userId, String id);
    void clearCart(String userId);
}