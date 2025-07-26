package com.Pahana_Edu_Backend.Security.jwt;

import java.security.Key;
import java.util.Date;
import java.util.concurrent.atomic.AtomicReference;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.Pahana_Edu_Backend.Admin.repository.AdminRepository;
import com.Pahana_Edu_Backend.Customer.repository.CustomerRepository;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

@Configuration
public class JwtUtils {

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private AdminRepository adminRepository;

    @Value("${app.secret}")
    private String secret;

    public static final String ROLE_CUSTOMER = "ROLE_CUSTOMER";
    public static final String ROLE_MANAGER = "ROLE_MANAGER";
    public static final String ROLE_STAFF = "ROLE_STAFF";

    private Key key() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
    }

    public String generateJwtToken(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();

        String role = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .findFirst()
                .orElse(ROLE_CUSTOMER); 

        String userId = null;
        AtomicReference<String> customRole = new AtomicReference<>(role);

        if (ROLE_CUSTOMER.equals(role)) {
            userId = customerRepository.findByUserName(username)
                    .map(c -> c.getCustomerId())
                    .orElse(null);

        } else {
            // Admin logic
            userId = adminRepository.findByUserName(username)
                    .map(admin -> {
                        String position = admin.getPosition();
                        if ("Manager".equalsIgnoreCase(position)) {
                            customRole.set(ROLE_MANAGER);
                        } else if ("Staff".equalsIgnoreCase(position)) {
                            customRole.set(ROLE_STAFF);
                        }
                        return admin.getId();
                    })
                    .orElse(null);
        }

        return Jwts.builder()
                .setSubject(username)
                .claim("role", customRole.get())
                .claim("userId", userId)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24)) // 24h
                .signWith(key(), SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean validateJwtToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key()).build().parse(token);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public String getUsernameFromJwtToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    public String getUserIdFromJwtToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("userId", String.class);
    }

    public String getRoleFromJwtToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .get("role", String.class);
    }
}
