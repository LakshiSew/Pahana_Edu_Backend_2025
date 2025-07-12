package com.Pahana_Edu_Backend.Cart.serviceImpl;

import com.Pahana_Edu_Backend.Accessories.entity.Accessories;
import com.Pahana_Edu_Backend.Accessories.service.AccessoriesService;
import com.Pahana_Edu_Backend.Book.entity.Book;
import com.Pahana_Edu_Backend.Book.service.BookService;
import com.Pahana_Edu_Backend.Cart.entity.Cart;
import com.Pahana_Edu_Backend.Cart.repository.CartRepository;
import com.Pahana_Edu_Backend.Cart.service.CartService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class CartServiceImpl implements CartService {

    private static final Logger logger = LoggerFactory.getLogger(CartServiceImpl.class);

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private BookService bookService;

    @Autowired
    private AccessoriesService accessoriesService;

    @Override
    public Cart addToCart(String userId, String productId, String productType, Integer quantity) {
        if (userId == null || userId.trim().isEmpty()) {
            logger.error("User ID is required");
            throw new IllegalArgumentException("User ID is required");
        }
        if (productId == null || productId.trim().isEmpty()) {
            logger.error("Product ID is required");
            throw new IllegalArgumentException("Product ID is required");
        }
        if (!productType.equals("Book") && !productType.equals("Accessory")) {
            logger.error("Invalid product type: {}", productType);
            throw new IllegalArgumentException("Product type must be 'Book' or 'Accessory'");
        }
        if (quantity == null || quantity <= 0) {
            logger.error("Quantity must be positive");
            throw new IllegalArgumentException("Quantity must be positive");
        }

        Cart existingItem = cartRepository.findByUserId(userId).stream()
            .filter(item -> item.getProductId().equals(productId) && item.getProductType().equals(productType))
            .findFirst()
            .orElse(null);

        if (existingItem != null) {
            existingItem.setQuantity(existingItem.getQuantity() + quantity);
            existingItem.setUpdatedAt(LocalDateTime.now());
            cartRepository.save(existingItem);
            logger.info("Updated cart item for user {}: {} ({})", userId, productId, productType);
            return existingItem;
        }

        Cart cartItem = new Cart();
        cartItem.setId(UUID.randomUUID().toString());
        cartItem.setUserId(userId);
        cartItem.setProductId(productId);
        cartItem.setProductType(productType);
        cartItem.setQuantity(quantity);

        if (productType.equals("Book")) {
            Book book = bookService.getBookById(productId);
            if (book == null || !book.getStatus().equals("Active")) {
                logger.error("Book not found or inactive: {}", productId);
                throw new RuntimeException("Book not found or inactive");
            }
            if (book.getStockQty() < quantity) {
                logger.error("Insufficient stock for book: {}", productId);
                throw new IllegalArgumentException("Insufficient stock for book");
            }
            cartItem.setName(book.getTitle());
            cartItem.setPrice(book.getPrice() * (1 - (book.getDiscount() != null ? book.getDiscount() / 100 : 0)));
            cartItem.setImage(book.getImage());
        } else {
            Accessories accessory = accessoriesService.getAccessoryById(productId);
            if (accessory == null || !accessory.getStatus().equals("Active")) {
                logger.error("Accessory not found or inactive: {}", productId);
                throw new RuntimeException("Accessory not found or inactive");
            }
            if (accessory.getStockQty() < quantity) {
                logger.error("Insufficient stock for accessory: {}", productId);
                throw new IllegalArgumentException("Insufficient stock for accessory");
            }
            cartItem.setName(accessory.getItemName());
            cartItem.setPrice(accessory.getPrice() * (1 - (accessory.getDiscount() != null ? accessory.getDiscount() / 100 : 0)));
            cartItem.setImage(accessory.getImage());
        }

        cartItem.setCreatedAt(LocalDateTime.now());
        cartItem.setUpdatedAt(LocalDateTime.now());
        Cart savedCartItem = cartRepository.save(cartItem);
        logger.info("Added to cart for user {}: {} ({})", userId, productId, productType);
        return savedCartItem;
    }

    @Override
    public List<Cart> getCartByUserId(String userId) {
        List<Cart> cartItems = cartRepository.findByUserId(userId);
        logger.info("Fetched {} cart items for user {}", cartItems.size(), userId);
        return cartItems;
    }

    @Override
    public Cart updateCartItem(String userId, String productId, String productType, Integer quantity) {
        Cart existingItem = cartRepository.findByUserId(userId).stream()
            .filter(item -> item.getProductId().equals(productId) && item.getProductType().equals(productType))
            .findFirst()
            .orElseThrow(() -> {
                logger.error("Cart item not found for user {}: {} ({})", userId, productId, productType);
                return new RuntimeException("Cart item not found");
            });

        if (quantity <= 0) {
            logger.error("Quantity must be positive");
            throw new IllegalArgumentException("Quantity must be positive");
        }

        if (productType.equals("Book")) {
            Book book = bookService.getBookById(productId);
            if (book == null || !book.getStatus().equals("Active")) {
                logger.error("Book not found or inactive: {}", productId);
                throw new RuntimeException("Book not found or inactive");
            }
            if (book.getStockQty() < quantity) {
                logger.error("Insufficient stock for book: {}", productId);
                throw new IllegalArgumentException("Insufficient stock for book");
            }
            existingItem.setPrice(book.getPrice() * (1 - (book.getDiscount() != null ? book.getDiscount() / 100 : 0)));
            existingItem.setImage(book.getImage());
        } else {
            Accessories accessory = accessoriesService.getAccessoryById(productId);
            if (accessory == null || !accessory.getStatus().equals("Active")) {
                logger.error("Accessory not found or inactive: {}", productId);
                throw new RuntimeException("Accessory not found or inactive");
            }
            if (accessory.getStockQty() < quantity) {
                logger.error("Insufficient stock for accessory: {}", productId);
                throw new IllegalArgumentException("Insufficient stock for accessory");
            }
            existingItem.setPrice(accessory.getPrice() * (1 - (accessory.getDiscount() != null ? accessory.getDiscount() / 100 : 0)));
            existingItem.setImage(accessory.getImage());
        }

        existingItem.setQuantity(quantity);
        existingItem.setUpdatedAt(LocalDateTime.now());
        Cart updatedCartItem = cartRepository.save(existingItem);
        logger.info("Updated cart item for user {}: {} ({})", userId, productId, productType);
        return updatedCartItem;
    }

    @Override
    public void removeFromCart(String userId, String id) {
        if (cartRepository.existsById(id)) {
            cartRepository.deleteByUserIdAndId(userId, id);
            logger.info("Removed cart item {} for user {}", id, userId);
        } else {
            logger.error("Cart item not found: {}", id);
            throw new RuntimeException("Cart item not found");
        }
    }

    @Override
    public void clearCart(String userId) {
        if (userId == null || userId.trim().isEmpty()) {
            logger.error("User ID is required");
            throw new IllegalArgumentException("User ID is required");
        }
        List<Cart> cartItems = cartRepository.findByUserId(userId);
        if (cartItems.isEmpty()) {
            logger.info("Cart is already empty for user {}", userId);
            return;
        }
        cartRepository.deleteByUserId(userId);
        logger.info("Cleared {} cart items for user {}", cartItems.size(), userId);
    }
}