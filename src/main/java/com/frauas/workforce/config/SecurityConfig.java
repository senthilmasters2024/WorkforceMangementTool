
package com.frauas.workforce.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        http
                // ENABLE CORS (THIS IS THE KEY FIX)
                .cors(Customizer.withDefaults())

                // Disable CSRF for REST APIs
                .csrf(csrf -> csrf.disable())

                .authorizeHttpRequests(auth -> auth
                        //  ALLOW PREFLIGHT REQUESTS
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()

                        // Allow everything (as you already had)
                        .anyRequest().permitAll()
                )

                .headers(headers -> headers
                        .frameOptions(frameOptions -> frameOptions.disable())
                )

                // Stateless API
                .sessionManagement(session -> session.disable())
                .securityContext(context -> context.disable())
                .formLogin(form -> form.disable())
                .httpBasic(basic -> basic.disable());

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}



//
//
//@Configuration
//@EnableWebSecurity
//@EnableMethodSecurity(prePostEnabled = true)
//public class SecurityConfig {
//
//    @Autowired
//    private SessionAuthenticationFilter sessionAuthenticationFilter;
//
////    @Bean
////    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
////        http
////                .csrf(csrf -> csrf.disable())  // Disable CSRF for simplicity
////                .authorizeHttpRequests(auth -> auth
////                        // Public endpoints
////                        .requestMatchers("/api/auth/**").permitAll()
////                        .requestMatchers("/h2-console/**").permitAll()
////                        .requestMatchers("/swagger-ui/**", "/api-docs/**", "/swagger-ui.html", "/v3/api-docs/**").permitAll()
////                        .requestMatchers("/actuator/**").permitAll()
////
////                        // Employee endpoints - role-based access
////                        .requestMatchers("/api/employees/**").authenticated()
////
////                        // All other requests require authentication
////                        .anyRequest().authenticated()
////                )
////                .headers(headers -> headers
////                        .frameOptions(frameOptions -> frameOptions.disable()) // For H2 console
////                )
////                .sessionManagement(session -> session
////                        .maximumSessions(1)
////                        .maxSessionsPreventsLogin(false)
////                )
////                .addFilterBefore(sessionAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
////
////        return http.build();
////    }
////
////    @Bean
////    public HttpSessionEventPublisher httpSessionEventPublisher() {
////        return new HttpSessionEventPublisher();
////    }
//
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//
//        http
//                .csrf(csrf -> csrf.disable())
//                .authorizeHttpRequests(auth -> auth
//                        .anyRequest().permitAll()   // allow everything
//                )
//                .headers(headers -> headers
//                        .frameOptions(frameOptions -> frameOptions.disable())
//                )
//                .sessionManagement(session -> session.disable())
//                .securityContext(context -> context.disable())
//                .formLogin(form -> form.disable())
//                .httpBasic(basic -> basic.disable());
//
//        return http.build();
//    }
//
////    @Bean
////    public PasswordEncoder passwordEncoder() {
////        return new BCryptPasswordEncoder();
////    }
//
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//}

