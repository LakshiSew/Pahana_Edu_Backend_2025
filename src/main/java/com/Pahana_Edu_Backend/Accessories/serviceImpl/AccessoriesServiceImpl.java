// Updated Accessories Service Implementation
package com.Pahana_Edu_Backend.Accessories.serviceImpl;

import com.Pahana_Edu_Backend.Accessories.entity.Accessories;
import com.Pahana_Edu_Backend.Accessories.repository.AccessoriesRepository;
import com.Pahana_Edu_Backend.Accessories.service.AccessoriesService;
import com.Pahana_Edu_Backend.Category.repository.CategoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class AccessoriesServiceImpl implements AccessoriesService {

    private static final Logger logger = LoggerFactory.getLogger(AccessoriesServiceImpl.class);

    @Autowired
    private AccessoriesRepository accessoriesRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public Accessories addAccessory(Accessories accessory) {
        // Validate required fields
        if (accessory.getItemName() == null || accessory.getItemName().trim().isEmpty()) {
            logger.error("Item name is required");
            throw new IllegalArgumentException("Item name is required");
        }
        if (accessory.getCategoryId() == null || accessory.getCategoryId().trim().isEmpty()) {
            logger.error("Category ID is required");
            throw new IllegalArgumentException("Category ID is required");
        }
        if (!categoryRepository.existsById(accessory.getCategoryId())) {
            logger.error("Category with ID {} not found", accessory.getCategoryId());
            throw new IllegalArgumentException("Category with ID " + accessory.getCategoryId() + " not found");
        }
        if (accessory.getPrice() == null || accessory.getPrice() < 0) {
            logger.error("Price is required and must be non-negative");
            throw new IllegalArgumentException("Price is required and must be non-negative");
        }
        if (accessory.getStockQty() == null || accessory.getStockQty() < 0) {
            logger.error("Stock quantity must be non-negative");
            throw new IllegalArgumentException("Stock quantity must be non-negative");
        }

        // Validate status
        if (accessory.getStatus() != null && !accessory.getStatus().isEmpty()) {
            if (!accessory.getStatus().equals("Active") && !accessory.getStatus().equals("Inactive")) {
                logger.error("Status must be 'Active' or 'Inactive'");
                throw new IllegalArgumentException("Status must be 'Active' or 'Inactive'");
            }
        } else {
            accessory.setStatus("Active");
        }

        // Set timestamps
        if (accessory.getCreatedAt() == null) {
            accessory.setCreatedAt(LocalDateTime.now());
        }
        if (accessory.getUpdatedAt() == null) {
            accessory.setUpdatedAt(LocalDateTime.now());
        }

        Accessories savedAccessory = accessoriesRepository.save(accessory);
        logger.info("Added accessory: {}", savedAccessory.getId());
        return savedAccessory;
    }

    @Override
    public Accessories updateAccessory(String id, Accessories accessory) {
        Accessories existingAccessory = accessoriesRepository.findById(id).orElse(null);
        if (existingAccessory == null) {
            logger.error("Accessory not found with ID: {}", id);
            throw new RuntimeException("Accessory with ID " + id + " not found");
        }

        // Update fields if provided
        if (accessory.getItemName() != null && !accessory.getItemName().trim().isEmpty()) {
            existingAccessory.setItemName(accessory.getItemName());
        }
        if (accessory.getCategoryId() != null && !accessory.getCategoryId().trim().isEmpty()) {
            if (!categoryRepository.existsById(accessory.getCategoryId())) {
                logger.error("Category with ID {} not found", accessory.getCategoryId());
                throw new IllegalArgumentException("Category with ID " + accessory.getCategoryId() + " not found");
            }
            existingAccessory.setCategoryId(accessory.getCategoryId());
        }
        if (accessory.getBrand() != null && !accessory.getBrand().trim().isEmpty()) {
            existingAccessory.setBrand(accessory.getBrand());
        }
        if (accessory.getPrice() != null && accessory.getPrice() >= 0) {
            existingAccessory.setPrice(accessory.getPrice());
        }
        if (accessory.getStockQty() != null && accessory.getStockQty() >= 0) {
            existingAccessory.setStockQty(accessory.getStockQty());
        }
        if (accessory.getImage() != null && !accessory.getImage().trim().isEmpty()) {
            existingAccessory.setImage(accessory.getImage());
        }
        if (accessory.getDiscount() != null && accessory.getDiscount() >= 0) {
            existingAccessory.setDiscount(accessory.getDiscount());
        }
        if (accessory.getDescription() != null && !accessory.getDescription().trim().isEmpty()) {
            existingAccessory.setDescription(accessory.getDescription());
        }
        if (accessory.getStatus() != null && !accessory.getStatus().isEmpty()) {
            if (!accessory.getStatus().equals("Active") && !accessory.getStatus().equals("Inactive")) {
                logger.error("Status must be 'Active' or 'Inactive'");
                throw new IllegalArgumentException("Status must be 'Active' or 'Inactive'");
            }
            existingAccessory.setStatus(accessory.getStatus());
        }

        existingAccessory.setUpdatedAt(LocalDateTime.now());
        Accessories updatedAccessory = accessoriesRepository.save(existingAccessory);
        logger.info("Updated accessory: {}", id);
        return updatedAccessory;
    }

    @Override
    public void deleteAccessory(String id) {
        if (accessoriesRepository.existsById(id)) {
            accessoriesRepository.deleteById(id);
            logger.info("Deleted accessory: {}", id);
        } else {
            logger.error("Accessory not found for deletion with ID: {}", id);
            throw new RuntimeException("Accessory with ID " + id + " not found");
        }
    }

    @Override
    public Accessories getAccessoryById(String id) {
        Accessories accessory = accessoriesRepository.findById(id).orElse(null);
        if (accessory == null) {
            logger.error("Accessory not found with ID: {}", id);
            throw new RuntimeException("Accessory with ID " + id + " not found");
        }
        logger.info("Fetched accessory: {}", id);
        return accessory;
    }

    @Override
    public List<Accessories> getAllAccessories() {
        List<Accessories> accessories = accessoriesRepository.findAll();
        logger.info("Fetched {} accessories", accessories.size());
        return accessories;
    }

    @Override
    public List<Accessories> getAccessoriesByCategoryId(String categoryId) {
        if (categoryId == null || categoryId.trim().isEmpty()) {
            logger.error("Category ID is required");
            throw new IllegalArgumentException("Category ID is required");
        }
        if (!categoryRepository.existsById(categoryId)) {
            logger.error("Category with ID {} not found", categoryId);
            throw new IllegalArgumentException("Category with ID " + categoryId + " not found");
        }
        List<Accessories> accessories = accessoriesRepository.findByCategoryId(categoryId);
        logger.info("Fetched {} accessories for category ID: {}", accessories.size(), categoryId);
        return accessories;
    }
}
