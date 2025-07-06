// package com.Pahana_Edu_Backend.Security;

// import java.util.List;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.web.servlet.MultipartConfigFactory;
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;

// import org.springframework.security.authentication.AuthenticationManager;
// import org.springframework.security.authentication.dao.DaoAuthenticationProvider;

// import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
// import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
// import org.springframework.security.config.annotation.web.builders.HttpSecurity;

// import org.springframework.security.config.annotation.web.builders.HttpSecurity;
// import org.springframework.security.config.http.SessionCreationPolicy;

// import com.Pahana_Edu_Backend.Security.jwt.AuthTokenFilter;

// import jakarta.servlet.MultipartConfigElement;

// import org.springframework.security.core.userdetails.UserDetailsService;
// import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
// import org.springframework.security.crypto.password.PasswordEncoder;
// import org.springframework.security.web.SecurityFilterChain;

// import org.springframework.security.web.SecurityFilterChain;
// import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
// import org.springframework.web.multipart.MultipartResolver;
// import org.springframework.web.multipart.support.StandardServletMultipartResolver;

// import com.Pahana_Edu_Backend.Security.jwt.AuthEntryPoint;
// import com.Pahana_Edu_Backend.Security.jwt.AuthTokenFilter;

// // import org.springframework.context.annotation.Bean;
// // import org.springframework.context.annotation.Configuration;
// // import org.springframework.web.multipart.MultipartResolver;
// // import org.springframework.web.multipart.support.StandardServletMultipartResolver;

// // import com.example.MegaCityCab_Booking_System.Security.jwt.AuthEntryPoint;
// // import com.example.MegaCityCab_Booking_System.Security.jwt.AuthTokenFilter;

// @Configuration
// @EnableMethodSecurity
// public class WebSecurityConfig {

//     @Autowired
//     private UserDetailsServiceImpl userDetailsService;

//     @Autowired
//     private AuthEntryPoint unauthorizedHandler;

//     @Bean
//     public UserDetailsService userDetailsService() {
//         return userDetailsService;
//     }

//     @Bean
//     public AuthTokenFilter authenticAuthTokenFilter() {
//         return new AuthTokenFilter();
//     }
    


//        @Bean
//     public StandardServletMultipartResolver multipartResolver() {
//         return new StandardServletMultipartResolver();
//     }

//     @Bean
//     public PasswordEncoder passwordEncoder() {
//         return new BCryptPasswordEncoder();
//     }

//     @Bean
//     public DaoAuthenticationProvider authenticationProvider() {
//         DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
//         provider.setUserDetailsService(userDetailsService);
//         provider.setPasswordEncoder(passwordEncoder());
//         return provider;
//     }

//     @Bean
//     public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
//          return authConfig.getAuthenticationManager();
//     }

//     @Bean
//     public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//         http
//             .cors(cors -> cors.configurationSource(request -> {
//                 var corsConfig = new org.springframework.web.cors.CorsConfiguration();
//                 corsConfig.setAllowedOrigins(List.of("http://localhost:5173")); // Adjust frontend URL accordingly
//                 corsConfig.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
//                 corsConfig.setAllowedHeaders(List.of("*"));
//                 return corsConfig;
//             }))
//             .csrf(csrf -> csrf.disable())
//             .exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler))
//             .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//             .authorizeHttpRequests(auth -> auth
//                 .requestMatchers("/auth/**").permitAll()
//                 .anyRequest().authenticated());

//         http.authenticationProvider(authenticationProvider());
//         http.addFilterBefore(authenticAuthTokenFilter(), UsernamePasswordAuthenticationFilter.class);

//         return http.build();
//     }


  
// }
package com.Pahana_Edu_Backend.Security;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import com.Pahana_Edu_Backend.Security.jwt.AuthEntryPoint;
import com.Pahana_Edu_Backend.Security.jwt.AuthTokenFilter;

@Configuration
@EnableMethodSecurity
public class WebSecurityConfig {

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private AuthEntryPoint unauthorizedHandler;

    @Bean
    public UserDetailsServiceImpl userDetailsService() {
        return userDetailsService;
    }

    @Bean
    public AuthTokenFilter authenticAuthTokenFilter() {
        return new AuthTokenFilter();
    }

    @Bean
    public StandardServletMultipartResolver multipartResolver() {
        return new StandardServletMultipartResolver();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .cors(cors -> cors.configurationSource(request -> {
                var corsConfig = new org.springframework.web.cors.CorsConfiguration();
                corsConfig.setAllowedOrigins(List.of("http://localhost:5173"));
                corsConfig.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
                corsConfig.setAllowedHeaders(List.of("*"));
                corsConfig.setAllowCredentials(true);
                return corsConfig;
            }))
            .csrf(csrf -> csrf.disable())
            .exceptionHandling(exception -> exception.authenticationEntryPoint(unauthorizedHandler))
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/auth/**").permitAll()
                .anyRequest().authenticated());

        http.authenticationProvider(authenticationProvider());
        http.addFilterBefore(authenticAuthTokenFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}