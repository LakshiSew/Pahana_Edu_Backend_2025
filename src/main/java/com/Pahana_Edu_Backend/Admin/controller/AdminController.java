package com.Pahana_Edu_Backend.Admin.controller;

import com.Pahana_Edu_Backend.Admin.entity.Admin;
import com.Pahana_Edu_Backend.Admin.service.AdminService;
import com.Pahana_Edu_Backend.Cloudinary.CloudinaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private CloudinaryService cloudinaryService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    // ✅ Create Admin
    @PostMapping("/createadmin")
    public ResponseEntity<?> createAdmin(
            @RequestParam("userName") String userName,
            @RequestParam("password") String password,
            @RequestParam("email") String email,
            @RequestParam("position") String position,
            @RequestParam("adminImage") MultipartFile adminImage
    ) throws IOException {

        if (adminService.existsByUsername(userName)) {
            return ResponseEntity.status(409).body("Username already exists");
        }

        String adminImageUrl = cloudinaryService.uploadImage(adminImage);

        Admin admin = new Admin();
        admin.setUserName(userName);
        admin.setPassword(passwordEncoder.encode(password));
        admin.setEmail(email);
        admin.setPosition(position);
        admin.setAdminImage(adminImageUrl);

        Admin savedAdmin = adminService.createAdmin(admin);
        return ResponseEntity.ok(savedAdmin);
    }

    // ✅ Get All Admins
    @GetMapping("/getalladmins")
    public List<Admin> getAllAdmins() {
        return adminService.getAllAdmins();
    }

    // ✅ Get Admin by ID
    @GetMapping("/getadminbyid/{id}")
    public ResponseEntity<?> getAdminById(@PathVariable String id) {
        Optional<Admin> admin = adminService.getAdminById(id);
        return admin.isPresent()
                ? ResponseEntity.ok(admin.get())
                : ResponseEntity.status(404).body("Admin not found");
    }

    // ✅ Delete Admin by ID
    @DeleteMapping("/deleteadmin/{id}")
    public ResponseEntity<?> deleteAdmin(@PathVariable String id) {
        if (adminService.getAdminById(id).isPresent()) {
            adminService.deleteAdmin(id);
            return ResponseEntity.ok("Admin deleted successfully");
        } else {
            return ResponseEntity.status(404).body("Admin not found");
        }
    }

    // ✅ Update Admin
    @PutMapping("/updateadmin/{id}")
    public ResponseEntity<?> updateAdmin(
            @PathVariable String id,
            @RequestParam("userName") String userName,
            @RequestParam("email") String email,
            @RequestParam("position") String position,
            @RequestParam(value = "password", required = false) String password,
            @RequestParam(value = "adminImage", required = false) MultipartFile adminImage
    ) throws IOException {
        try {
            String adminImageUrl = adminImage != null ? cloudinaryService.uploadImage(adminImage) : null;

            Admin updatedData = new Admin();
            updatedData.setUserName(userName);
            updatedData.setEmail(email);
            updatedData.setPosition(position);
            updatedData.setPassword(password != null && !password.isEmpty() ? passwordEncoder.encode(password) : null);
            updatedData.setAdminImage(adminImageUrl);

            Admin updatedAdmin = adminService.updateAdmin(id, updatedData);
            return ResponseEntity.ok(updatedAdmin);
        } catch (RuntimeException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        }
    }
}
