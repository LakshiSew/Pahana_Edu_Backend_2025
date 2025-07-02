package com.Pahana_Edu_Backend.Security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Pahana_Edu_Backend.Admin.entity.Admin;
import com.Pahana_Edu_Backend.Admin.repository.AdminRepository;
import com.Pahana_Edu_Backend.Customer.entity.Customer;
import com.Pahana_Edu_Backend.Customer.repository.CustomerRepository;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private AdminRepository adminRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        // Check Customer
        Customer customer = customerRepository.findByUserName(username).orElse(null);
        if (customer != null) {
            return org.springframework.security.core.userdetails.User.builder()
                    .username(customer.getUserName())
                    .password(customer.getPassword())
                    .authorities("ROLE_CUSTOMER")
                    .build();
        }

        // Check Admin (Manager or Staff)
        Admin admin = adminRepository.findByUserName(username).orElse(null);
        if (admin != null) {
            String role = admin.getPosition().equalsIgnoreCase("Manager") ? "ROLE_MANAGER" : "ROLE_STAFF";
            return org.springframework.security.core.userdetails.User.builder()
                    .username(admin.getUserName())
                    .password(admin.getPassword())
                    .authorities(role)
                    .build();
        }

        throw new UsernameNotFoundException("User not found with username: " + username);
    }
}
