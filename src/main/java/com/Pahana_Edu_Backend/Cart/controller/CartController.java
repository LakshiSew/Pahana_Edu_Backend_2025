package com.Pahana_Edu_Backend.Cart.controller;

import com.Pahana_Edu_Backend.Cart.entity.Cart;
import com.Pahana_Edu_Backend.Cart.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@CrossOrigin
public class CartController {

    @Autowired
    private CartService cartService;

    @PostMapping("/add")
    public ResponseEntity<?> addToCart(@RequestBody CartRequest cartRequest) {
        try {
            if (cartRequest.getUserId() == null || cartRequest.getUserId().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("User ID is required");
            }
            Cart cartItem = cartService.addToCart(
                cartRequest.getUserId(),
                cartRequest.getProductId(),
                cartRequest.getProductType(),
                cartRequest.getQuantity()
            );
            return ResponseEntity.ok(cartItem);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/{userId}")
    public ResponseEntity<?> getCart(@PathVariable("userId") String userId) {
        try {
            if (userId == null || userId.trim().isEmpty()) {
                return ResponseEntity.badRequest().body("User ID is required");
            }
            List<Cart> cartItems = cartService.getCartByUserId(userId);
            return ResponseEntity.ok(new CartResponse(cartItems));
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    @GetMapping("/count/{userId}")
    public ResponseEntity<?> getCartItemCount(@PathVariable("userId") String userId) {
        try {
            if (userId == null || userId.trim().isEmpty()) {
                return ResponseEntity.badRequest().body("User ID is required");
            }
            List<Cart> cartItems = cartService.getCartByUserId(userId);
            int totalCount = cartItems.stream().mapToInt(Cart::getQuantity).sum();
            return ResponseEntity.ok(new CartCountResponse(totalCount));
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    @GetMapping("/item/{userId}/{productId}/{productType}")
    public ResponseEntity<?> getCartItemByProduct(
            @PathVariable("userId") String userId,
            @PathVariable("productId") String productId,
            @PathVariable("productType") String productType) {
        try {
            if (userId == null || userId.trim().isEmpty()) {
                return ResponseEntity.badRequest().body("User ID is required");
            }
            List<Cart> cartItems = cartService.getCartByUserId(userId);
            Cart cartItem = cartItems.stream()
                .filter(item -> item.getProductId().equals(productId) && item.getProductType().equals(productType))
                .findFirst()
                .orElse(null);
            if (cartItem == null) {
                return ResponseEntity.status(404).body("Item not found in cart.");
            }
            return ResponseEntity.ok(cartItem);
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateCartItem(@RequestBody CartRequest cartRequest) {
        try {
            if (cartRequest.getUserId() == null || cartRequest.getUserId().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("User ID is required");
            }
            Cart updatedItem = cartService.updateCartItem(
                cartRequest.getUserId(),
                cartRequest.getProductId(),
                cartRequest.getProductType(),
                cartRequest.getQuantity()
            );
            return ResponseEntity.ok(updatedItem);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/remove/{userId}/{id}")
    public ResponseEntity<?> removeFromCart(
            @PathVariable("userId") String userId,
            @PathVariable("id") String id) {
        try {
            if (userId == null || userId.trim().isEmpty()) {
                return ResponseEntity.badRequest().body("User ID is required");
            }
            cartService.removeFromCart(userId, id);
            return ResponseEntity.ok("Item removed from cart.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }

    @DeleteMapping("/clear/{userId}")
    public ResponseEntity<?> clearCart(@PathVariable("userId") String userId) {
        try {
            if (userId == null || userId.trim().isEmpty()) {
                return ResponseEntity.badRequest().body("User ID is required");
            }
            cartService.clearCart(userId);
            return ResponseEntity.ok("Cart cleared successfully.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }
}

class CartRequest {
    private String userId;
    private String productId;
    private String productType;
    private Integer quantity;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}

class CartResponse {
    private List<Cart> items;

    public CartResponse(List<Cart> items) {
        this.items = items;
    }

    public List<Cart> getItems() {
        return items;
    }

    public void setItems(List<Cart> items) {
        this.items = items;
    }
}

class CartCountResponse {
    private int count;

    public CartCountResponse(int count) {
        this.count = count;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}