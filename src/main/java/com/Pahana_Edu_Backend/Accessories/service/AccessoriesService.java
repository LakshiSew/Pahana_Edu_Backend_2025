package com.Pahana_Edu_Backend.Accessories.service;

import java.util.List;
import com.Pahana_Edu_Backend.Accessories.entity.Accessories;

public interface AccessoriesService {

    Accessories addAccessory(Accessories accessory);
    Accessories updateAccessory(String id, Accessories accessory);
    void deleteAccessory(String id);
    Accessories getAccessoryById(String id);
    List<Accessories> getAllAccessories();
    List<Accessories> getAccessoriesByCategoryId(String categoryId); 
}