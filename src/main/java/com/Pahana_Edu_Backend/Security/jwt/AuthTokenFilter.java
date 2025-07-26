package com.Pahana_Edu_Backend.Security.jwt;

import java.io.IOException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.filter.OncePerRequestFilter;

import com.Pahana_Edu_Backend.Security.UserDetailsServiceImpl;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;


import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import jakarta.servlet.FilterChain;
import org.springframework.security.core.context.SecurityContextHolder;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

public class AuthTokenFilter  extends OncePerRequestFilter{
   
    @Autowired
    private JwtUtils jwtUtils;

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    private String praseJwtFromHeader(HttpServletRequest request) {

        String authHeader = request.getHeader("Authorization");

        if(StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer ")) {

            return authHeader.substring(7);
    
    }else {

        return null;
    }

}

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,FilterChain filterChain) throws
    IOException, ServletException {
        
        try {
            String jwtToken = praseJwtFromHeader(request);

            if(jwtToken != null && jwtUtils.validateJwtToken(jwtToken)) {

                String username = jwtUtils.getUsernameFromJwtToken(jwtToken);

                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(userDetails, null,
                userDetails.getAuthorities());
            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // authenticationToken.setDetails(new WebAuthenticationDetails(request));/// error

                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }catch (Exception e){

            e.printStackTrace();
        }

        filterChain.doFilter(request, response);
    }
}
