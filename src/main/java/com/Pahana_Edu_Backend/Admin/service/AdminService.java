package com.Pahana_Edu_Backend.Admin.service;

import com.Pahana_Edu_Backend.Admin.entity.Admin;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface AdminService {

    List<Admin> getAllAdmins();

    Optional<Admin> getAdminById(String id);

    Admin createAdmin(Admin admin);

    Admin updateAdmin(String id, Admin admin);

    void deleteAdmin(String id);

    boolean existsByUsername(String userName);
}
