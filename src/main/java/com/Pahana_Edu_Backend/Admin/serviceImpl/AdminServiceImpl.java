package com.Pahana_Edu_Backend.Admin.serviceImpl;

import com.Pahana_Edu_Backend.Admin.entity.Admin;
import com.Pahana_Edu_Backend.Admin.repository.AdminRepository;
import com.Pahana_Edu_Backend.Admin.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private AdminRepository adminRepository;

    @Override
    public List<Admin> getAllAdmins() {
        return adminRepository.findAll();
    }

    @Override
    public Optional<Admin> getAdminById(String id) {
        return adminRepository.findById(id);
    }

    @Override
    public Admin createAdmin(Admin admin) {
        return adminRepository.save(admin);
    }

    @Override
    public Admin updateAdmin(String id, Admin updatedAdmin) {
        Optional<Admin> optionalAdmin = adminRepository.findById(id);

        if (optionalAdmin.isPresent()) {
            Admin existingAdmin = optionalAdmin.get();

            if (updatedAdmin.getUserName() != null && !updatedAdmin.getUserName().isEmpty()) {
                existingAdmin.setUserName(updatedAdmin.getUserName());
            }
            if (updatedAdmin.getEmail() != null && !updatedAdmin.getEmail().isEmpty()) {
                existingAdmin.setEmail(updatedAdmin.getEmail());
            }
            if (updatedAdmin.getPassword() != null) {
                existingAdmin.setPassword(updatedAdmin.getPassword());
            }
            if (updatedAdmin.getAdminImage() != null) {
                existingAdmin.setAdminImage(updatedAdmin.getAdminImage());
            }
            if (updatedAdmin.getPosition() != null && !updatedAdmin.getPosition().isEmpty()) {
                existingAdmin.setPosition(updatedAdmin.getPosition());
            }

            return adminRepository.save(existingAdmin);
        } else {
            throw new RuntimeException("Admin with ID " + id + " not found.");
        }
    }

    @Override
    public void deleteAdmin(String id) {
        adminRepository.deleteById(id);
    }

    @Override
    public boolean existsByUsername(String userName) {
        return adminRepository.existsByUserName(userName);
    }
}
